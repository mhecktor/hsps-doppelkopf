package hsps.services.logic.rules.basic;

import javax.swing.JOptionPane;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;
import hsps.services.logic.player.Spieler;

public class Schmeissen implements Rule {

	/*
	 * Schmeissen - da die Koenige die niedrigste Karte im Spiel sind, soll ein
	 * Spieler, der 5 Koenige hat, die Moeglichkeit haben, ein neues Blatt zu
	 * bekommen, da er sonst einen Nachteil hat.
	 */

	private int anzKoenige = 0;
	private Spiel spiel;

	@Override
	public boolean test( Spiel spiel ) {
		this.spiel = spiel;
		for( Spieler s : spiel.getSpielerliste() ) {
			for( Karte k : s.getHand().getKarten() ) {
				if( k.getSymbolik() == Symbolik.KOENIG ) {
					anzKoenige++;
				}
			}
			if( anzKoenige >= 5 ) {
				// Nochmal genau ueberlegen wie man das machen will.
				//int rueckgabe = JOptionPane.showConfirmDialog( null, "Viele Koenige auf der Hand/n Willst du ( " + s + ") schmeissen?", "Schmeissen", JOptionPane.YES_NO_OPTION );
				int rueckgabe = 0;
				if( rueckgabe == JOptionPane.YES_OPTION ) {
					return true;
				} else {
					return false;
				}
			}
			anzKoenige = 0;
		}
		return false;
	}

	@Override
	public void perform() {
		spiel.neustarten();
	}

}
