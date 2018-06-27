package hsps.services.test;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import hsps.services.logic.player.Spieler;
import hsps.services.mqtt.Topic;

/* Subscriber ist der "Client" */
public class Subscriber {

	private Spieler spieler;
	private MqttClient client;

	public Subscriber() {}

	public void init() throws MqttException {
		System.out.println( "== START SUBSCRIBER ==" );

		client = new MqttClient( "tcp://localhost:1883", MqttClient.generateClientId() );
		client.setCallback( new AbstractMqttCallback( this ) );
		client.connect();

		client.subscribe( Topic.GENERELL );
	}

	public void disconnect() throws MqttException {
		client.disconnect();
	}
	
	public Spieler getSpieler() {
		return spieler;
	}

	public void setSpieler( Spieler spieler ) throws MqttException {
		this.spieler = spieler;
		client.subscribe( Topic.genPlayerTopic( "Testspiel", spieler.getName()) );
	}

}
