package exceptions;

/**
 * Thrown when creation of a disk with an already existing name is attempted.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class ExistingDiskException extends RuntimeException {

	public ExistingDiskException() {
		// TODO Auto-generated constructor stub
	}

	public ExistingDiskException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ExistingDiskException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ExistingDiskException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ExistingDiskException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
