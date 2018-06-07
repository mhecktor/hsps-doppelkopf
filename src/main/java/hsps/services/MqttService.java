package hsps.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import hsps.services.mqtt.server.Publisher;

@Service
public class MqttService {
	private Publisher publisher;

	@PostConstruct
	public void init() throws MqttException {
		publisher = new Publisher();
		publisher.connect();
	}

	@PreDestroy
	public void destroy() throws MqttException {
		publisher.disconnect();
	}
}
