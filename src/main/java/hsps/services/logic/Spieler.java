package hsps.services.logic;

import java.util.ArrayList;
import java.util.Collection;

public class Spieler {

	private String name;
	private Collection<Stich> gesammelteStiche;
	private boolean reh;
	private String ip;
	private Hand hand;

	public Spieler( String name ) {
		this.name = name;
		gesammelteStiche = new ArrayList<Stich>();
		hand = new Hand();
	}

	public Hand getHand() {
		return hand;
	}

	public boolean isReh() {
		return reh;
	}

	@Override
	public String toString() {
		return name;
	}
}
