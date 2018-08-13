package co.il.nmh.compare.files.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Maor Hamami
 */

public class Signature
{
	public static String getBaseSignature(File file)
	{
		try
		{
			int maxLength = 15 * 1000000;

			if (file.length() < maxLength)
			{
				maxLength = (int) file.length();
			}

			byte[] buffer = new byte[maxLength];

			FileInputStream fis = new FileInputStream(file.getPath());
			fis.read(buffer, 0, buffer.length);
			fis.close();

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(buffer);

			String FileBit = DatatypeConverter.printBase64Binary(result);

			return FileBit;
		}

		catch (Exception e)
		{
		}

		return null;
	}

	public static String getFullSignature(String baseSignature, File file)
	{
		try
		{
			int maxLength = 15 * 1000000;

			if (file.length() <= maxLength)
			{
				return baseSignature;
			}

			StringBuilder stringBuilder = new StringBuilder();
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file.getPath());

			do
			{
				int available = fis.available();

				if (available < maxLength)
				{
					maxLength = available;
				}

				byte[] buffer = new byte[maxLength];

				fis.read(buffer, 0, buffer.length);

				byte[] result = md.digest(buffer);
				String FileBit = DatatypeConverter.printBase64Binary(result);

				stringBuilder.append(FileBit);
			} while (fis.available() > 0);

			fis.close();

			return stringBuilder.toString();
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
