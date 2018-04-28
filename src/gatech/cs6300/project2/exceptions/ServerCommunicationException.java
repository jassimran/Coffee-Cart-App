package gatech.cs6300.project2.exceptions;

public class ServerCommunicationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServerCommunicationException() {
		super();
	}
	
	public ServerCommunicationException(final String message) {
		super(message);
	}
	
	public ServerCommunicationException(final Throwable cause) {
		super(cause);
	}
	
	public ServerCommunicationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
