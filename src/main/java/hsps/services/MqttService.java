package hsps.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import hsps.services.logic.basic.Spiel;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Publisher;
import hsps.services.mqtt.Topic;

@Service
public class MqttService {
	public static Publisher publisher;

	@PostConstruct
	public void init() throws MqttException {
		if( Spiel.DEBUG ) System.err.println( "Init MqttService" );
		publisher = new Publisher();
		publisher.connect();
		Message m = new Message( MessageType.GetCard, null );
		publisher.publishData( m, Topic.GENERELL );
	}

	@PreDestroy
	public void destroy() throws MqttException {
		if( Spiel.DEBUG ) System.err.println( "Destroy MqttService" );
		publisher.disconnect();
	}
}
