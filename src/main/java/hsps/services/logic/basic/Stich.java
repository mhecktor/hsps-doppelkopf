package hsps.services.logic.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.player.Spieler;

public class Stich {

	private int punktezahl = 0;

	public void setPunktezahl(int punktezahl) {
		this.punktezahl = punktezahl;
	}

	public Karte[] getKarten() {
		return karten;
	}

	public void setKarten(Karte[] karten) {
		this.karten = karten;
	}

	public int getKartenAnzahl() {
		return kartenAnzahl;
	}

	public void setKartenAnzahl(int kartenAnzahl) {
		this.kartenAnzahl = kartenAnzahl;
	}

	private Karte[] karten = new Karte[ 4 ];
	private int kartenAnzahl = 0;
	@JsonIgnore
	private Spieler spieler;

	@JsonProperty("stichBesitzer")
	public String getStichBesitzer() {
		return spieler.getName();
	}

	private Karte hoechsteKarte;

	// Setzen des ersten Spielers mit seiner gelegten Karte
	public Stich( Spieler spieler, Karte karte ) {
		setSpieler( spieler );
		addKarte( karte, spieler.getName());
		hoechsteKarte = karte;
	}

	// Setzen des Spieler, wem der Stich gehoert
	public void setSpieler( Spieler spieler ) {
		this.spieler = spieler;
	}

	// Rueckgabe des Spielers, wem der Stich gehoert
	public Spieler getSpieler() {
		return spieler;
	}

	// Hinzufuegen einer Karte und direktes Erhoehen der gesamten Punktezahl des
	// Stichs
	public void addKarte( Karte karte, String playedBy) {
		karten[ kartenAnzahl++ ] = karte;
		karte.setPlayedByPlayer(playedBy);
		punktezahl += karte.getWertigkeit();
	}

	public Karte getErsteKarte() {
		return karten[ 0 ];
	}

	public void setHoechsteKarte( Karte hoechsteKarte ) {
		this.hoechsteKarte = hoechsteKarte;
	}

	public Karte getHoechsteKarte() {
		return hoechsteKarte;
	}

	// Rueckgabe der Punktezahl des Stichs
	public int getPunktezahl() {
		return punktezahl;
	}
}
