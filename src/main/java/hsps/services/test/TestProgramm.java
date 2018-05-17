package hsps.services.test;

import logic.Farbwert;
import logic.Hand;
import logic.Karte;
import logic.Symbolik;

public class TestProgramm {

	public static void main( String[] args ) {

		Hand h = new Hand();
		h.addKarte( new Karte( Farbwert.HERZ, Symbolik.ASS ) );
		h.addKarte( new Karte( Farbwert.KARO, Symbolik.DAME ) );
		System.out.println( h );
	}
}
