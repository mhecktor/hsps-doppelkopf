package hsps.services.logic.rules.stich;

import hsps.services.logic.basic.Stich;
import hsps.services.logic.basic.Symbolik;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;

public class StichRuleZweiteDulle implements StichRule {

	/*
	 * Zweite Dulle sticht erste - die Garantie einen Stich zu bekommen, laesst
	 * wenig Spielraum fuer Taktik, weswegen wir uns dazu entschieden haben,
	 * diese Regel hinzuzufuegen. Jedoch gilt dies nicht fuer den letzten Stich.
	 */

	@Override
	public boolean changeBelonging( Stich stich, Karte karte ) {
		if( karte.getFarbwert() == Farbwert.HERZ && karte.getSymbolik() == Symbolik.ZEHN ) {
			if( stich.getHoechsteKarte().getFarbwert() == Farbwert.HERZ && stich.getHoechsteKarte().getSymbolik() == Symbolik.ZEHN ) {
				// Die hoechste Karte war bereits die Dulle und daher wird diese
				// gestochen
				return true;
			}
		}

		return false;
	}

}
