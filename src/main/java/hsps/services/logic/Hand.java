package hsps.services.logic;

import java.util.LinkedList;
import java.util.List;

public class Hand {

	private List<Karte> karten;

	public Hand() {
		karten = new LinkedList<Karte>();
	}

	// TODO ausgewaehlte Karte muss entfernt werden
	public void removeKarte() {

	}

	public void addKarte( Karte karte ) {
		karten.add( karte );
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
