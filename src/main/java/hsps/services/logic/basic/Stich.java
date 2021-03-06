package hsps.services.logic.basic;

import hsps.services.logic.cards.Karte;
import hsps.services.logic.player.Spieler;

public class Stich {

    private int punktezahl;
    private Karte[] karten = new Karte[4];
    private int kartenAnzahl = 0;
    private Spieler spieler;
    private Karte hoechsteKarte;

    // Setzen des ersten Spielers mit seiner gelegten Karte
    public Stich(Spieler spieler, Karte karte) {
        setSpieler(spieler);
        addKarte(karte);
        hoechsteKarte = karte;
    }

    // Rueckgabe des Spielers, wem der Stich gehoert
    public Spieler getSpieler() {
        return spieler;
    }

    // Setzen des Spieler, wem der Stich gehoert
    public void setSpieler(Spieler spieler) {
        this.spieler = spieler;
    }

    // Hinzufuegen einer Karte und direktes Erhoehen der gesamten Punktezahl des
    // Stichs
    public void addKarte(Karte karte) {
        karten[kartenAnzahl++] = karte;
        punktezahl += karte.getWertigkeit();
    }

    public Karte getErsteKarte() {
        return karten[0];
    }

    public Karte getHoechsteKarte() {
        return hoechsteKarte;
    }

    public void setHoechsteKarte(Karte hoechsteKarte) {
        this.hoechsteKarte = hoechsteKarte;
    }

    // Rueckgabe der Punktezahl des Stichs
    public int getPunktezahl() {
        return punktezahl;
    }
}
