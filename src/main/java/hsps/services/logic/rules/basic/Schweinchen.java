package hsps.services.logic.rules.basic;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.Symbolik;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.player.Spieler;
import hsps.services.logic.rules.stich.StichRuleSchweinchen;

import javax.swing.*;

public class Schweinchen implements Rule {

	/*
     * Schweinchen - durch die Fuechse lassen sich fuer die Gegner viele Punkte
	 * holen. Wenn ein Spieler beide Fuechse hat, minimieren sich seine
	 * Gewinnchancen. Durch die Schweinchen-Regel soll dies verhindert werden,
	 * indem die Fuechse zur hoechsten Karte werden, sobald ein Spieler beide
	 * hat.
	 * Dies muss beim Ausspielen angesagt werden.
	 */

    private int anzFuechse = 0;
    private Spiel spiel;

    @Override
    public boolean test(Spiel spiel) {
        this.spiel = spiel;
        // Durch die Hand von jedem Spieler Iterieren und Gucken ob einer 2
        // Karo-Asse Besitzt
        for (Spieler s : spiel.getSpielerliste()) {
            for (Karte k : s.getHand().getKarten()) {
                if (k.getSymbolik() == Symbolik.ASS && k.getFarbwert() == Farbwert.KARO) {
                    anzFuechse++;
                }
            }

            // Sollte jemand zwei Karo-Asse besitzen tritt die Regel in Kraft
            if (anzFuechse == 2) {
                // JOptionPane nur Beispielhaft, muss ueber Messaging Service
                // gemacht werden
                JOptionPane.showMessageDialog(null, "Spieler: " + s + ", besitzt zwei Karo-Ass Karten und wendet die Regel Schweinchen an.");
                return true;
            }

            anzFuechse = 0;
        }
        return false;
    }

    @Override
    public void perform() {
        spiel.getStichRules().add(new StichRuleSchweinchen());
    }

}
