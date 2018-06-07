package hsps.services.logic.basic;

import java.util.ArrayList;

import hsps.services.MqttService;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;
import hsps.services.logic.player.Spieler;
import hsps.services.logic.rules.basic.Armut;
import hsps.services.logic.rules.basic.KoenigSolo;
import hsps.services.logic.rules.basic.Rule;
import hsps.services.logic.rules.basic.Schmeissen;
import hsps.services.logic.rules.basic.Schweinchen;
import hsps.services.logic.rules.stich.StichRule;
import hsps.services.logic.rules.stich.StichRuleNormal;
import hsps.services.logic.rules.stich.StichRuleZweiteDulle;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Topic;

public class Initialisierend extends Zustand {

	public Initialisierend( Spiel spiel ) {
		super( spiel );
		
		MqttService.publisher.publishData( new Message( MessageType.InitGame) );
	}

	@Override
	public void initialisieren() {
		if( !( spiel.anzahlSpieler == spiel.spielerListe.length ) ) return;

		// Initialisierungen um das Spiel spaeter leichter neustarten zu koennen
		initKartenspiel();
		initSpieler();

		// TODO Die Regeln sollten am besten vorher ausgesucht werden koennen
		// und anhand der Pruefungen werden dann die Regeln fuer den Stich
		// hinzugefuegt
		initRules();

		if( Spiel.DEBUG ) {
			System.out.println( "--------------------" );
			System.out.println( "Karten der Spieler:" );
			for( Spieler s : spiel.spielerListe )
				System.out.println( s + ": " + s.getHand() );
			System.out.println( "--------------------" );
		}
	}

	@Override
	public void pausieren() {
		if( Spiel.DEBUG ) System.out.println( "Spiel kann nicht waehrend der Initialisierung pausiert werden!" );
	}

	@Override
	public void wiederaufnehmen() {
		if( Spiel.DEBUG ) System.out.println( "Spiel wird gestartet..." );

		// Zufallsmaessige Verteilung der Karten an die Spieler
		int kartenAnzahl = spiel.kartenSpiel.size();
		int spielerNr = 0;
		while( kartenAnzahl > 0 ) {
			Karte k = spiel.kartenSpiel.remove( (int) ( Math.random() * kartenAnzahl-- ) );
			spiel.spielerListe[ spielerNr ].getHand().addKarte( k );

			// Sende Karte an den Spieler
			MqttService.publisher.publishData( new Message( MessageType.GetCard, k ), Topic.genPlayerTopic( spielerNr ) );

			spielerNr = ( spielerNr + 1 ) % spiel.spielerListe.length;
		}

		// Erster Spieler wird informiert, dass dieser jetzt am Zug ist
		spiel.notifyObserver( spiel.spielerListe[ spiel.startPlayer ] );

		spiel.setAktuellerZustand( new Laufend( spiel ) );
	}

	@Override
	public void beenden() {
		if( Spiel.DEBUG ) System.out.println( "Spiel wird beendet." );
		spiel.setAktuellerZustand( new Beendend( spiel ) );
		spiel.beenden();
	}

	@Override
	public State getState() {
		return State.INITIALISIEREND;
	}

	@Override
	public String toString() {
		return getState().toString();
	}

	private void initKartenspiel() {
		spiel.kartenSpiel = new ArrayList<Karte>();
		for( int i = 0; i < 2; i++ )
			for( Symbolik s : Symbolik.values() )
				for( Farbwert f : Farbwert.values() )
					spiel.kartenSpiel.add( new Karte( f, s ) );
		spiel.maxRundenZahl = spiel.kartenSpiel.size() / spiel.spielerListe.length;
	}

	private void initSpieler() {
		// Die Karten muessen fuer jeden Spieler geleert werden
		// Ist fuer einen Neustart leichter
		for( Spieler s : spiel.spielerListe )
			s.getHand().resetKarten();
	}

	private void initRules() {
		spiel.rules = new ArrayList<Rule>();
		spiel.stichRules = new ArrayList<StichRule>();
		// Es wird immer nach den normalen Regeln gespielt
		// Die speziellste Regel steht ganz vorne
		spiel.stichRules.add( 0, new StichRuleNormal() );
		spiel.stichRules.add( 0, new StichRuleZweiteDulle() );

		spiel.rules.add( new Armut() );
		spiel.rules.add( new Schmeissen() );
		spiel.rules.add( new KoenigSolo() );
		// rules.add( new Pflichtansage() );
		spiel.rules.add( new Schweinchen() );

		// TODO spaeter eventuell ein dynamisches Hinzufuegen der Regeln vor
		// Beginn
		// des Spiel

		for( Rule r : spiel.rules ) {
			if( r.test( spiel ) ) {
				r.perform();
			}
		}

	}

}
