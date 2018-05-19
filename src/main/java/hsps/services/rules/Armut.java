package hsps.services.rules;

import logic.Karte;
import logic.Spiel;
import logic.Spieler;
import logic.Stich;

public class Armut implements Rule {
	/*
	 * Armut � da Tr�mpfe in dem Spiel sehr wichtig sind, bestimmt das Gl�ck
	 * dar�ber, wie gut die Chancen auf den Sieg stehen. Um dieses zu
	 * verhindern, wird neu gegeben, sobald ein Spieler 3 oder weniger Tr�mpfe
	 * auf der Hand hat.
	 */

	Spiel spiel;
	int anzahlTruempfe = 0;

	@Override
	public boolean test( Stich stich, Karte karte ) {
		for (Spieler s : spiel.getSpielerliste()) {
			for (Karte k : s.getHand().getKarten()) {
				if (k.isTrumpf()) {
					anzahlTruempfe++;
				}
			}
		}
		if (anzahlTruempfe <= 3) {
			//Einfach neues Spiel erstellen?
			new Spiel();
			return true;
		} else
			return false;

	}

}
