package hsps.services.exception;

public class NotYourTurnException extends Exception {

    public NotYourTurnException() {
        super( "Sie dürfen nicht ziehen, sie sind nicht am Zug" );
    }

    public NotYourTurnException(String message ) {
        super( message );
    }
}