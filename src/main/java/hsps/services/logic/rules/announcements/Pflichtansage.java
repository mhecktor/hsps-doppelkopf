package hsps.services.logic.rules.announcements;

import hsps.services.logic.basic.Spiel;

public class Pflichtansage extends Announcement {

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

		if( spiel.getCurrentRoundIndex() == 1 ) {
			if( spiel.getStich().getPunktezahl() >= 15 ) { return true; }
		}
		return false;
	}

	@Override
	protected String announce() {
		return spiel.getStich().getSpieler().getName() + " ist " + spiel.getStich().getSpieler().isRe();
	}
}
