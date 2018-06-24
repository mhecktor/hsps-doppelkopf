package hsps.services.logic.basic.conditions;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.abstr.AbstractGameState;

public class RuleCondition extends Condition {

	public RuleCondition( AbstractGameState gameState, Spiel spiel ) {
		super( gameState, spiel );
	}

	@Override
	public boolean testCondition() {
		return spiel.ruleIndex >= spiel.getRules().size() && spiel.decisionRuleIndex >= spiel.getDecisionRules().size();
	}

}
