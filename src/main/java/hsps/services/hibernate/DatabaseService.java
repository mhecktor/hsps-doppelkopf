package hsps.services.hibernate;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import hsps.services.logic.basic.Spiel;

@Service
public class DatabaseService {
	// private static SessionFactory sessionFactory;
	// Dockerfreunde: docker run -d -p 49161:1521 wnameless/oracle-xe-11g
//	private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
	private static SessionFactory sessionFactory;

	@PostConstruct
	public void init() {
		if( Spiel.DEBUG ) System.out.println( "Init DatabaseService" );
		//sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	@PreDestroy
	public void destroy() {
		if( Spiel.DEBUG ) System.out.println( "Destroy DatabaseService" );
	}

	public static synchronized void saveObject( Object arg ) {
		Transaction tx = null;
		try (Session session = sessionFactory.openSession();) {
			tx = session.beginTransaction();
			session.save( arg );

			tx.commit();
		} catch( HibernateException e ) {
			if( tx != null ) tx.rollback();
			e.printStackTrace();
		}
	}

	public static void main( String[] args ) {
		System.out.println( "== START TEST DATABASESERVICE ==" );
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Transaction tx = null;
		try (Session session = sessionFactory.openSession();) {
			tx = session.beginTransaction();

			DBStatistik dbStatistik = new DBStatistik();

			int punkteContra = 110;
			int punkteRe = 109;

			dbStatistik.setIdStatistik( 5 );
			dbStatistik.setCreateDateTime( LocalDateTime.now() );
			dbStatistik.setPunktestandKontra( punkteContra );
			dbStatistik.setPunktestandRe( punkteRe );
			dbStatistik.setReGewonnen( 'y' );
			session.save( dbStatistik );
			// DatabaseService.saveObject( dbStatistik );

			for( int i = 0; i < 4; i++ ) {
				DBSpieler dbSpieler = new DBSpieler();
				dbSpieler.setId( ( i ) );
				dbSpieler.setName( "Spieler" + ( i ) );
				dbSpieler.setDbStatistik( dbStatistik );
				dbStatistik.getSpielerliste().add( dbSpieler );
				// DatabaseService.saveObject( dbSpieler );
				session.save( dbSpieler );

			}
			// session.save( obj );

			tx.commit();
		} catch( HibernateException e ) {
			if( tx != null ) tx.rollback();
			e.printStackTrace();
		}
	}

}
