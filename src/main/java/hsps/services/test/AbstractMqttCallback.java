package hsps.services.test;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import hsps.services.logic.cards.Karte;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;

public class AbstractMqttCallback implements MqttCallback {

	public void connectionLost( Throwable throwable ) {
		System.out.println( "Connection to MQTT broker lost!" );
		throwable.printStackTrace();
	}

	public void messageArrived( String s, MqttMessage mqttMessage ) throws Exception {
		ObjectMapper objMapper = new ObjectMapper();
		Message m = objMapper.readValue( mqttMessage.getPayload(), Message.class );
		if( m.getType() == MessageType.GetCard ) {
			System.out.println( "Message received:\t" + objMapper.readValue( m.getBytes(), Karte.class ) );
		}
	}

	public void deliveryComplete( IMqttDeliveryToken iMqttDeliveryToken ) {}
}
