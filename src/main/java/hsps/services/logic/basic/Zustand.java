package hsps.services.logic.basic;

public abstract class Zustand {

	protected Spiel spiel;

	public Zustand( Spiel spiel ) {
		this.spiel = spiel;
	}

	public abstract void initialisieren();

	public abstract void pausieren();

	public abstract void wiederaufnehmen();

	public abstract void beenden();

	public abstract State getState();

	public abstract String toString();
}
