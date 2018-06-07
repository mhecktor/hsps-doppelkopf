package hsps.services.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {

	@JsonProperty
	private MessageType type;

	@JsonProperty
	private byte[] bytes;

	public Message() {}

	public Message( MessageType type ) {
		this( type, null );
	}

	public Message( MessageType type, Object arg ) {
		this.type = type;
		try {
			ObjectMapper objMapper = new ObjectMapper();
			bytes = objMapper.writeValueAsBytes( arg );
		} catch( JsonProcessingException e ) {
			e.printStackTrace();
		}
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setType( MessageType type ) {
		this.type = type;
	}

	public MessageType getType() {
		return type;
	}

	@Override
	public String toString() {
		return type + " : " + bytes;
	}

}
