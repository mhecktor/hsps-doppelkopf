package hsps.services.mqtt;

public class Topic {
	/*
	 * Fuer eine neue Topic kann bspw. einfach ein neues Klassenattribut
	 * ergaenzt werden vom Typ String
	 */
	public static final String GENERELL = "generell";

	public static String genPlayerTopic( String spielID, int playerIndex ) {
		return spielID + playerIndex;
	}
}
