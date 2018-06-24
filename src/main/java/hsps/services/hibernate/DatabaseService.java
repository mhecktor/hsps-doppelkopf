package hsps.services.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.HibernateError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import hsps.services.logic.basic.Spiel;
import hsps.services.logic.player.Spieler;

@Service
public class DatabaseService {
	private static SessionFactory sessionFactory;

	public static void main( String[] args ) {
		System.out.println( "== START TEST DATABASESERVICE ==" );

		DBStatistik dbStatistik = new DBStatistik();
		List<DBSpieler> dbSpielerliste = new ArrayList<DBSpieler>();
		for( int i = 0; i < 4; i++ ) {
			DBSpieler dbSpieler = new DBSpieler();
			dbSpieler.setName( "Spieler" + i );
			dbSpieler.setDbStatistik( dbStatistik );
			dbSpielerliste.add( dbSpieler );
			DatabaseService.saveObject( dbSpieler );
		}
		int punkteContra = 110;
		int punkteRe = 109;

		dbStatistik.setPunktestandKontra( punkteContra );
		dbStatistik.setPunktestandRe( punkteRe );
		dbStatistik.setSpielerliste( dbSpielerliste );

		DatabaseService.saveObject( dbStatistik );
	}

	@PostConstruct
	public void init() {
		if( Spiel.DEBUG ) System.out.println( "Init DatabaseService" );
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	@PreDestroy
	public void destroy() {
		if( Spiel.DEBUG ) System.out.println( "Destroy DatabaseService" );
	}

	public static void saveObject( Object obj ) {
		if( Spiel.DEBUG ) System.out.println( "Saving " + obj + " to Database..." );
		Transaction tx = null;
		try (Session session = sessionFactory.openSession();) {
			tx = session.beginTransaction();

			session.save( obj );

			tx.commit();
		} catch( HibernateException e ) {
			if( tx != null ) tx.rollback();
			e.printStackTrace();
		}
	}

}
