package hsps.services.rules;

public class ZweiteDulle implements Rule {

	/*
	 * Zweite Dulle sticht erste - die Garantie einen Stich zu bekommen, laesst
	 * wenig Spielraum fuer Taktik, weswegen wir uns dazu entschieden haben,
	 * diese Regel hinzuzufuegen. Jedoch gilt dies nicht fuer den letzten Stich.
	 */

	@Override
	public boolean test() {
		return false;
	}

}
