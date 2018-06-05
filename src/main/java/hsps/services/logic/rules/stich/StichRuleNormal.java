package hsps.services.logic.rules.stich;

import hsps.services.logic.basic.Stich;
import hsps.services.logic.basic.Symbolik;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;

public class StichRuleNormal implements StichRule {

    /*
     * Es wird ueberprueft wem der Stich gehoert
     */
    public boolean changeBelonging(Stich stich, Karte karte) {
        /* Es werden die "False"-Faelle geprueft */
        // Wenn der Stich eigentlich Trumpf verlangt, aber eine nicht
        // Trumpf-Karte gelegt wurde
        if (stich.getErsteKarte().isTrumpf() && !karte.isTrumpf()) return false;

        // Wenn die falsche Farbe bedient wurde
        if (!stich.getErsteKarte().isTrumpf() && karte.getFarbwert() != stich.getErsteKarte().getFarbwert())
            return false;

        // Wenn bereits eine "Dulle" im Stich vorhanden ist
        if (stich.getHoechsteKarte().getFarbwert() == Farbwert.HERZ && stich.getHoechsteKarte().getSymbolik() == Symbolik.ZEHN)
            return false;

        // Wenn die gelegte Karte gleichwertig mit der aktuell hoechsten Karte
        // ist
        if (karte.getWertigkeit() <= stich.getHoechsteKarte().getWertigkeit()) return false;

        return true;
    }
}
