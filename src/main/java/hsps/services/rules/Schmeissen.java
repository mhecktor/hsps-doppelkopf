package hsps.services.rules;

import logic.Karte;
import logic.Spiel;
import logic.Spieler;
import logic.Stich;

public class Schmeissen implements Rule {

	/*
	 * Schmei�en � da die K�nige die niedrigste Karte im Spiel sind, soll ein
	 * Spieler, der 5 K�nige hat, die M�glichkeit haben, ein neues Blatt zu
	 * bekommen, da er sonst einen Nachteil hat.
	 */

	Spiel spiel;
	int anzKoenige = 0;

	@Override
	public boolean test( Stich stich, Karte karte ) {
		for( Spieler s : spiel.getSpielerliste() ) {
			for( Karte k : s.getHand().getKarten() ) {
				if( k.getSymbolik().toString() == "KOENIG" ) {
					anzKoenige++;
				}
			}
			if( anzKoenige >= 5 ) {
				// Nochmal genau �berlegen wie man das machen will.
				new Spiel();
				return true;
			}
		}
		return false;
	}

}
