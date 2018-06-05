package hsps.services.exception;

public class NotAValidCard extends Exception {

    public NotAValidCard() {
        super("Karte nicht gueltig");
    }

    public NotAValidCard(String message) {
        super(message);
    }
}