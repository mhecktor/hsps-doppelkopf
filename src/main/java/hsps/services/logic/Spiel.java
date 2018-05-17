package hsps.services.logic;

import java.util.ArrayList;
import java.util.List;

public class Spiel {

	// spielID ist entsprechend dem Namen
	private String spielID;

	private List<Karte> kartenSpiel;

	private int rundenAnzahl;
	private Spieler[] spielerListe = new Spieler[ 4 ];
	private int anzahlSpieler = 0;

	public Spiel() {
		kartenSpiel = new ArrayList<Karte>();
		for( Symbolik s : Symbolik.values() )
			for( Farbwert f : Farbwert.values() )
				kartenSpiel.add( new Karte( f, s ) );
	}

	public void addSpieler( Spieler spieler ) {
		if( anzahlSpieler < spielerListe.length ) spielerListe[ anzahlSpieler++ ] = spieler;
	}

	/*
	 * Verteilung der Karten aus dem Kartenspiel an die Spieler und das Spiel
	 * starten
	 */
	public void starten() {

		// Zufallsmaessige Verteilung der Karten an die Spieler
		int kartenAnzahl = kartenSpiel.size();
		int spielerNr = 0;
		while( kartenAnzahl > 0 ) {
			spielerListe[ spielerNr ].getHand().addKarte( kartenSpiel.remove( (int) ( Math.random() * kartenAnzahl-- ) ) );
			spielerNr = ( spielerNr + 1 ) % spielerListe.length;
		}

	}

	public void spielzugAusfuehren() {

	}

	public void beenden() {

	}

}
