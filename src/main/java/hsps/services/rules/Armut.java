package hsps.services.rules;

public class Armut implements Rule {

	/*
	 * Armut - da Truempfe in dem Spiel sehr wichtig sind, bestimmt das Glueck
	 * darueber, wie gut die Chancen auf den Sieg stehen. Um dieses zu
	 * verhindern, wird neu gegeben, sobald ein Spieler 3 oder weniger Truempfe
	 * auf der Hand hat.
	 */

	@Override
	public boolean test() {

		return false;
	}
}
