package hsps.services.logic.basic.abstr;

import hsps.services.exception.NotYourTurnException;

public abstract class AbstractSpieler {
	
	public abstract void pauseGame();
	
	public abstract void resumeGame();
	
	public abstract void performTurn() throws NotYourTurnException;

	public abstract void notifyPerformTurn();
}
