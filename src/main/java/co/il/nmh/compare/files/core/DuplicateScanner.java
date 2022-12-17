package co.il.nmh.compare.files.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import co.il.nmh.compare.files.data.CFile;
import co.il.nmh.compare.files.data.DuplicateBySize;
import co.il.nmh.compare.files.exceptions.DuplicateScannerException;
import co.il.nmh.compare.files.exceptions.PersistenceException;
import co.il.nmh.compare.files.gui.listeners.DuplicateScannerListener;
import co.il.nmh.compare.files.persistence.FilesDAO;
import co.il.nmh.easy.utils.EasyThread;
import co.il.nmh.easy.utils.FileUtils;

/**
 * @author Maor Hamami
 */

public class DuplicateScanner extends EasyThread
{
	private static final int MAX_DATA = 1000; // maximum amount of data to hold before putting it into db

	protected DuplicateScannerListener duplicateScannerListener;
	protected File directoryFile;
	protected Pattern filter;
	protected boolean subDirectories;
	protected boolean fullSignature;

	public DuplicateScanner(DuplicateScannerListener duplicateScannerListener, String directory, String filter, boolean subDirectories, boolean fullSignature)
	{
		super("DuplicateScanner");

		this.duplicateScannerListener = duplicateScannerListener;
		this.subDirectories = subDirectories;
		this.fullSignature = fullSignature;

		this.directoryFile = new File(directory);

		if (!directoryFile.exists() || !directoryFile.isDirectory())
		{
			throw new DuplicateScannerException("Invalid directory - " + directory);
		}

		filter = filter.trim();
		filter = filter.replace(" ", "");

		String patt = "(.+)";

		if (filter.length() > 0)
		{
			patt += "\\.(?i)(" + filter + ")";
		}

		this.filter = Pattern.compile(patt);
	}

	@Override
	public boolean loopRun()
	{
		duplicateScannerListener.updateProgress(0, 100, "Initializing db");

		FilesDAO filesDAO = null;

		int initTries = 0;

		while (null == filesDAO)
		{
			try
			{
				filesDAO = new FilesDAO();
			}
			catch (PersistenceException e)
			{
				if (initTries++ > 2)
				{
					duplicateScannerListener.failed("Failed to initialize db");
					return false;
				}

				else
				{
					try
					{
						Thread.sleep(15);
					}
					catch (InterruptedException e1)
					{
						return false;
					}
				}
			}
		}

		try
		{
			if (!isInterrupted())
			{
				mapFiles(filesDAO);
			}

			Map<String, List<CFile>> duplicates = null;

			if (!isInterrupted())
			{
				duplicates = filterDuplicateBySignature(filesDAO);
			}

			if (!isInterrupted())
			{
				duplicateScannerListener.done(duplicates, filesDAO.count());
			}
		}
		catch (Exception e)
		{
			duplicateScannerListener.failed("Error occured - " + e.getMessage());
		}
		finally
		{
			if (null != filesDAO)
			{
				filesDAO.close();
				filesDAO = null;
			}
		}

		return false;
	}

	private void mapFiles(FilesDAO filesDAO)
	{
		duplicateScannerListener.updateProgress(0, 100, "Mapping file list");

		List<CFile> scannedFiles = new ArrayList<>();

		List<File> directories = new ArrayList<File>();
		directories.add(directoryFile);

		while (directories.size() > 0)
		{
			if (isInterrupted())
			{
				break;
			}

			try
			{
				duplicateScannerListener.updateProgress(0, 100, "Mapping " + directories.get(0));

				File[] listFiles = directories.get(0).listFiles();

				for (File fileEntry : listFiles)
				{
					if (isInterrupted())
					{
						break;
					}

					try
					{
						if (fileEntry.isDirectory())
						{
							if (subDirectories)
							{
								directories.add(fileEntry);
							}
						}

						else
						{
							if (filter.matcher(fileEntry.getName()).matches())
							{
								scannedFiles.add(new CFile(fileEntry.getName(), fileEntry.getPath(), String.valueOf(fileEntry.length())));
							}
						}
					}

					catch (Exception e)
					{
					}
				}
			}
			catch (Exception e)
			{
			}

			directories.remove(0);

			if (scannedFiles.size() >= MAX_DATA)
			{
				export(filesDAO, scannedFiles);
			}
		}

		if (!isInterrupted())
		{
			export(filesDAO, scannedFiles);
		}
	}

	private void export(FilesDAO filesDAO, List<CFile> scannedFiles)
	{
		for (CFile file : scannedFiles)
		{
			if (isInterrupted())
			{
				break;
			}

			filesDAO.insert(file.getName(), file.getLocation(), file.getSize());
		}

		scannedFiles.clear();
	}

	private Map<String, List<CFile>> filterDuplicateBySignature(FilesDAO filesDAO)
	{
		duplicateScannerListener.updateProgress(0, 100, "Gathering duplicate suspects");

		DuplicateBySize duplicateBySize = filesDAO.getDuplicateBySize();

		int total = duplicateBySize.getTotal();
		Map<String, List<CFile>> duplicatesBySize = duplicateBySize.getDuplicatesBySize();

		duplicateScannerListener.updateProgress(0, total);

		int i = 0;

		Set<String> duplicateBaseSignatures = new HashSet<>();
		Map<String, List<CFile>> filesByBaseSignature = new HashMap<>();

		for (List<CFile> files : duplicatesBySize.values())
		{
			if (isInterrupted())
			{
				break;
			}

			for (CFile file : files)
			{
				if (isInterrupted())
				{
					break;
				}

				duplicateScannerListener.updateProgress(i++, total, "Checking " + file.getName());

				String baseSignature = FileUtils.getBaseSignature(new File(file.getLocation()));

				if (!filesByBaseSignature.containsKey(baseSignature))
				{
					filesByBaseSignature.put(baseSignature, new ArrayList<>());
				}

				else
				{
					duplicateBaseSignatures.add(baseSignature);
				}

				filesByBaseSignature.get(baseSignature).add(file);
			}
		}

		duplicateScannerListener.updateProgress(i++, total, "Validating duplicate suspects");

		if (fullSignature)
		{
			Set<String> duplicateFullSignatures = new HashSet<>();
			Map<String, List<CFile>> filesByFullSignature = new HashMap<>();

			for (String baseSignature : duplicateBaseSignatures)
			{
				if (isInterrupted())
				{
					break;
				}

				List<CFile> files = filesByBaseSignature.get(baseSignature);

				for (CFile file : files)
				{
					if (isInterrupted())
					{
						break;
					}

					String fullSignature = FileUtils.getFullSignature(baseSignature, new File(file.getLocation()));

					if (!filesByFullSignature.containsKey(fullSignature))
					{
						filesByFullSignature.put(fullSignature, new ArrayList<>());
					}

					else
					{
						duplicateFullSignatures.add(fullSignature);
					}

					filesByFullSignature.get(fullSignature).add(file);
				}
			}

			Map<String, List<CFile>> duplicates = new HashMap<>();

			for (String fullSignature : duplicateFullSignatures)
			{
				if (isInterrupted())
				{
					break;
				}

				List<CFile> files = filesByFullSignature.get(fullSignature);
				duplicates.put(fullSignature, files);
			}

			return duplicates;
		}
		else
		{
			Map<String, List<CFile>> duplicates = new HashMap<>();

			for (String baseSignature : duplicateBaseSignatures)
			{
				if (isInterrupted())
				{
					break;
				}

				List<CFile> files = filesByBaseSignature.get(baseSignature);
				duplicates.put(baseSignature, files);
			}

			return duplicates;
		}
	}
}
