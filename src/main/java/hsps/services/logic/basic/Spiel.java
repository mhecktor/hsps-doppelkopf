package hsps.services.logic.basic;

import java.util.ArrayList;
import java.util.List;

import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Zustand;
import hsps.services.logic.player.Spieler;
import hsps.services.logic.rules.basic.Armut;
import hsps.services.logic.rules.basic.KoenigSolo;
import hsps.services.logic.rules.basic.Rule;
import hsps.services.logic.rules.basic.Schmeissen;
import hsps.services.logic.rules.basic.Schweinchen;
import hsps.services.logic.rules.stich.StichRule;
import hsps.services.logic.rules.stich.StichRuleNormal;
import hsps.services.logic.rules.stich.StichRuleZweiteDulle;

public class Spiel extends Subject {

	// spielID ist entsprechend dem Namen
	private String spielID;

	private List<Karte> kartenSpiel;

	// Hier wird abgelegt, nach welchen Regeln die Stiche ausgewertet werden
	// sollen
	// Die Reihenfolge der Regeln ist wichtig!!! Die speziellen Regeln stehen
	// ganz vorne
	private List<StichRule> stichRules;

	// Hier sind die Regeln enthalten, nach den das Spiel gespielt wird
	private List<Rule> rules;

	private int rundenAnzahl;
	private Spieler[] spielerListe = new Spieler[ 4 ];
	private int anzahlSpieler = 0;
	private int maxRundenZahl;

	private Zustand aktZustand;

	// Enthaelt den aktuellen Stich der Runde
	private Stich stich;

	public Spiel() {
		aktZustand = new Laufend( this );
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

		// Initialisierungen um das Spiel spaeter leichter neustarten zu koennen
		initKartenspiel();
		initSpieler();

		// Zufallsmaessige Verteilung der Karten an die Spieler
		int kartenAnzahl = kartenSpiel.size();
		int spielerNr = 0;
		while( kartenAnzahl > 0 ) {
			spielerListe[ spielerNr ].getHand().addKarte( kartenSpiel.remove( (int) ( Math.random() * kartenAnzahl-- ) ) );
			spielerNr = ( spielerNr + 1 ) % spielerListe.length;
		}

		// TODO Die Regeln sollten am besten vorher ausgesucht werden koennen
		// und anhand der Pruefungen werden dann die Regeln fuer den Stich
		// hinzugefuegt
		initRules();

		run();
	}

	private void initKartenspiel() {
		kartenSpiel = new ArrayList<Karte>();
		for( int i = 0; i < 2; i++ )
			for( Symbolik s : Symbolik.values() )
				for( Farbwert f : Farbwert.values() )
					kartenSpiel.add( new Karte( f, s ) );
		maxRundenZahl = kartenSpiel.size() / spielerListe.length;
	}

	private void initSpieler() {
		// Die Karten muessen fuer jeden Spieler geleert werden
		// Ist fuer einen Neustart leichter
		for( Spieler s : spielerListe )
			s.getHand().resetKarten();
	}

	private void initRules() {
		rules = new ArrayList<Rule>();
		stichRules = new ArrayList<StichRule>();
		// Es wird immer nach den normalen Regeln gespielt
		// Die speziellste Regel steht ganz vorne
		stichRules.add( 0, new StichRuleNormal() );
		stichRules.add( 0, new StichRuleZweiteDulle() );

		rules.add( new Armut() );
		rules.add( new Schmeissen() );
		rules.add( new KoenigSolo() );
		// rules.add( new Pflichtansage() );
		rules.add( new Schweinchen() );

		// TODO spaeter eventuell ein dynamisches Hinzufuegen der Regeln vor
		// Beginn
		// des Spiel

		for( Rule r : rules ) {
			if( r.test( this ) ) {
				r.perform();
			}
		}

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

			stich = null;

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
				for( StichRule sr : stichRules ) {
					if( sr.changeBelonging( stich, karte ) ) {
						aendereZugehoerigkeit = true;
						break;
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
		System.out.println( "Spiel beendet" );
	}

	public Spieler[] getSpielerliste() {
		return spielerListe;
	}

	public List<StichRule> getStichRules() {
		return stichRules;
	}

	public void setAktuellerZustand( Zustand zustand ) {
		this.aktZustand = zustand;
	}

	// TODO Hier muessen noch Mechanismen eingebaut werden um ein Spiel neu
	// starten zu koennen
	public void neustarten() {
		starten();
	}

	public void pausieren() {
		aktZustand.pausieren();
	}

	public void wiederaufnahme() {
		aktZustand.wiederaufnahme();
	}

}
