package co.il.nmh.compare.files.exceptions;

/**
 * @author Maor Hamami
 */

public class PersistenceException extends RuntimeException
{
	private static final long serialVersionUID = 7523785336318598108L;

	public PersistenceException(Throwable cause)
	{
		super(cause);
	}
}
