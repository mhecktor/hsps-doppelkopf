package hsps.services.logic.basic;

import hsps.services.MqttService;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;

public class Laufend extends Zustand {

	public Laufend( Spiel spiel ) {
		super( spiel );
		
		MqttService.publisher.publishData( new Message( MessageType.ContinueGame) );
	}

	@Override
	public void initialisieren() {
		if( Spiel.DEBUG )
			System.out.println( "Spiel kann nicht initialisiert werden!" );
	}

	@Override
	public void wiederaufnehmen() {
		if( Spiel.DEBUG )
			System.out.println( "Spiel laeuft bereits!" );
	}

	@Override
	public void pausieren() {
		if( Spiel.DEBUG )
			System.out.println( "Spiel wird angehalten..." );
		spiel.setAktuellerZustand( new Pausierend( spiel ) );
	}

	@Override
	public void beenden() {
		if( Spiel.DEBUG )
			System.out.println( "Spiel wird beendet..." );
		spiel.setAktuellerZustand( new Pausierend( spiel ) );
	}

	@Override
	public State getState() {
		return State.LAUFEND;
	}

	@Override
	public String toString() {
		return getState().toString();
	}
}
