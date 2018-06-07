package hsps.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hsps.services.exception.AddSpielerException;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.player.Spieler;
import hsps.services.model.CreateGameJson;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private List<Spiel> sessions = new ArrayList<>();

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Spiel> sessions(@RequestParam(name = "name", required = false, defaultValue = "") String name) {
        return sessions
                .stream()
                .filter(
                        x -> x.getSpielID()
                                .contains(name))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Spiel session(@PathVariable("id") String name) {
        return sessions
                .stream()
                .filter(
                        x -> x.getSpielID()
                                .equals(name))
                .findFirst().orElse(null);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
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

    @RequestMapping(value = "/{id}/start", method = RequestMethod.POST)
    public Spiel startGame(@PathVariable("id") String id) {
        Spiel game = this.session(id);

        if(game != null) {
            game.starten();
        }
        return game;
    }

    @RequestMapping(value = "/{id}/pause")
    public Spiel pauseGame(@PathVariable("id") String id) {
        Spiel game = this.session(id);
        game.pausieren();
        return game;
    }

    @RequestMapping(value = "/{id}/join", method = RequestMethod.POST)
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
}
