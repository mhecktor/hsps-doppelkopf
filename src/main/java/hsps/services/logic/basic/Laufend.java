package hsps.services.logic.basic;

public class Laufend extends Zustand {

	public Laufend( Spiel spiel ) {
		super( spiel );
		System.out.println( spiel.aktZustand );
	}

	@Override
	public void initialisieren() {
		if( Spiel.DEBUG )
			System.out.println( "Spiel kann nicht initialisiert werden!" );
	}

	public void wiederaufnehmen() {
		if( Spiel.DEBUG )
			System.out.println( "Spiel laeuft bereits!" );
	}

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
