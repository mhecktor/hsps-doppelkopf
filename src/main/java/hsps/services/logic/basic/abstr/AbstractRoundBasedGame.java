package hsps.services.logic.basic.abstr;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.State;
import hsps.services.logic.basic.Zustand;
import hsps.services.logic.basic.conditions.Condition;

public abstract class AbstractRoundBasedGame {

	protected String spielID;

	@JsonIgnore
	protected Zustand aktZustand;

	public AbstractGameState gameState;

	protected ArrayList<Condition> conditions;

	protected boolean continueGame = false;

	public AbstractRoundBasedGame( String spielID ) {
		this.spielID = spielID;
		conditions = new ArrayList<Condition>();
	}

	public boolean testConditions() {
		for( Condition c : conditions ) {
			if( c.getGameState() == gameState ) {
				if( !c.testCondition() ) return false;
			}
		}

		return true;
	}

	public synchronized void next() {
		if( aktZustand.getState() != State.LAUFEND ) return;

		continueGame = false;

		switch( gameState ) {
			case RestartingGame:
				gameState = AbstractGameState.PreGame;
			case PreGame:
				preGame();
				break;
			case PerformGame:
				performGame();
				break;
			case PreTurn:
				preTurn();
				break;
			case PerformTurn:
				performTurn();
				break;
			case PostTurn:
				postTurn();
				break;
			case PostGame:
				postGame();
				break;
			case EndGame:
				break;
			default:
				break;
		}
		
		boolean nextState = testConditions();
		if( nextState ) {
			switch( gameState ) {
				case RestartingGame:
					gameState = AbstractGameState.PreGame;
					break;
				case PreGame:
					gameState = AbstractGameState.PerformGame;
					break;
				case PerformGame:
					gameState = AbstractGameState.PreTurn;
					break;
				case PreTurn:
					gameState = AbstractGameState.PerformTurn;
					getCurrentSpieler().notifyPerformTurn();
					break;
				case PerformTurn:
					gameState = AbstractGameState.PostTurn;
					break;
				case PostTurn:
					gameState = AbstractGameState.PostGame;
					break;
				case PostGame:
					gameState = AbstractGameState.EndGame;
					break;
				case EndGame:
					break;
			}
		} else {
			// Zyklus von PreTurn - PerformTurn - PostTurn einbauen
			if( gameState == AbstractGameState.PostTurn ) {
				gameState = AbstractGameState.PreTurn;
			}
		}

		if( continueGame ) next();
	}

	protected abstract void preGame();

	protected abstract void performGame();

	protected abstract void postGame();

	protected abstract void preTurn();

	protected abstract void performTurn();

	protected abstract void postTurn();

	protected abstract void starten();

	protected abstract void neustarten();

	public abstract AbstractSpieler getCurrentSpieler();

	public String getSpielID() {
		return spielID;
	}

	public void setAktuellerZustand( Zustand zustand ) {
		if( Spiel.DEBUG ) System.out.println( "Setze Zustand auf " + zustand );
		this.aktZustand = zustand;
	}

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

	public synchronized Zustand getAktZustand() {
		return aktZustand;
	}

	@Override
	public String toString() {
		return spielID;
	}
}
