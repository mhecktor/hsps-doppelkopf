package hsps.services.logic.test;

import hsps.services.exception.AddSpielerException;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.player.Spieler;

public class TestProgram {

	public static void main( String[] args ) {
		Spiel spiel = new Spiel( "Testspiel" );

		Spieler karl = new Spieler( spiel, "Karl" );
		Spieler dieter = new Spieler( spiel, "dieter" );
		Spieler peter = new Spieler( spiel, "Peter" );
		Spieler klaus = new Spieler( spiel, "Klaus" );

		try {
			spiel.addSpieler( karl );
			spiel.addSpieler( dieter );
			spiel.addSpieler( peter );
			spiel.addSpieler( klaus );
		} catch( AddSpielerException e ) {
			e.printStackTrace();
		}

		spiel.starten();

	}

}
