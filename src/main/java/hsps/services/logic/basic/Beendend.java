package hsps.services.logic.basic;

import hsps.services.MqttService;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;

public class Beendend extends Zustand {

	public Beendend( Spiel spiel ) {
		super( spiel );
		MqttService.publisher.publishData( new Message( MessageType.EndedGame ) );
	}

	@Override
	public void initialisieren() {
		if( Spiel.SYSTEM ) System.out.println( "Spiel wird initialisiert..." );
		spiel.setAktuellerZustand( new Initialisierend( spiel ) );
	}

	@Override
	public void pausieren() {
		if( Spiel.SYSTEM ) System.out.println( "Spiel bereits beendet!" );
	}

	@Override
	public void wiederaufnehmen() {
		if( Spiel.SYSTEM ) System.out.println( "Spiel bereits beendet!" );
	}

	@Override
	public void beenden() {
		if( Spiel.SYSTEM ) System.out.println( "Spiel bereits beendet!" );
	}

	@Override
	public State getState() {
		return State.BEENDEND;
	}

	@Override
	public String toString() {
		return getState().toString();
	}
}
