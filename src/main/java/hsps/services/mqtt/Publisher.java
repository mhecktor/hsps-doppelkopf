package hsps.services.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/* Publisher ist der "Server" */
public class Publisher {

	private MqttClient client;

	public Publisher() {
		this( "tcp://localhost:1883" );
		// this( "tcp://192.168.0.17:1883" );
	}

	public Publisher( String serverURI ) {
		try {
			client = new MqttClient( serverURI, MqttClient.generateClientId() );
		} catch( MqttException e ) {
			e.printStackTrace();
		}
	}

	public void connect() {
		try {
			client.connect();
		} catch( MqttSecurityException e ) {
			e.printStackTrace();
		} catch( MqttException e ) {
			e.printStackTrace();
		}
	}

	public synchronized void publishData( byte[] data ) {
		publishData( data, Topic.GENERELL );
	}

	public synchronized void publishData( Message arg ) {
		publishData( arg, Topic.GENERELL );
	}

	public synchronized void publishData( Message arg, String topic ) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			publishData( objMapper.writeValueAsBytes( arg ), topic );
		} catch( JsonProcessingException e ) {
			e.printStackTrace();
		}
	}

	public synchronized void publishData( byte[] data, String topic ) {
		MqttMessage message = new MqttMessage();
		try {
			message.setPayload( data );
			client.publish( topic, message );
		} catch( MqttPersistenceException e ) {
			e.printStackTrace();
		} catch( MqttException e ) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			client.disconnect();
		} catch( MqttException e ) {
			e.printStackTrace();
		}
	}
}
