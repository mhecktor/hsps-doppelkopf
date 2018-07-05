package hsps.services.logic.rules.announcements;

import hsps.services.MqttService;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Topic;

public class KeineNeunzig extends DecisionAnnouncement {

	@Override
	public boolean testCondition() {
		if( called ) return false;
		if( spieler.isRe() && spiel.reCalled && spiel.getCurrentRoundIndex() <= 1 ) return true;
		if( !spieler.isRe() && spiel.contraCalled && spiel.getCurrentRoundIndex() <= 1 ) return true;
		return false;
	}

	@Override
	public void inform() {
		String ansage = "Keine 9";
		MqttService.publisher.publishData( new Message( MessageType.AskReContraAnnouncement, ansage ), Topic.genPlayerTopic( spiel.getSpielID(), spieler.getName() ) );
	}

	@Override
	protected String announce() {
		called = true;
		if( spiel.contraCalled )
			return "Contra sagt keine 9";
		else
			return "Re sagt keine 9";
	}

}
