package hsps.services.mqtt.server;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import hsps.services.mqtt.Topic;

public class Publisher {
	
	private MqttClient client;

	public Publisher() throws MqttException {
		this( "tcp://localhost:1883" );
	}

	public Publisher( String serverURI ) throws MqttException {
		client = new MqttClient( serverURI, MqttClient.generateClientId() );
	}

	public void connect() throws MqttSecurityException, MqttException {
		client.connect();
	}

	public void publishData( byte[] data ) throws MqttPersistenceException, MqttException {
		publishData( data, Topic.DEFAULT_TOPIC );
	}

	public void publishData( byte[] data, String topic ) throws MqttPersistenceException, MqttException {
		MqttMessage message = new MqttMessage();
		message.setPayload( data );
		client.publish( topic, message );
	}

	public void disconnect() throws MqttException {
		client.disconnect();
	}
}
