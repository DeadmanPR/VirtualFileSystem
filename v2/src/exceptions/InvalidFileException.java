package exceptions;

/**
 * Thrown when accessing a disk file that doesn't meet the format specified.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class InvalidFileException extends RuntimeException {

	public InvalidFileException() {
		// TODO Auto-generated constructor stub
	}

	public InvalidFileException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidFileException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidFileException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidFileException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
