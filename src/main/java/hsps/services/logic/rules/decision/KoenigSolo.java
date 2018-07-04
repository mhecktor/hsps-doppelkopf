package hsps.services.logic.rules.decision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hsps.services.MqttService;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;
import hsps.services.logic.player.Spieler;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Topic;

public class KoenigSolo implements DecisionRule {

	/*
	 * Koenigssolo - da der Koenig absolut keinen Wert hat, kann jemand der
	 * viele
	 * Koenige auf der Hand hat entweder Schmeissen oder ein Solo ansagen,
	 * sodass
	 * er faire Chancen bekommt.
	 */

	private int anzKoenige;

	public int getAnzKoenige() {
		return anzKoenige;
	}

	public void setAnzKoenige(int anzKoenige) {
		this.anzKoenige = anzKoenige;
	}

	public Spiel getSpiel() {
		return spiel;
	}

	public void setSpiel(Spiel spiel) {
		this.spiel = spiel;
	}

	public Spieler getSpieler() {
		return spieler;
	}

	public void setSpieler(Spieler spieler) {
		this.spieler = spieler;
	}

	@JsonIgnore
	private Spiel spiel;
	private Spieler spieler;

	@Override
	public boolean test( Spiel spiel ) {
		anzKoenige = 0;
		this.spiel = spiel;
		// Durch jede Hand von jedem Spieler Iterieren
		for( Spieler s : spiel.getSpielerListe() ) {
			this.spieler = s;
			for( Karte k : s.getHand().getKarten() ) {
				if( k.getSymbolik() == Symbolik.KOENIG ) {
					anzKoenige++;
				}
				if( anzKoenige >= 5 ) { return true; }
			}
			anzKoenige = 0;
		}
		return false;
	}

	@Override
	public void inform() {
		MqttService.publisher.publishData( new Message( MessageType.AskKoenigSolo ), Topic.genPlayerTopic( spiel.getSpielID(), spieler.getName()) );
	}

	@Override
	public void perform() {
		MqttService.publisher.publishData( new Message( MessageType.KoenigSolo, spieler ) );
		spieler.setSolo( true );

		// Durch jede Hand von jedem Spieler Iterieren
		// und Die Truempfe fuer dieses Spiel aendern
		for( Spieler spieler : spiel.getSpielerListe() ) {
			for( Karte karte : spieler.getHand().getKarten() ) {
				if( karte.getSymbolik() == Symbolik.KOENIG ) {
					karte.setTrumpf( false );
				} else {
					karte.setTrumpf( true );
				}
			}
		}
	}
}
