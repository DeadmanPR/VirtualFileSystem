package exceptions;

/**
 * Thrown when trying to access a disk that doesn't exist.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class NonExistingDiskException extends RuntimeException {

	public NonExistingDiskException() {
		// TODO Auto-generated constructor stub
	}

	public NonExistingDiskException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public NonExistingDiskException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public NonExistingDiskException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public NonExistingDiskException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
