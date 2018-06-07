package hsps.services.test;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import hsps.services.mqtt.Topic;

/* Subscriber ist der "Client" */
public class Subscriber {

	public static void main( String[] args ) throws MqttException {

		System.out.println( "== START SUBSCRIBER ==" );

		MqttClient client = new MqttClient( "tcp://localhost:1883", MqttClient.generateClientId() );
		client.setCallback( new AbstractMqttCallback() );
		client.connect();

		client.subscribe( Topic.GENERELL );
		client.subscribe( Topic.genPlayerTopic( 1 ) );

	}
}
