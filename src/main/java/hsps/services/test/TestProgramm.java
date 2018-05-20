package hsps.services.test;

import logic.basic.Spiel;
import logic.player.Spieler;

public class TestProgramm {

	public static void main( String[] args ) {
		Spiel spiel = new Spiel();

		Spieler klaus = new Spieler( spiel, "Klaus" );
		Spieler peter = new Spieler( spiel, "Peter" );
		Spieler dieter = new Spieler( spiel, "Dieter" );
		Spieler karl = new Spieler( spiel, "Karl" );

		spiel.addSpieler( klaus );
		spiel.addSpieler( peter );
		spiel.addSpieler( dieter );
		spiel.addSpieler( karl );

		spiel.starten();
	}
}
