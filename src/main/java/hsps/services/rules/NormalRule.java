package hsps.services.rules;

import logic.Farbwert;
import logic.Karte;
import logic.Stich;
import logic.Symbolik;

public class NormalRule implements Rule {

	/*
	 * Es wird ueberprueft wem der Stich gehoert
	 */

	@Override
	public boolean test( Stich stich, Karte karte ) {
		/* Es werden die "False"-Faelle geprueft */
		// Wenn der Stich eigentlich Trumpf verlangt, aber eine nicht
		// Trumpf-Karte gelegt wurde
		if( stich.getErsteKarte().isTrumpf() && !karte.isTrumpf() ) return false;

		// Wenn die falsche Farbe bedient wurde
		if( !stich.getErsteKarte().isTrumpf() && karte.getFarbwert() != stich.getErsteKarte().getFarbwert() ) return false;

		// Wenn bereits eine "Dulle" im Stich vorhanden ist
		if( stich.getHoechsteKarte().getFarbwert() == Farbwert.HERZ && stich.getHoechsteKarte().getSymbolik() == Symbolik.ZEHN ) return false;

		// Wenn die gelegte Karte gleichwertig mit der aktuell hoechsten Karte
		// ist
		if( karte.getWertigkeit() <= stich.getHoechsteKarte().getWertigkeit() ) return false;

		return true;
	}

}
