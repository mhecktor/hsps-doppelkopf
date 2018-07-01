package hsps.services.logic.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hsps.services.MqttService;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.player.Spieler;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;

public class Armut implements Rule {
	/*
	 * Armut - da Truempfe in dem Spiel sehr wichtig sind, bestimmt das Glueck
	 * darueber, wie gut die Chancen auf den Sieg stehen. Um dieses zu
	 * verhindern, wird neu gegeben, sobald ein Spieler 3 oder weniger Truempfe
	 * auf der Hand hat.
	 */

	public int getAnzahlTruempfe() {
		return anzahlTruempfe;
	}

	public void setAnzahlTruempfe(int anzahlTruempfe) {
		this.anzahlTruempfe = anzahlTruempfe;
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

	private int anzahlTruempfe;
	@JsonIgnore
	private Spiel spiel;
	private Spieler spieler;

	@Override
	public boolean test( Spiel spiel ) {
		anzahlTruempfe = 0;
		this.spiel = spiel;
		for( Spieler s : spiel.getSpielerListe() ) {
			this.spieler = s;
			for( Karte k : s.getHand().getKarten() ) {
				if( k.isTrumpf() ) {
					anzahlTruempfe++;
				}
			}
			if( anzahlTruempfe <= 3 ) {
				return true;
			}
		}
		return false;

	}

	@Override
	public void perform() {
		MqttService.publisher.publishData( new Message( MessageType.Armut, spieler ) );
		spiel.neustarten();
	}
}
