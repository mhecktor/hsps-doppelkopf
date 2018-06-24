package hsps.services.logic.rules.announcements;

import hsps.services.MqttService;
import hsps.services.logic.rules.Rule;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;

public abstract class Announcement implements Rule {

	@Override
	public void perform() {
		MqttService.publisher.publishData( new Message( MessageType.Announcement, announce() ) );
	}
	
	protected abstract String announce();

}
