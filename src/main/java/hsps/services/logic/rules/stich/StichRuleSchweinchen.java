package hsps.services.logic.rules.stich;

import hsps.services.logic.basic.Stich;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;

public class StichRuleSchweinchen implements StichRule {

	@Override
	public boolean changeBelonging( Stich stich, Karte karte ) {
		if( karte.getFarbwert() == Farbwert.KARO && karte.getSymbolik() == Symbolik.ASS ) {
			// Da nur eine Person beide Karo-Asse haben kann, darf hier direkt true zurueckgegeben werden
			return true;
		}
		return false;
	}
}
