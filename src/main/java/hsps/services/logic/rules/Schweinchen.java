package hsps.services.logic.rules;

import hsps.services.MqttService;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;
import hsps.services.logic.player.Spieler;
import hsps.services.logic.rules.stich.StichRuleSchweinchen;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;

public class Schweinchen implements Rule {

	/*
	 * Schweinchen - durch die Fuechse lassen sich fuer die Gegner viele Punkte
	 * holen. Wenn ein Spieler beide Fuechse hat, minimieren sich seine
	 * Gewinnchancen. Durch die Schweinchen-Regel soll dies verhindert werden,
	 * indem die Fuechse zur hoechsten Karte werden, sobald ein Spieler beide
	 * hat.
	 * Dies muss beim Ausspielen angesagt werden.
	 */

	private int anzFuechse ;
	private Spiel spiel;
	private Spieler spieler;

	@Override
	public boolean test( Spiel spiel ) {
		anzFuechse = 0;
		this.spiel = spiel;
		// Durch die Hand von jedem Spieler Iterieren und Gucken ob einer 2
		// Karo-Asse Besitzt
		for( Spieler s : spiel.getSpielerListe() ) {
			this.spieler = s;
			for( Karte k : s.getHand().getKarten() ) {
				if( k.getSymbolik() == Symbolik.ASS && k.getFarbwert() == Farbwert.KARO ) {
					anzFuechse++;
				}
			}

			// Sollte jemand zwei Karo-Asse besitzen tritt die Regel in Kraft
			if( anzFuechse == 2 ) { return true; }

			anzFuechse = 0;
		}
		return false;
	}

	@Override
	public void perform() {
		MqttService.publisher.publishData( new Message( MessageType.Schweinchen, spieler ) );
		spiel.getStichRules().add( 0, new StichRuleSchweinchen() );
	}

}
