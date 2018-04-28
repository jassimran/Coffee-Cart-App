package gatech.cs6300.project2.exceptions;

public class BadRequestException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public BadRequestException() {
		super();
	}
	
	public BadRequestException(final String message) {
		super(message);
	}
	
	public BadRequestException(final Throwable cause) {
		super(cause);
	}
	
	public BadRequestException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
