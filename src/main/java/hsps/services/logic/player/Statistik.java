package hsps.services.logic.player;

public class Statistik {

	private int siege;
	private int punkte;
	private int hochzeiten;
	private int fuechse;
	private int solos;
	private int solosGewonnen;

	public Statistik() {}

	public int getSiege() {
		return siege;
	}

	public void setSiege( int siege ) {
		this.siege = siege;
	}

	public int getPunkte() {
		return punkte;
	}

	public void setPunkte( int punkte ) {
		this.punkte = punkte;
	}

	public int getHochzeiten() {
		return hochzeiten;
	}

	public void setHochzeiten( int hochzeiten ) {
		this.hochzeiten = hochzeiten;
	}

	public int getFuechse() {
		return fuechse;
	}

	public void setFuechse( int fuechse ) {
		this.fuechse = fuechse;
	}

	public int getSolos() {
		return solos;
	}

	public void setSolos( int solos ) {
		this.solos = solos;
	}

	public int getSolosGewonnen() {
		return solosGewonnen;
	}

	public void setSolosGewonnen( int solosGewonnen ) {
		this.solosGewonnen = solosGewonnen;
	}

	@Override
	public String toString() {
		return String.format( "[ Siege: %d, Punkte: %d, Fuechse: %d, Solos: %d (davon gewonnen %d), Hochzeiten: %d ]", siege, punkte, fuechse, solos, solosGewonnen, hochzeiten );
	}

}
