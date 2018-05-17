package hsps.services.rules;

public class Schweinchen implements Rule {

	/*
	 * Schweinchen - durch die Fuechse lassen sich fuer die Gegner viele Punkte
	 * holen. Wenn ein Spieler beide Fuechse hat, minimieren sich seine
	 * Gewinnchancen. Durch die Schweinchen-Regel soll dies verhindert werden,
	 * indem die Fuechse zur hoechsten Karte werden, sobald ein Spieler beide hat.
	 * Dies muss beim Ausspielen angesagt werden.
	 */

	@Override
	public boolean test() {

		return false;
	}

}
