package hsps.services.hibernate;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table( name = "DBSTATISTIK" )
public class DBStatistik {

	@Id
	@GeneratedValue
	@Column( name = "id" )
	private int id;

	@Column( length = 1 )
	private char reGewonnen;

	@Column( name = "PunktestandRe" )
	private int punktestandRe;

	@Column( name = "PunktestandKontra" )
	private int punktestandKontra;

	@Column
	@CreationTimestamp
	private LocalDateTime createDateTime;

	@OneToMany( mappedBy = "DBSTATISTIK" )
	private List<DBSpieler> spielerliste;

	public DBStatistik() {}

	public int getId() {
		return id;
	}

	public void setId( int id ) {
		this.id = id;
	}

	public char getReGewonnen() {
		return reGewonnen;
	}

	public void setReGewonnen( char reGewonnen ) {
		this.reGewonnen = reGewonnen;
	}

	public int getPunktestandRe() {
		return punktestandRe;
	}

	public void setPunktestandRe( int punktestandRe ) {
		this.punktestandRe = punktestandRe;
	}

	public int getPunktestandKontra() {
		return punktestandKontra;
	}

	public void setPunktestandKontra( int punktestandKontra ) {
		this.punktestandKontra = punktestandKontra;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime( LocalDateTime createDateTime ) {
		this.createDateTime = createDateTime;
	}

	public List<DBSpieler> getSpielerliste() {
		return spielerliste;
	}

	public void setSpielerliste( List<DBSpieler> spielerliste ) {
		this.spielerliste = spielerliste;
	}

}
