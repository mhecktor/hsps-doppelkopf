package hsps.services.logic.rules.announcements;

import hsps.services.MqttService;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Topic;
public class ReContraAnsage extends DecisionAnnouncement {
	
	@Override
	public boolean testCondition() {
		return spiel.getCurrentRoundIndex() == 0 && !spiel.reCalled && !spiel.contraCalled;
	}

	@Override
	public void inform() {
		String ansage = "Contra";
		if( spieler.isRe() ) ansage = "Re";
		MqttService.publisher.publishData( new Message( MessageType.AskReContraAnnouncement, ansage ), Topic.genPlayerTopic( spiel.getSpielID(), spieler.getName() ) );
	}

	@Override
	protected String announce() {
		called = true;
		if( spieler.isRe()) spiel.reCalled = true;
		else spiel.contraCalled = true;
		String retVal = spieler.getName() + " ist ";
		return spieler.isRe() ? retVal + "Re" : retVal + "Contra";
	}

}
