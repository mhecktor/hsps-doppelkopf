package hsps.services.logic.basic.conditions;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.abstr.AbstractGameState;

public class AnsagenCondition extends Condition {

	public AnsagenCondition( AbstractGameState gameState, Spiel spiel ) {
		super( gameState, spiel );
	}

	@Override
	public boolean testCondition() {
		return spiel.decisionAnnouncementIndex >= spiel.decisionAnnouncements.size();
	}

}
