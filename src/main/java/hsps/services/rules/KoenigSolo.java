package hsps.services.rules;

public class KoenigSolo implements Rule {

	@Override
	public boolean test() {
		return false;
	}

	/*
	 * Koenigssolo - da der Koenig absolut keinen Wert hat, kann jemand der viele
	 * Koenige auf der Hand hat entweder Schmeissen oder ein Solo ansagen, sodass
	 * er faire Chancen bekommt.
	 */
}
