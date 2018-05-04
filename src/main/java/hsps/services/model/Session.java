package hsps.services.model;

public class Session {
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer(short player) {
        this.player = player;
    }

    private long id;
    private String name;
    private short player;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public short getPlayer() {
        return player;
    }

    public Session() {
    }
}
