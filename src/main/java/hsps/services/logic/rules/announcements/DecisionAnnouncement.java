package hsps.services.logic.rules.announcements;

import java.util.ArrayList;
import java.util.List;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.player.Spieler;

public abstract class DecisionAnnouncement extends Announcement {

	private List<Spieler> playersAskedAnnouncement = new ArrayList<Spieler>();
	
	public boolean called = false;
	
	protected Spiel spiel;
	protected Spieler spieler;
	
	@Override
	public boolean test( Spiel spiel ) {
		this.spiel = spiel;
		this.spieler = spiel.getCurrentSpieler();
		if( !playersAskedAnnouncement.contains( spieler ) ) {
			System.out.println( "Ask" + spieler );
			playersAskedAnnouncement.add( spieler );
			return testCondition();
		}
		return false;
	}
	
	protected abstract boolean testCondition();
	
	public abstract void inform(); 
}
