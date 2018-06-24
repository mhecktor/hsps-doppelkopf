package hsps.services.logic.basic.conditions;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.abstr.AbstractGameState;

public class PostAnsagenCondition extends Condition {

	public PostAnsagenCondition( AbstractGameState gameState, Spiel spiel ) {
		super( gameState, spiel );
	}

	@Override
	public boolean testCondition() {
		return true;
	}

}
