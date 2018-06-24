package hsps.services.logic.player;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hsps.services.MqttService;
import hsps.services.exception.NotYourTurnException;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.Stich;
import hsps.services.logic.basic.abstr.AbstractSpieler;
import hsps.services.logic.cards.Karte;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Topic;

public class Spieler extends AbstractSpieler {

	@JsonIgnore
	private Spiel spiel;

	private String name;
	private Hand hand;
	private Statistik statistik;
	private boolean solo;
	private List<Stich> gesammelteStiche;
	private static int uniqueIdIndex = 0;
	private int uniqueId;

	private Karte chosenCard;

	public Spieler( Spiel spiel, String name ) {
		uniqueId = uniqueIdIndex++;
		this.spiel = spiel;
		this.name = name;
		gesammelteStiche = new ArrayList<Stich>();
		hand = new Hand();
		statistik = new Statistik();
	}

	public int getUniqueId() {
		return uniqueId;
	}
	
	public void addStich( Stich stich ) {
		gesammelteStiche.add( stich );
	}

	public Karte getChosenCard() {
		return chosenCard;
	}

	public void setChosenCard( Karte chosenCard ) {
		this.chosenCard = chosenCard;
	}

	public void performDecisionRule( boolean arg ) {
		if( arg ) spiel.performDecisionRule();

		spiel.next();
	}

	public void performDecisionAnnouncement( boolean arg ) {
		if( arg )
			spiel.performDecisionAnnouncement();
		else
			spiel.next();
	}

	public String getName() {
		return name;
	}

	public List<Stich> getGesammelteStiche() {
		return gesammelteStiche;
	}

	public int getStichpunkte() {
		int sum = 0;
		for( Stich s : gesammelteStiche ) {
			sum += s.getPunktezahl();
		}
		return sum;
	}

	public Hand getHand() {
		return hand;
	}

	public boolean isRe() {
		return hand.isRe();
	}
	
	public void setRe( boolean re ) {
		hand.setRe( re );
	}

	public boolean isSolo() {
		return solo;
	}

	public void setSolo( boolean solo ) {
		this.solo = solo;
	}

	public Statistik getStatistik() {
		return statistik;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void performTurn() throws NotYourTurnException {
		if( spiel.getCurrentSpieler() != this ) throw new NotYourTurnException();
		spiel.next();
	}

	@Override
	public void notifyPerformTurn() {
		if( Spiel.DEBUG ) System.out.println( this.getName() + " soll seine Karte waehlen!" );
		MqttService.publisher.publishData( new Message( MessageType.ChooseCard ), Topic.genPlayerTopic( spiel.getSpielID(), spiel.getSpielerNr( this ) ) );
	}

	@Override
	public void pauseGame() {
		spiel.pausieren();
	}

	@Override
	public void resumeGame() {
		spiel.wiederaufnehmen();

	}
}
