package hsps.services.logic.basic;

import hsps.services.logic.cards.Zustand;

public class Laufend extends Zustand {

    public Laufend(Spiel spiel) {
        super(spiel);
    }

    public void wiederaufnahme() {
        System.out.println("Spiel laeuft bereits!!");
    }

    public void pausieren() {
        spiel.setAktuellerZustand(new Pausierend(spiel));
    }
}
