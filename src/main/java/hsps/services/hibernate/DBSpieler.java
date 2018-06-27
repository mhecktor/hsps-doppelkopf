package hsps.services.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "DBSpieler" )
public class DBSpieler {

	@Id
	@GeneratedValue
	@Column( name = "idSpieler", nullable = false )
	private int id;

	@Column( name = "name", length = 20, nullable = false )
	private String name;

	@Column( name = "re", length = 1, nullable = false )
	private char re;

	@ManyToOne
	@JoinColumn( name = "idStatistik", referencedColumnName = "idStatistik", nullable = false )
	private DBStatistik dbStatistik;

	public DBSpieler() {}

	public int getId() {
		return id;
	}

	public void setId( int id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public char getRe() {
		return re;
	}

	public void setRe( char re ) {
		this.re = re;
	}

	public DBStatistik getDbStatistik() {
		return dbStatistik;
	}

	public void setDbStatistik( DBStatistik dbStatistik ) {
		this.dbStatistik = dbStatistik;
	}

}
