package hsps.services.rules;

import logic.Karte;
import logic.Spiel;
import logic.Spieler;
import logic.Stich;

public class ZweiteDulle implements Rule {

	/*
	 * Zweite Dulle sticht erste � die Garantie einen Stich zu bekommen, l�sst
	 * wenig Spielraum f�r Taktik, weswegen wir uns dazu entschieden haben,
	 * diese Regel hinzuzuf�gen. Jedoch gilt dies nicht f�r den letzten Stich.
	 */

	Spiel spiel;

	@Override
	public boolean test(  Stich stich, Karte karte ) {
		//Nicht Fertig
		for (Spieler spieler : spiel.getSpielerliste()) {
			for (Karte k : spieler.getHand().getKarten()) {
				if (k.getFarbwert().toString().equals("HERZ") && k.getSymbolik().toString().equals("ZEHN")) {
					Karte Dulle = k;

					// Spieler gewinnt automatisch den Stich keine
					// �berpr�fung n�tig ob zweite dulle gelegt wurde

					return true;
				}
			}
		}
		return false;
	}

}
