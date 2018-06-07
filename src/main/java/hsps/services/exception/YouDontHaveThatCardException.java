package hsps.services.exception;

public class YouDontHaveThatCardException extends Exception {

	private static final long serialVersionUID = 2781522803774952367L;

	public YouDontHaveThatCardException() {
		super( "Sie können diese Karte nicht spielen, da sie sie nicht auf der Hand halten" );
	}

	public YouDontHaveThatCardException(String message ) {
		super( message );
	}
}