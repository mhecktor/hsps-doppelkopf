package hsps.services.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "DBSPIELER" )
public class DBSpieler {

	@Id
	@GeneratedValue
	@Column( name = "id" )
	private int id;

	@Column( name = "name", length = 20 )
	private String name;

	@ManyToOne
	@JoinColumn( name = "id", nullable = false )
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

	public DBStatistik getDbStatistik() {
		return dbStatistik;
	}

	public void setDbStatistik( DBStatistik dbStatistik ) {
		this.dbStatistik = dbStatistik;
	}

}
