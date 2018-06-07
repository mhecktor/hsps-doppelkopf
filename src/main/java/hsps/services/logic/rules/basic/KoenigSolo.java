package hsps.services.logic.rules.basic;

import javax.swing.JOptionPane;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;
import hsps.services.logic.player.Spieler;

public class KoenigSolo implements Rule {

	/*
	 * Koenigssolo - da der Koenig absolut keinen Wert hat, kann jemand der
	 * viele
	 * Koenige auf der Hand hat entweder Schmeissen oder ein Solo ansagen,
	 * sodass
	 * er faire Chancen bekommt.
	 */

	private int anzKoenige = 0;
	private Spiel spiel;

	@Override
	public boolean test( Spiel spiel ) {
		this.spiel = spiel;
		// Durch jede Hand von jedem Spieler Iterieren
		for( Spieler s : spiel.getSpielerliste() ) {
			for( Karte k : s.getHand().getKarten() ) {
				if( k.getSymbolik() == Symbolik.KOENIG ) {
					anzKoenige++;
				}
				// Wenn Anzahl Koenige >= 5
				// Zwei Moeglichkeiten fuer den Spieler anbieten
				// 1. Schmeissen, 2. Koenigssolo
				if( anzKoenige >= 5 ) {
					// JOPtionPane nur Beispielhaft verwendet, besser ueber
					// scanner o?.
					//int rueckgabe = JOptionPane.showConfirmDialog( null, "Koenigsolo spielen?", "Koenigsolo", JOptionPane.YES_NO_OPTION );
					int rueckgabe = 0;
					if( rueckgabe == JOptionPane.YES_OPTION ) {
						s.setSolo( true );
						return true;
					} else {
						return false;
					}
				}
			}
			anzKoenige = 0;
		}
		return false;
	}

	@Override
	public void perform() {
		// Durch jede Hand von jedem Spieler Iterieren
		// und Die Truempfe fuer dieses Spiel aendern
		for( Spieler spieler : spiel.getSpielerliste() ) {
			for( Karte karte : spieler.getHand().getKarten() ) {
				if( karte.getSymbolik() == Symbolik.KOENIG ) {
					karte.setTrumpf( false );
				} else {
					karte.setTrumpf( true );
				}
			}
		}

	}
}
