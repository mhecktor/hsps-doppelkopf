package hsps.services.hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table( name = "DBSTATISTIK" )
public class DBStatistik {

	@Id
	@Column( name = "idStatistik" )
	private int idStatistik;

	@Column( name = "reGewonnen", length = 1, nullable = false )
	private char reGewonnen;

	@Column( name = "PunktestandRe", nullable = false )
	private int punktestandRe;

	@Column( name = "PunktestandKontra", nullable = false )
	private int punktestandKontra;

	@Column( nullable = false )
	@CreationTimestamp
	private LocalDateTime createDateTime;

	@OneToMany( mappedBy = "dbStatistik" )
	private List<DBSpieler> spielerliste = new ArrayList<DBSpieler>();

	public DBStatistik() {}

	public int getIdStatistik() {
		return idStatistik;
	}

	public void setIdStatistik( int idStatistik ) {
		this.idStatistik = idStatistik;
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
