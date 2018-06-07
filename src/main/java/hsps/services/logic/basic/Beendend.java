package hsps.services.logic.basic;

import hsps.services.MqttService;
import hsps.services.logic.player.Spieler;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;

public class Beendend extends Zustand {

	public Beendend( Spiel spiel ) {
		super( spiel );

		MqttService.publisher.publishData( new Message( MessageType.EndGame ) );
		auswerten();
	}

	@Override
	public void initialisieren() {
		if( Spiel.DEBUG ) System.out.println( "Spiel wird initialisiert..." );
		spiel.setAktuellerZustand( new Initialisierend( spiel ) );
	}

	@Override
	public void pausieren() {
		if( Spiel.DEBUG ) System.out.println( "Spiel bereits beendet!" );
	}

	@Override
	public void wiederaufnehmen() {
		if( Spiel.DEBUG ) System.out.println( "Spiel bereits beendet!" );
	}

	@Override
	public void beenden() {
		if( Spiel.DEBUG ) System.out.println( "Spiel bereits beendet!" );
	}

	@Override
	public State getState() {
		return State.BEENDEND;
	}

	@Override
	public String toString() {
		return getState().toString();
	}

	private void auswerten() {
		if( spiel.rundenAnzahl == spiel.maxRundenZahl ) {
			int punkteRe = 0;
			int punkteContra = 0;
			boolean siegRe = false;
			for( Spieler s : spiel.spielerListe ) {
				if( s.isRe() ) {
					punkteRe += s.getStichpunkte();
				} else {
					punkteContra += s.getStichpunkte();
				}
			}

			if( punkteRe > punkteContra ) {
				System.out.println( "Re gewinnt" );
				siegRe = true;
			} else {
				System.out.println( "Contra gewinnt" );
			}

			for( Spieler s : spiel.spielerListe ) {
				if( s.isRe() ) {
					if( siegRe ) {
						s.getStatistik().setSiege( s.getStatistik().getSiege() + 1 );
						s.getStatistik().setPunkte( s.getStatistik().getPunkte() + 1 );
					} else {
						s.getStatistik().setPunkte( s.getStatistik().getPunkte() - 1 );
					}
				} else {
					if( !siegRe ) {
						s.getStatistik().setSiege( s.getStatistik().getSiege() + 1 );
						s.getStatistik().setPunkte( s.getStatistik().getPunkte() + 1 );
					} else {
						s.getStatistik().setPunkte( s.getStatistik().getPunkte() - 1 );
					}
				}
			}
		}
	}

}
