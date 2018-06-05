package hsps.services.logic.rules.stich;

import hsps.services.logic.basic.Stich;
import hsps.services.logic.cards.Karte;

public interface StichRule {

	public boolean changeBelonging(Stich stich, Karte karte);
}
