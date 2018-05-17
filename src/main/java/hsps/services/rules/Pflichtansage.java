package hsps.services.rules;

public class Pflichtansage implements Rule {

	/*
	 * Pflichtansage bei 30 Punkten im ersten Stich - der erste Stich ist sehr
	 * entscheidend fuer den Spielverlauf. Wenn man in diesem mehr als 30 Punkte
	 * holt, muss Kontra/Re gesagt werden, damit die Spieler wissen, wer deren
	 * Partner ist. Somit soll etwas Bedeutung aus dem ersten Stich genommen
	 * werden und das Teamspiel im Mittelpunkt stehen.
	 */

	@Override
	public boolean test() {
		return false;
	}

}
