package hsps.services.exception;

public class NotAValidCardException extends Exception {

	private static final long serialVersionUID = 2781522803774950367L;

	public NotAValidCardException() {
		super( "Karte nicht gueltig" );
	}

	public NotAValidCardException( String message ) {
		super( message );
	}
}