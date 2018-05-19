package hsps.services.rules;


import javax.swing.JOptionPane;

import logic.Karte;
import logic.Spiel;
import logic.Spieler;
import logic.Stich;

public class Pflichtansage implements Rule {

	/*
	 * Pflichtansage bei 30 Punkten im ersten Stich � der erste Stich ist sehr
	 * entscheidend f�r den Spielverlauf. Wenn man in diesem mehr als 30 Punkte
	 * holt, muss Kontra/Re gesagt werden, damit die Spieler wissen, wer deren
	 * Partner ist. Somit soll etwas Bedeutung aus dem ersten Stich genommen
	 * werden und das Teamspiel im Mittelpunkt stehen.
	 */

	Spiel spiel;

	@Override
	public boolean test(  Stich stich, Karte karte ) {

		for (Spieler s : spiel.getSpielerliste()) {
			if (s.getErstenStich().getPunktezahl() >= 30) {
				// Direkt hier implementieren?
				for (Spieler spieler : spiel.getSpielerliste()) {
					for (Karte k : spieler.getHand().getKarten()) {
						if (k.getSymbolik().toString().equals("DAME") && k.getFarbwert().toString().equals("KREUZ")) {
							//JOPtionPane nur beispielhaft verwendet muss �ber Messaging Service gemacht werden
							JOptionPane.showMessageDialog(null,
									"Spieler " + spieler.getName() + "besitzt eine Kreuz-Dame");
						}
					}
				}
				return true;
			}
		}
		return false;
	}

}
