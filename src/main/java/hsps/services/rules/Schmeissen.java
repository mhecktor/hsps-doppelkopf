package hsps.services.rules;

public class Schmeissen implements Rule {

	/*
	 * Schmeissen - da die Koenige die niedrigste Karte im Spiel sind, soll ein
	 * Spieler, der 5 Koenige hat, die Moeglichkeit haben, ein neues Blatt zu
	 * bekommen, da er sonst einen Nachteil hat.
	 */

	@Override
	public boolean test() {
		return false;
	}

}
