package gatech.cs6300.project2.exceptions;

public class DuplicateCustomerException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public DuplicateCustomerException() {
		super();
	}
	
	public DuplicateCustomerException(final String message) {
		super(message);
	}
	
	public DuplicateCustomerException(final Throwable cause) {
		super(cause);
	}
	
	public DuplicateCustomerException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
