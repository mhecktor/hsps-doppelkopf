package hsps.services.logic;

public class Stich {

	private int punktezahl;
	private Karte[] karten = new Karte[ 4 ];
	private int kartenAnzahl = 0;
	private Spieler spieler;

	public void setSpieler( Spieler spieler ) {
		this.spieler = spieler;
	}

	public void addKarte( Karte karte ) {
		karten[ kartenAnzahl++ ] = karte;
	}

	/* Berechnung der Punktezahl kann direkt hier erfolgen */
	public int getPunktezahl() {
		return punktezahl;
	}
}
