package hsps.services.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;

import hsps.services.MqttService;
import hsps.services.exception.AddSpielerException;
import hsps.services.exception.NotYourTurnException;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.player.Spieler;
import hsps.services.model.Rules;

public class TestProgramm {

	static Spiel spiel;
	static MqttService mqttService;

	static List<Subscriber> subscribers;

	public static void main( String[] args ) throws MqttException, NotYourTurnException {
		mqttService = new MqttService();
		mqttService.init();
		subscribers = new ArrayList<Subscriber>();

		subscribers.add( new Subscriber() );
		subscribers.add( new Subscriber() );
		subscribers.add( new Subscriber() );
		subscribers.add( new Subscriber() );

		for( Subscriber s : subscribers )
			s.init();

		spiel = new Spiel( "Testspiel" );
		setRules();
		for( int i = 0; i < subscribers.size(); i++ ) {
			subscribers.get( i ).setSpieler( new Spieler( spiel, "Spieler " + i ) );
		}

		try {
			for( Subscriber s : subscribers )
				spiel.addSpieler( s.getSpieler() );
		} catch( AddSpielerException e ) {
			e.printStackTrace();
		}

		spiel.starten();
	}

	public static void setRules() {
		Rules rules = new Rules();
		rules.setArmut( true );
		rules.setKoenigSolo( true );
		rules.setZweiteDulle( true );
		rules.setSchmeissen( true );
		rules.setSchweinchen( true );
		spiel.setRules( rules );
	}

	public static synchronized void writeKarten( Spieler spieler ) {
		System.out.println( "/\\----------/\\" );
		System.out.println( "Karten von " + spieler.getName() );
		for( Karte k : spieler.getHand().getKarten() ) {
			System.out.print( k + " " );
		}
		System.out.println( "\\/----------\\/" );
	}
}
