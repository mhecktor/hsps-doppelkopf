package hsps.services.rules;

import javax.swing.JOptionPane;

import logic.Karte;
import logic.Spiel;
import logic.Spieler;
import logic.Stich;

public class KoenigSolo implements Rule {

	Spiel spiel;
	int anzKoenige = 0;

	@Override
	public boolean test( Stich stich, Karte karte ) {
		//Durch jede Hand von jedem Spieler Iterieren
		for (Spieler s : spiel.getSpielerliste()) {
			for (Karte k : s.getHand().getKarten()) {
				if (k.getSymbolik().toString() == "KOENIG") {
					anzKoenige++;
				}
				//Wenn Anzahl K�nige >= 5
				// Zwei M�glichkeiten f�r den Spieler anbieten
				// 1. Schmei�en, 2. K�nigssolo
				if (anzKoenige >= 5) {
					//JOPtionPane nur Beispielhaft verwendet, besser �ber scanner o�.
					int rueckgabe = Integer.parseInt(JOptionPane.showInputDialog("1. Karten neu Verteilen\n" + "2. K�nigssolo"));
					if (rueckgabe == 1) {
						// Neues Spiel anlegen - Korrekt?!
						new Spiel();
					} else {
						//Durch jede Hand von jedem Spieler Iterieren
						//und Die Tr�mpfe f�r dieses Spiel �ndern
						for (Spieler spieler : spiel.getSpielerliste()) {
							for (Karte karte : spieler.getHand().getKarten()) {
								if(karte.getSymbolik().toString() != "KOENIG"){
									karte.setTrumpf(false);
								}else{
									karte.setTrumpf(true);
								}
							}
						}

					}
					return true;
				}
			}
			anzKoenige = 0;
		}
		return false;
	}

	/*
	 * K�nigssolo � da der K�nig absolut keinen Wert hat, kann jemand der viele
	 * K�nige auf der Hand hat entweder Schmei�en oder ein Solo ansagen, sodass
	 * er faire Chancen bekommt.
	 */
}
