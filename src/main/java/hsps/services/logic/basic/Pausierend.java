package hsps.services.logic.basic;

import hsps.services.MqttService;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;

public class Pausierend extends Zustand {

	public Pausierend( Spiel spiel ) {
		super( spiel );

		MqttService.publisher.publishData( new Message( MessageType.PauseGame ) );
	}

	@Override
	public void initialisieren() {
		if( Spiel.DEBUG ) System.out.println( "Spiel kann nicht initialisiert werden!" );
	}

	public void wiederaufnehmen() {
		if( Spiel.DEBUG ) System.out.println( "Spiel wird wieder aufgenommen..." );
		spiel.setAktuellerZustand( new Laufend( spiel ) );
	}

	public void pausieren() {
		if( Spiel.DEBUG ) System.out.println( "Spiel pausiert bereits!" );
	}

	@Override
	public void beenden() {
		if( Spiel.DEBUG ) System.out.println( "Spiel wird beendet..." );
		spiel.setAktuellerZustand( new Beendend( spiel ) );
	}

	@Override
	public State getState() {
		return State.PAUSIEREND;
	}

	@Override
	public String toString() {
		return getState().toString();
	}
}
