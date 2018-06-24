package hsps.services.logic.rules.decision;

import hsps.services.MqttService;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;
import hsps.services.logic.player.Spieler;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Topic;

public class Schmeissen implements DecisionRule {

	/*
	 * Schmeissen - da die Koenige die niedrigste Karte im Spiel sind, soll ein
	 * Spieler, der 5 Koenige hat, die Moeglichkeit haben, ein neues Blatt zu
	 * bekommen, da er sonst einen Nachteil hat.
	 */

	private int anzKoenige;
	private Spiel spiel;
	private Spieler spieler;

	@Override
	public boolean test( Spiel spiel ) {
		anzKoenige = 0;
		this.spiel = spiel;
		for( Spieler s : spiel.getSpielerListe() ) {
			this.spieler = s;
			for( Karte k : s.getHand().getKarten() ) {
				if( k.getSymbolik() == Symbolik.KOENIG ) {
					anzKoenige++;
				}
			}
			if( anzKoenige >= 5 ) { return true; }
			anzKoenige = 0;
		}
		return false;
	}

	@Override
	public void inform() {
		MqttService.publisher.publishData( new Message( MessageType.AskSchmeissen ), Topic.genPlayerTopic( spiel.getSpielID(), spiel.getSpielerNr( this.spieler ) ) );
	}

	@Override
	public void perform() {
		MqttService.publisher.publishData( new Message( MessageType.Schmeissen, this.spieler ) );
		spiel.neustarten();
	}

}
