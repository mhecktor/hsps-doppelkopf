package hsps.services.test;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import hsps.services.logic.player.Spieler;
import hsps.services.mqtt.Message;

public class AbstractMqttCallback implements MqttCallback {

	private Subscriber subscriber;

	private int choosenCardIndex = 0;

	public static int counter = 0;
	public static final int test = (int) ( Math.random() * 200 );

	public AbstractMqttCallback( Subscriber subscriber ) {
		this.subscriber = subscriber;
	}

	public void connectionLost( Throwable throwable ) {
		System.out.println( "Connection to MQTT broker lost!" );
		throwable.printStackTrace();
	}

	public void messageArrived( String s, MqttMessage mqttMessage ) throws Exception {
		Spieler spieler = subscriber.getSpieler();
		ObjectMapper objMapper = new ObjectMapper();
		Message m = objMapper.readValue( mqttMessage.getPayload(), Message.class );

		counter++;
		if( counter == test ) {
			System.err.println( "==> PAUSIERE SPIEL <==" );
			spieler.pauseGame();
			Thread.sleep( 2000 );
			spieler.resumeGame();
		}

		switch( m.getType() ) {
			case Announcement:
				break;
			case Armut:
				System.err.println( m.getType() );
				break;
			case AskKoenigSolo:
				System.err.println( m.getType() );
				spieler.performDecisionRule( false );
				break;
			case AskReContraAnnouncement:
				break;
			case AskSchmeissen:
				System.err.println( m.getType() );
				spieler.performDecisionRule( true );
				break;
			case ChooseCard:
				spieler.setChosenCard( spieler.getHand().getKarten().get( choosenCardIndex ) );
				spieler.performTurn();
				break;
			case EndedGame:
				TestProgramm.writeKarten( spieler );
				break;
			case GameRunning:
				System.err.println( m.getType() );

				break;
			case GetCard:
				break;
			case InitGame:
				break;
			case InvalidCard:
				choosenCardIndex++;
				break;
			case KoenigSolo:
				System.err.println( m.getType() );
				break;
			case PauseGame:
				System.err.println( m.getType() );
				break;
			case PlayerGotStich:
				break;
			case PlayerTopic:
				break;
			case RestartGame:
				System.err.println( m.getType() );

				break;
			case Schmeissen:
				System.err.println( m.getType() );
				break;
			case Schweinchen:
				System.err.println( m.getType() );
				break;
			case ValidCard:
				choosenCardIndex = 0;
				break;
			default:
				break;

		}
	}

	public void deliveryComplete( IMqttDeliveryToken iMqttDeliveryToken ) {}
}
