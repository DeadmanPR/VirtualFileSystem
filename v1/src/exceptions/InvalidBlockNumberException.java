package exceptions;

/**
 * Thrown when the block number given is not valid.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class InvalidBlockNumberException extends RuntimeException {

	public InvalidBlockNumberException() {
		// TODO Auto-generated constructor stub
	}

	public InvalidBlockNumberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidBlockNumberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidBlockNumberException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidBlockNumberException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
