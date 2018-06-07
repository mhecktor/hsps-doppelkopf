package hsps.services.logic.rules.stich;

import hsps.services.logic.basic.Stich;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;

public class StichRuleNormal implements StichRule {

	/*
	 * Es wird ueberprueft wem der Stich gehoert
	 */
	public boolean changeBelonging( Stich stich, Karte karte ) {
		// Wenn der Stich eigentlich Trumpf verlangt, aber eine nicht
		// Trumpf-Karte gelegt wurde
		// => Wechsel
		if( stich.getErsteKarte().isTrumpf() && !karte.isTrumpf() )
			return false;

		// Es wurde eine Nicht-Trumpfkarte gespeilt, aber die hoechste Karte ist
		// bereits ein Trumpf
		// => Kein-Wechsel
		if( stich.getHoechsteKarte().isTrumpf() && !karte.isTrumpf() )
			return false;

		// Es wurde eine Trumpkarte gespielt, aber eine Nicht-Trumpfkarte ist
		// die hoechste Karte
		// => Wechsel
		if( !stich.getHoechsteKarte().isTrumpf() && karte.isTrumpf() )
			return true;

		// Wenn die falsche Farbe bedient wurde
		// => Kein-Wechsel
		if( !stich.getErsteKarte().isTrumpf() && karte.getFarbwert() != stich.getErsteKarte().getFarbwert() )
			return false;

		// Wenn bereits eine "Dulle" im Stich vorhanden ist
		// => Kein-Wechsel
		if( stich.getHoechsteKarte().getFarbwert() == Farbwert.HERZ && stich.getHoechsteKarte().getSymbolik() == Symbolik.ZEHN )
			return false;

		// Abfangen der Karo-Karten, da diese im Normalfall eine
		// Sonderbehandlung brauchen -.-

		if( karte.getFarbwert() == Farbwert.KARO && karte.isTrumpf() ) {
			switch( karte.getSymbolik() ) {
				case ASS:
					if( stich.getHoechsteKarte().isTrumpf() && ( stich.getHoechsteKarte().getSymbolik() == Symbolik.BUBE || stich.getHoechsteKarte().getSymbolik() == Symbolik.DAME || ( stich.getHoechsteKarte().getFarbwert() == Farbwert.HERZ && stich.getHoechsteKarte().getSymbolik() == Symbolik.ZEHN ) || ( stich.getHoechsteKarte().getFarbwert() == Farbwert.KARO && stich.getHoechsteKarte().getSymbolik() == Symbolik.ASS ) ) )
						return false;
					else
						return true;
				case ZEHN:
					if( stich.getHoechsteKarte().isTrumpf() && ( stich.getHoechsteKarte().getSymbolik() == Symbolik.BUBE || stich.getHoechsteKarte().getSymbolik() == Symbolik.DAME || ( stich.getHoechsteKarte().getFarbwert() == Farbwert.HERZ && stich.getHoechsteKarte().getSymbolik() == Symbolik.ZEHN ) || ( stich.getHoechsteKarte().getFarbwert() == Farbwert.KARO && stich.getHoechsteKarte().getSymbolik() == Symbolik.ASS ) || ( stich.getHoechsteKarte().getFarbwert() == Farbwert.KARO && stich.getHoechsteKarte().getSymbolik() == Symbolik.ZEHN ) ) )
						return false;
					else
						return true;
				case KOENIG:
					if( stich.getHoechsteKarte().isTrumpf() )
						return false;
					else
						return true;
				default:
					break;
			}
		}

		if( stich.getHoechsteKarte().getFarbwert() == Farbwert.KARO && stich.getHoechsteKarte().isTrumpf() ) {
			if( karte.isTrumpf() ) {
				if( karte.getFarbwert() != Farbwert.KARO )
					return true;
				else {
					switch( karte.getSymbolik() ) {
						case BUBE:
						case DAME:
							if( karte.getWertigkeit() + 10 > stich.getHoechsteKarte().getWertigkeit() )
								return true;
							else
								return false;
						default:
							if( karte.getWertigkeit() <= stich.getHoechsteKarte().getWertigkeit() )
								return false;

					}
				}
			}
		}

		// Wenn die gelegte Karte gleichwertig mit der aktuell hoechsten Karte
		// ist
		if( karte.getWertigkeit() == stich.getHoechsteKarte().getWertigkeit() ) {
			if( karte.getFarbwert().ordinal() <= stich.getHoechsteKarte().getFarbwert().ordinal() )
				return false;
			else
				return true;
		}

		if( karte.getWertigkeit() <= stich.getHoechsteKarte().getWertigkeit() )
			return false;

		return true;
	}
}
