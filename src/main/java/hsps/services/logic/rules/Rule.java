package hsps.services.logic.rules;

import hsps.services.logic.basic.Spiel;

public interface Rule {
	boolean test(Spiel spiel);

	void perform();
}
