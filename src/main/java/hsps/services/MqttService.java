package hsps.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import hsps.services.logic.basic.Spiel;
import hsps.services.mqtt.Publisher;

@Service
public class MqttService {
	public static Publisher publisher;

	@PostConstruct
	public void init() throws MqttException {
		if( Spiel.DEBUG ) System.out.println( "Init MqttService" );
		publisher = new Publisher();
		publisher.connect();
	}

	@PreDestroy
	public void destroy() throws MqttException {
		if( Spiel.DEBUG ) System.out.println( "Destroy MqttService" );
		publisher.disconnect();
	}
}
