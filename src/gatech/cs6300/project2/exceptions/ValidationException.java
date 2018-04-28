package gatech.cs6300.project2.exceptions;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}
	
	public ValidationException(final String message) {
		super(message);
	}
	
	public ValidationException(final Throwable cause) {
		super(cause);
	}
	
	public ValidationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
