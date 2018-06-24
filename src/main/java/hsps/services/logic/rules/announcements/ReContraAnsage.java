package hsps.services.logic.rules.announcements;

import hsps.services.MqttService;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.player.Spieler;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Topic;

public class ReContraAnsage extends DecisionAnnouncement {

	private Spiel spiel;
	private Spieler spieler;

	@Override
	public boolean test( Spiel spiel ) {
		this.spiel = spiel;
		this.spieler = spiel.getCurrentSpieler();
		if( spiel.getCurrentRoundIndex() == 0 ) { return true; }

		return false;
	}

	@Override
	public void inform() {
		MqttService.publisher.publishData( new Message( MessageType.AskReContraAnnouncement, "Re oder Contra ansagen?" ), Topic.genPlayerTopic( spiel.getSpielID(), spiel.getSpielerNr( spieler ) ) );

	}

	@Override
	protected String announce() {
		String retVal = spieler.getName() + " ist ";
		return spieler.isRe() ? retVal + "Re" : retVal + "Contra";
	}

}
