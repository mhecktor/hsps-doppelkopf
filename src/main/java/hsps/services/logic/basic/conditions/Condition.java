package hsps.services.logic.basic.conditions;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.abstr.AbstractGameState;

public abstract class Condition {

	private AbstractGameState gameState;
	protected Spiel spiel;

	public Condition( AbstractGameState gameState, Spiel spiel ) {
		this.gameState = gameState;
		this.spiel = spiel;
	}

	public abstract boolean testCondition();

	public AbstractGameState getGameState() {
		return gameState;
	}
}
