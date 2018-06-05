package hsps.services.logic.rules.basic;

import hsps.services.logic.basic.Spiel;

public interface Rule {
    boolean test(Spiel spiel);

    void perform();
}
