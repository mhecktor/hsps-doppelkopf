package hsps.services.logic.basic.conditions;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.abstr.AbstractGameState;

public class StartGameCondition extends Condition {

	public StartGameCondition( AbstractGameState gameState, Spiel spiel ) {
		super( gameState, spiel );
	}

	@Override
	public boolean testCondition() {
		return spiel.getAnzahlSpieler() >= spiel.getSpielerListe().length;
	}

}
