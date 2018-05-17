package hsps.services.logic;

public class Karte {

	private boolean trumpf;
	private int wertigkeit;
	private Symbolik symbolik;
	private Farbwert farbwert;

	public Karte( Farbwert farbwert, Symbolik symbolik ) {
		this.symbolik = symbolik;
		this.farbwert = farbwert;

		// Mitels "symbolik" und "farbwert" kann die Wertigkeit und die Trumpf
		// Seinheit bestimmt werden
	}
	
	public boolean isTrumpf() {
		return trumpf;
	}

	public int getWertigkeit() {
		return wertigkeit;
	}

	@Override
	public String toString() {
		return farbwert + "-" + symbolik;
	}
}
