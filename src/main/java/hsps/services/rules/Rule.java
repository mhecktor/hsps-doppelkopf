package hsps.services.rules;

import logic.Karte;
import logic.Stich;

public interface Rule {
	boolean test(  Stich stich, Karte karte );
}
