package hsps.services.test;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.player.Spieler;

public class TestProgramm {

    public static void main(String[] args) {
        // TODO in REST endpunkte umwandeln
        Spiel spiel = new Spiel("Testspiel");

        Spieler klaus = new Spieler(spiel, "Klaus");
        Spieler peter = new Spieler(spiel, "Peter");
        Spieler dieter = new Spieler(spiel, "Dieter");
        Spieler karl = new Spieler(spiel, "Karl");

        spiel.addSpieler(klaus);
        spiel.addSpieler(peter);
        spiel.addSpieler(dieter);
        spiel.addSpieler(karl);

        spiel.starten();
    }
}
