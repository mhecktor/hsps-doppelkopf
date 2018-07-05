package hsps.services.logic.cards;

// TODO Eventuell kann man noch eine Klasse "DoppelkopfKarte" erstellen und diese dann von "Karte" erben lassen
public class Karte {

	public Karte() {

	}

	private Symbolik symbolik;
	private Farbwert farbwert;
	private int wertigkeit;
	private boolean trumpf;

	public String getPlayedByPlayer() {
		return playedByPlayer;
	}

	public void setPlayedByPlayer(String playedByPlayer) {
		this.playedByPlayer = playedByPlayer;
	}

	private String playedByPlayer;
	
	public Karte( Farbwert farbwert, Symbolik symbolik ) {
		this.symbolik = symbolik;
		this.farbwert = farbwert;

		if( farbwert == Farbwert.KARO ) trumpf = true;

		switch( symbolik ) {
			case ASS:
				wertigkeit = 11;
				break;
			case ZEHN:
				if( farbwert == Farbwert.HERZ ) trumpf = true;
				wertigkeit = 10;
				break;
			case KOENIG:
				wertigkeit = 4;
				break;
			case DAME:
				trumpf = true;
				wertigkeit = 3;
				break;
			case BUBE:
				trumpf = true;
				wertigkeit = 2;
				break;
		}
	}

	public boolean isTrumpf() {
		return trumpf;
	}

	public void setTrumpf( boolean trumpf ) {
		this.trumpf = trumpf;
	}

	public int getWertigkeit() {
		return wertigkeit;
	}

	public Symbolik getSymbolik() {
		return symbolik;
	}

	public Farbwert getFarbwert() {
		return farbwert;
	}

	@Override
	public String toString() {
		return farbwert + "-" + symbolik;
	}

}
