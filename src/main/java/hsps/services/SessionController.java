package hsps.services;

import hsps.services.exception.AddSpielerException;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.player.Hand;
import hsps.services.logic.player.Spieler;
import hsps.services.model.CreateGameJson;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class SessionController {

    private List<Spiel> sessions = new ArrayList<>();

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public List<Spiel> sessions(@RequestParam(name = "name", required = false, defaultValue = "Session") String name) {
        return sessions;
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.POST)
    public Spiel createSession(@RequestBody CreateGameJson createdGame) {
        Spiel newGame = new Spiel(createdGame.getGameName());
        try {
            newGame.addSpieler(new Spieler(newGame, createdGame.getPlayerName()));
        } catch (AddSpielerException e) {
            e.printStackTrace();
        }
        this.sessions.add(newGame);
        return newGame;
    }

    @RequestMapping(value = "/sessions/{id}/start", method = RequestMethod.POST)
    public Spiel startGame(@PathVariable("id") String id) {
        Spiel game = (Spiel) sessions.stream()
                .filter(x -> x.toString().equals(id))
                .findFirst()
                .orElse(null);

        if(game != null) {
            game.starten();
        }
        return game;
    }

    @RequestMapping(value = "/sessions/{id}/join", method = RequestMethod.POST)
    public boolean joinGame(@PathVariable("id") String id, @RequestBody String name) throws AddSpielerException {
        Spiel game = (Spiel) sessions.stream()
                .filter(x -> x.toString().equals(id))
                .findFirst()
                .orElse(null);

        if(game != null) {
            Spieler a = new Spieler(game, name);
            game.addSpieler(a);
            return true;
        }

        return false;
    }

    @RequestMapping(value = "/sessions/{id}/{player}/cards", method = RequestMethod.GET)
    public Hand getHand(@PathVariable("id") String id, @PathVariable("player") String player) {
        return Arrays.asList(sessions.stream().filter((x -> x.toString().equals(id)))
                .findFirst()
                .orElse(null)
                .getSpielerliste())
                .stream()
                .filter(x -> x.getName().equals(player))
                .findFirst()
                .orElse(null)
                .getHand();
    }
}
