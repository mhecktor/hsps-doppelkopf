package hsps.services.logic.basic;

import hsps.services.MqttService;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;

public class Initialisierend extends Zustand {

	public Initialisierend( Spiel spiel ) {
		super( spiel );
		MqttService.publisher.publishData( new Message( MessageType.InitGame ) );
	}

	@Override
	public void initialisieren() {
		if( Spiel.SYSTEM ) System.out.println( "Spiel wird bereits initialisiert..." );
	}

	@Override
	public void pausieren() {
		if( Spiel.SYSTEM ) System.out.println( "Spiel kann nicht waehrend der Initialisierung pausiert werden!" );
	}

	@Override
	public void wiederaufnehmen() {
		if( Spiel.SYSTEM ) System.out.println( "Spiel wird gestartet..." );
		spiel.setAktuellerZustand( new Laufend( spiel ) );
	}

	@Override
	public void beenden() {
		if( Spiel.SYSTEM ) System.out.println( "Spiel wird beendet..." );
		spiel.setAktuellerZustand( new Beendend( spiel ) );
	}

	@Override
	public State getState() {
		return State.INITIALISIEREND;
	}

	@Override
	public String toString() {
		return getState().toString();
	}
}
