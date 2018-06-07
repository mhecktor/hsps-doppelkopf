package hsps.services.logic.basic;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hsps.services.exception.AddSpielerException;
import hsps.services.exception.NotAValidCardException;
import hsps.services.exception.NotYourTurnException;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.player.Spieler;
import hsps.services.logic.rules.basic.Rule;
import hsps.services.logic.rules.stich.StichRule;

public class Spiel extends Subject {

	public final static boolean DEBUG = true;

	// spielID ist entsprechend dem Namen
	private String spielID;

    @JsonIgnore
	protected List<Karte> kartenSpiel;

	// Hier wird abgelegt, nach welchen Regeln die Stiche ausgewertet werden
	// sollen
	// Die Reihenfolge der Regeln ist wichtig!!! Die speziellen Regeln stehen
	// ganz vorne
    @JsonIgnore
	protected List<StichRule> stichRules;

	// Hier sind die Regeln enthalten, nach den das Spiel gespielt wird
    @JsonIgnore
	protected List<Rule> rules;

	protected int rundenAnzahl;
	@JsonIgnore
	protected Spieler[] spielerListe = new Spieler[ 4 ];
	protected int anzahlSpieler = 0;
	protected int maxRundenZahl;
	protected int startPlayer = 0;

    @JsonIgnore
	public Zustand aktZustand;

	// Enthaelt den aktuellen Stich der Runde
    @JsonIgnore
	protected Stich stich;
	private int round = 0;

	public Spiel( String spielID ) {
		this.spielID = spielID;
		aktZustand = new Initialisierend( this );
	}

	public Spieler getCurrent() {
		return spielerListe[ ( startPlayer + round ) % spielerListe.length ];
	}

	public void addSpieler( Spieler spieler ) throws AddSpielerException {
		if( aktZustand.getState() == State.INITIALISIEREND ) {
			if( anzahlSpieler < spielerListe.length ) {
				spielerListe[ anzahlSpieler++ ] = spieler;
			} else {
				throw new AddSpielerException( "Es befinden sich bereits 4 Spieler im Spiel. Der Spieler wurde daher nicht aufgenommen!" );
			}
		} else {
			throw new AddSpielerException( "Das Spiel laeuft bereits. Der Spieler wurde daher nicht mit aufgenommen!" );
		}
	}

	/*
	 * Eigentlicher Spielablauf
	 * Solange wie Runden spielbar sind, sollen die Spieler einen Zug
	 * vollfuehren
	 */
	// TODO Die Methoden run und getSpielerNr koennten eventuell noch in Laufend
	// untergebracht werden
	// Problem hierbei ist aber, dass das Spiel dann aktuell in einem Thread
	// laufen muss
	public void run() {
		while( rundenAnzahl < maxRundenZahl && aktZustand.getState() == State.LAUFEND ) {
			for( int a = 0; a < spielerListe.length; a++ ) {
				notifyObserver( spielerListe[ ( startPlayer + a ) % spielerListe.length ] );
			}

			// TODO warten bis alle Spieler ihren Zug gemacht haben
			Spieler tSpieler = stich.getSpieler();
			System.out.println( "Der Stich ging an: " + tSpieler + " (" + stich.getHoechsteKarte() + ")" );
			tSpieler.addStich( stich );

			stich = null;

			startPlayer = getSpielerNr( tSpieler );
			rundenAnzahl++;
		}

		if( rundenAnzahl == maxRundenZahl )
			beenden();
	}

	private int getSpielerNr( Spieler spieler ) {
		int a = 0;
		while( spieler != spielerListe[ a ] )
			a++;
		return a;
	}

	// Wird vom Spieler aufgerufen mit der ausgewaehlten Karte
	public void spielzugAusfuehren( Spieler spieler, Karte karte ) throws NotAValidCardException, NotYourTurnException {
		if(getCurrent() != spieler) {
			throw new NotYourTurnException();
		}
		if( stich == null ) {
			stich = new Stich( spieler, karte );
		} else {
			// Wenn die Karte gueltig war, dann entferne die Karte aus der Hand
			// von dem Spieler und pruefe, wem der Stich nun gehoert
			// Falls nicht gueltig, informiere den Spieler erneut, dass er eine
			// Karte aussuchen solls
			if( pruefeGueltigkeit( spieler, karte ) ) {
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



				// System.out.println();
				// System.out.println( "--> Spieler " + stich.getSpieler() + "
				// gehoert dem Stich" );
				// System.out.println();
			} else {
				System.out.println( "!!!--> Ausgewaehlte Karte war nicht gueltig!!" );
				notifyObserver( spieler );
				throw new NotAValidCardException();
			}
		}
		spieler.getHand().removeKarte( karte );
		round++;
		if(round % spielerListe.length == 0) {
			Spieler tSpieler = stich.getSpieler();
			System.out.println( "Der Stich ging an: " + tSpieler + " (" + stich.getHoechsteKarte() + ")" );
			tSpieler.addStich( stich );

			stich = null;

			startPlayer = getSpielerNr( tSpieler );
			round = 0;
		}
		// TODO: Async Kommunikation
		notifyObserver(spielerListe[ ( startPlayer + round ) % spielerListe.length ]);
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

	public Spieler[] getSpielerliste() {
		return spielerListe;
	}

	public List<StichRule> getStichRules() {
		return stichRules;
	}

	public void setAktuellerZustand( Zustand zustand ) {
		if( Spiel.DEBUG )
			System.out.println( "Setze Zustand auf " + zustand );
		this.aktZustand = zustand;
	}

	public void starten() {
		initialisieren();
		wiederaufnehmen();
		// Ersten Spieler benachrichtigen, dass er eine Karte legen darf
		notifyObserver(this.spielerListe[this.startPlayer]);
		System.out.println("Spieler wurde informiert");
//		run();

	}

	public void neustarten() {
		if( Spiel.DEBUG )
			System.out.println( "Spiel wird neugestartet..." );
		beenden();
		initialisieren();
		wiederaufnehmen();
	}

	public String getSpielID() {
		return spielID;
	}

	public List<Karte> getKartenSpiel() {
		return kartenSpiel;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public int getRundenAnzahl() {
		return rundenAnzahl;
	}

	public Spieler[] getSpielerListe() {
		return spielerListe;
	}

	public int getAnzahlSpieler() {
		return anzahlSpieler;
	}

	public int getMaxRundenZahl() {
		return maxRundenZahl;
	}

	public int getStartPlayer() {
		return startPlayer;
	}

	public Zustand getAktZustand() {
		return aktZustand;
	}

	public Stich getStich() {
		return stich;
	}

	/*
	 * Verteilung der Karten aus dem Kartenspiel an die Spieler und starten des
	 * Spiels
	 */

	public void initialisieren() {
		aktZustand.initialisieren();
	}

	public void pausieren() {
		aktZustand.pausieren();
	}

	public void wiederaufnehmen() {
		aktZustand.wiederaufnehmen();
	}

	public void beenden() {
		aktZustand.beenden();
	}

	@Override
	public String toString() {
		return spielID;
	}

}
