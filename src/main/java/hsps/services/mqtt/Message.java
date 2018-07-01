package hsps.services.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    @JsonProperty
    private MessageType type;

    @JsonProperty
    private Object data;

    public Message() {
    }

    public Message(MessageType type) {
        this(type, null);
    }

    public Message(MessageType type, Object arg) {
        this.type = type;
        this.data = arg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " : " + data + " : " + data;
    }

}
