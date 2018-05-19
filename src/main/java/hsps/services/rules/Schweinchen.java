package hsps.services.rules;

import javax.swing.JOptionPane;

import logic.Karte;
import logic.Spiel;
import logic.Spieler;
import logic.Stich;

public class Schweinchen implements Rule {

	/*
	 * Schweinchen � durch die F�chse lassen sich f�r die Gegner viele Punkte
	 * holen. Wenn ein Spieler beide F�chse hat, minimieren sich seine
	 * Gewinnchancen. Durch die Schweinchen-Regel soll dies verhindert werden,
	 * indem die F�chse zur h�chsten Karte werden, sobald ein Spieler beide hat.
	 * Dies muss beim Ausspielen angesagt werden.
	 */

	int anzFuechse = 0;
	Spiel spiel;

	@Override
	public boolean test( Stich stich, Karte karte ) {
		// Durch die Hand von jedem Spieler Iterieren und Gucken ob einer 2
		// Karo-Asse Besitzt
		for( Spieler s : spiel.getSpielerliste() ) {
			for( Karte k : s.getHand().getKarten() ) {
				if( k.getSymbolik().toString().equals( "ASS" ) && k.getFarbwert().toString().equals( "KARO" ) ) {
					anzFuechse++;
				}
			}

			// Sollte jemand zwei Karo-Asse besitzen tritt die Regel in Kraft
			if( anzFuechse == 2 ) {
				// JOptionPane nur Beispielhaft, muss �ber Messaging Service
				// gemacht werden
				JOptionPane.showMessageDialog( null, "Spieler: " + s.getName() + ", besitzt zwei Karo-Ass Karten und wendet die Regel Schweinchen an." );
				for( Spieler spieler : spiel.getSpielerliste() ) {
					for( Karte karte : spieler.getHand().getKarten() ) {
						if( karte.getSymbolik().toString().equals( "ASS" ) && karte.getFarbwert().toString().equals( "KARO" ) ) {
							// Karo-Asse m�ssen noch als Bester Trumpf Markiert
							// werden, �ber weitere Variable bzw. boolean?!
							karte.setTrumpf( true );
						}
					}

				}
				return true;
			}

			anzFuechse = 0;
		}
		return false;
	}

}
