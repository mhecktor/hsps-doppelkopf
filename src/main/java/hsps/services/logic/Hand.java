package hsps.services.logic;

import java.util.LinkedList;
import java.util.List;

public class Hand {

	private boolean reh;
	private List<Karte> karten;
	
	public Hand() {
		karten = new LinkedList<Karte>();
	}

	public void removeKarte( Karte karte ) {
		karten.remove( karte );
	}

	public void addKarte( Karte karte ) {
		if( karte.getSymbolik() == Symbolik.DAME 
			&& karte.getFarbwert() == Farbwert.KREUZ )
			reh = true;
		karten.add( karte );
	}
	
	public boolean isReh() {
		return reh;
	}
	
	public List<Karte> getKarten() {
		return karten;
	}

	@Override
	public String toString() {
		String s = "[";
		boolean first = true;
		for( Karte k : karten ) {
			if( first )
				first = false;
			else
				s += ", ";
			s += k;
		}
		s += "]";
		return s;
	}
}
