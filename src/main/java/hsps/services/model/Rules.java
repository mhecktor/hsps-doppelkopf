package hsps.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rules {

	private boolean armut;
	private boolean schweinchen;
	private boolean zweiteDulle;
	private boolean schmeissen;
	private boolean koenigsSolo;
	private boolean pflichtAnsage;

	public Rules() {}

	public boolean isArmut() {
		return armut;
	}

	@JsonProperty("armut")
	public void setArmut( boolean armut ) {
		this.armut = armut;
	}

	public boolean isSchweinchen() {
		return schweinchen;
	}

	@JsonProperty("schweinchen")
	public void setSchweinchen( boolean schweinchen ) {
		this.schweinchen = schweinchen;
	}

	public boolean isZweiteDulle() {
		return zweiteDulle;
	}

	@JsonProperty("zweiteDulle")
	public void setZweiteDulle( boolean zweiteDulle ) {
		this.zweiteDulle = zweiteDulle;
	}

	public boolean isSchmeissen() {
		return schmeissen;
	}

	@JsonProperty("schmeissen")
	public void setSchmeissen( boolean schmeissen ) {
		this.schmeissen = schmeissen;
	}

	public boolean isKoenigsSolo() {
		return koenigsSolo;
	}

	@JsonProperty("koenigsSolo")
	public void setKoenigSolo( boolean koenigsSolo ) {
		this.koenigsSolo = koenigsSolo;
	}

	public boolean isPflichtAnsage() {
		return pflichtAnsage;
	}

	@JsonProperty("pflichtAnsage")
	public void setPflichtAnsage( boolean pflichtAnsage ) {
		this.pflichtAnsage = pflichtAnsage;
	}

}
