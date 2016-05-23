package exceptions;

/**
 * Thrown when trying to access a file that doesn't exist.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class FileDoesNotExistException extends RuntimeException {

	public FileDoesNotExistException() {
		// TODO Auto-generated constructor stub
	}

	public FileDoesNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FileDoesNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public FileDoesNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FileDoesNotExistException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
