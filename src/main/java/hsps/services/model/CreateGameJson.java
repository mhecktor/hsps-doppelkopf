package hsps.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGameJson {
    private String gameName;
    private String playerName;
    private int rounds;

    public int getRounds() {
        return rounds;
    }

    @JsonProperty("rounds")
    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public String getGameName() {
        return gameName;
    }

    @JsonProperty("gameName")
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPlayerName() {

        return playerName;
    }

    @JsonProperty("playerName")
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
