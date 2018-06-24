package hsps.services.logic.basic.conditions;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.abstr.AbstractGameState;

public class EndingCondition extends Condition {

	public EndingCondition( AbstractGameState gameState, Spiel spiel ) {
		super( gameState, spiel );
	}

	@Override
	public boolean testCondition() {
		return spiel.getCurrentRoundIndex() >= spiel.getMaxRundenZahl();
	}

}
