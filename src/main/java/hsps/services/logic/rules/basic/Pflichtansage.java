package hsps.services.logic.rules.basic;

import javax.swing.JOptionPane;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;
import hsps.services.logic.player.Spieler;

public class Pflichtansage implements Rule {

	/*
	 * Pflichtansage bei 30 Punkten im ersten Stich - der erste Stich ist sehr
	 * entscheidend fuer den Spielverlauf. Wenn man in diesem mehr als 30 Punkte
	 * holt, muss Kontra/Re gesagt werden, damit die Spieler wissen, wer deren
	 * Partner ist. Somit soll etwas Bedeutung aus dem ersten Stich genommen
	 * werden und das Teamspiel im Mittelpunkt stehen.
	 */

	private Spiel spiel;

	@Override
	public boolean test( Spiel spiel ) {
		this.spiel = spiel;
		for( Spieler s : spiel.getSpielerliste() ) {
			if( s.getGesammelteStiche().get( 0 ).getPunktezahl() >= 30 ) { return true; }
		}
		return false;
	}

	@Override
	public void perform() {
		// Direkt hier implementieren?
		for( Spieler spieler : spiel.getSpielerliste() ) {
			for( Karte k : spieler.getHand().getKarten() ) {
				if( k.getSymbolik() == Symbolik.DAME && k.getFarbwert() == Farbwert.KREUZ ) {
					// JOPtionPane nur beispielhaft verwendet muss ueber
					// Messaging Service gemacht werden
					//JOptionPane.showMessageDialog( null, "Spieler " + spieler + "besitzt eine Kreuz-Dame" );
				}
			}
		}
	}

}
