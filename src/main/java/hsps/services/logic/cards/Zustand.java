package hsps.services.logic.cards;

import hsps.services.logic.basic.Spiel;

public abstract class Zustand {

	protected Spiel spiel;

	public Zustand( Spiel spiel ) {
		this.spiel = spiel;
	}

	public abstract void pausieren();

	public abstract void wiederaufnahme();
}
