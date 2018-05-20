package hsps.services.logic.basic;

import hsps.services.logic.cards.Zustand;

public class Pausierend extends Zustand {

	public Pausierend( Spiel spiel ) {
		super( spiel );
	}

	public void wiederaufnahme() {
		spiel.setAktuellerZustand( new Laufend( spiel ) );
	}

	public void pausieren() {
		System.out.println( "Spiel pausiert bereits!!" );
	}
}
