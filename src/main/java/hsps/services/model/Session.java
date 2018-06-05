package hsps.services.model;

public class Session {
    private long id;
    private String name;
    private short player;

    public Session() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getPlayer() {
        return player;
    }

    public void setPlayer(short player) {
        this.player = player;
    }
}
