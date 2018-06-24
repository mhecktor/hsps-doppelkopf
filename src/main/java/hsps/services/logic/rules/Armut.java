package hsps.services.logic.rules;

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

	private int anzahlTruempfe;
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
