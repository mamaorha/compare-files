package co.il.nmh.compare.files.exceptions;

/**
 * @author Maor Hamami
 */

public class DuplicateScannerException extends RuntimeException
{
	private static final long serialVersionUID = 7406285700200822348L;

	public DuplicateScannerException(String error)
	{
		super(error);
	}
}
