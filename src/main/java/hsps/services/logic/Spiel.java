package hsps.services.logic;

import java.util.ArrayList;
import java.util.List;

import basic.Subject;
import rules.NormalRule;
import rules.Rule;

public class Spiel extends Subject {

	// spielID ist entsprechend dem Namen
	private String spielID;

	private List<Karte> kartenSpiel;
	
	// TODO !!! Die Reihenfolge der Regeln ist wichtig
	private List<Rule> rules;

	private int rundenAnzahl;
	private Spieler[] spielerListe = new Spieler[ 4 ];
	private int anzahlSpieler = 0;
	private int maxRundenZahl;

	// Enthaelt den aktuellen Stich der Runde
	private Stich stich;

	public Spiel() {
		kartenSpiel = new ArrayList<Karte>();
		rules = new ArrayList<Rule>();
		for( int i = 0; i < 2; i++ )
			for( Symbolik s : Symbolik.values() )
				for( Farbwert f : Farbwert.values() )
					kartenSpiel.add( new Karte( f, s ) );
		maxRundenZahl = kartenSpiel.size() / spielerListe.length;
		
		rules.add( new NormalRule() );
	}

	public void addSpieler( Spieler spieler ) {
		if( anzahlSpieler < spielerListe.length ) spielerListe[ anzahlSpieler++ ] = spieler;
	}

	private boolean checkSpielerAnzahl() {
		return anzahlSpieler == spielerListe.length;
	}

	/*
	 * Verteilung der Karten aus dem Kartenspiel an die Spieler und starten des
	 * Spiels
	 */
	public void starten() {
		if( !checkSpielerAnzahl() ) return;

		// Zufallsmaessige Verteilung der Karten an die Spieler
		int kartenAnzahl = kartenSpiel.size();

		int spielerNr = 0;
		while( kartenAnzahl > 0 ) {
			spielerListe[ spielerNr ].getHand().addKarte( kartenSpiel.remove( (int) ( Math.random() * kartenAnzahl-- ) ) );
			spielerNr = ( spielerNr + 1 ) % spielerListe.length;
		}

		run();
	}

	/*
	 * Eigentlicher Spielablauf
	 * Solange wie Runden spielbar sind, sollen die Spieler einen Zug vollf�hren
	 */
	public void run() {
		int startPlayer = 0;
		while( rundenAnzahl < maxRundenZahl ) {
			for( int a = 0; a < spielerListe.length; a++ ) {
				notifyObserver( spielerListe[ ( startPlayer + a ) % spielerListe.length ] );
			}

			// TODO warten bis alle Spieler ihren Zug gemacht haben
			Spieler tSpieler = stich.getSpieler();
			tSpieler.addStich( stich );

			startPlayer++;
			rundenAnzahl++;
		}
	}

	// Wird vom Spieler aufgerufen mit der ausgewaehlten Karte
	public void spielzugAusfuehren( Spieler spieler, Karte karte ) {
		if( stich == null ) {
			stich = new Stich( spieler, karte );
		} else {
			// Wenn die Karte gueltig war, dann entferne die Karte aus der Hand
			// von dem Spieler und pruefe, wem der Stich nun gehoert
			// Falls nicht gueltig, informiere den Spieler erneut, dass er eine
			// Karte aussuchen solls
			if( pruefeGueltigkeit( spieler, karte ) ) {
				spieler.getHand().removeKarte( karte );								
				stich.addKarte( karte );

				// Die Pruefung, wem der Stich gehoert kann eventuell wieder mit
				// einem Muster vollzogen werden (siehe Notizen)
				boolean aendereZugehoerigkeit = false;
				for( Rule r : rules ) {
					if( r.test( stich, karte ) ) {
						aendereZugehoerigkeit = true;
					} 
				}

				if( aendereZugehoerigkeit ) {
					stich.setHoechsteKarte( karte );
					stich.setSpieler( spieler );
				}
				System.out.println();
				System.out.println( "--> Spieler " + stich.getSpieler() + " gehört dem Stich" );
				System.out.println();
			} else {
				System.out.println();
				System.out.println( "--> Ausgewaehlte Karte war nicht gueltig!!" );
				System.out.println();
				notifyObserver( spieler );
			}
		}
	}

	// Methode von Schulte
	private boolean pruefeGueltigkeit( Spieler spieler, Karte karte ) {
		if( stich.getErsteKarte().isTrumpf() ) {
			if( karte.isTrumpf() ) {
				return true;
			} else {
				for( int i = 0; i < spieler.getHand().getKarten().size(); i++ ) {
					if( spieler.getHand().getKarten().get( i ).isTrumpf() == true ) { return false; }
				}
				return true;
			}
		} else {
			if( karte.isTrumpf() ) {
				for( int i = 0; i < spieler.getHand().getKarten().size(); i++ ) {
					if( ( spieler.getHand().getKarten().get( i ).getFarbwert() == stich.getErsteKarte().getFarbwert() ) && ( !spieler.getHand().getKarten().get( i ).isTrumpf() ) ) { return false; }
				}
				return true;

			} else {
				if( karte.getFarbwert() == stich.getErsteKarte().getFarbwert() ) {
					return true;
				} else {
					for( int i = 0; i < spieler.getHand().getKarten().size(); i++ ) {
						if( spieler.getHand().getKarten().get( i ).getFarbwert() == stich.getErsteKarte().getFarbwert() ) { return false; }
					}
					return true;
				}
			}
		}

	}

	public void beenden() {

	}

}
