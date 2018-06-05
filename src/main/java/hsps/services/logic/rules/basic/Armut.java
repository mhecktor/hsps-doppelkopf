package hsps.services.logic.rules.basic;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.player.Spieler;

public class Armut implements Rule {
    /*
	 * Armut - da Truempfe in dem Spiel sehr wichtig sind, bestimmt das Glueck
	 * darueber, wie gut die Chancen auf den Sieg stehen. Um dieses zu
	 * verhindern, wird neu gegeben, sobald ein Spieler 3 oder weniger Truempfe
	 * auf der Hand hat.
	 */

    private int anzahlTruempfe = 0;
    private Spiel spiel;

    @Override
    public boolean test(Spiel spiel) {
        this.spiel = spiel;
        for (Spieler s : spiel.getSpielerliste()) {
            for (Karte k : s.getHand().getKarten()) {
                if (k.isTrumpf()) {
                    anzahlTruempfe++;
                }
            }
        }
        if (anzahlTruempfe <= 3) {
            return true;
        } else
            return false;

    }

    @Override
    public void perform() {
        spiel.neustarten();
    }
}
