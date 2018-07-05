package hsps.services;

import hsps.services.exception.AddSpielerException;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.player.Spieler;
import hsps.services.model.CreateGameJson;
import hsps.services.model.Rules;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.test.TestProgramm;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sessions")
public class SessionController {

    private List<Spiel> sessions = new ArrayList<>();

    public final boolean DEBUG = true;

    @PostConstruct
    public void init() throws AddSpielerException {
        if(DEBUG) {
            Spiel demoGame = new Spiel("DEMO");
            TestProgramm.setRules(demoGame);
            String[] playerNames = new String[] {"Franz", "Gustav", "Dieter", "Rolf"};
            for (String playerName : playerNames) {
                demoGame.addSpieler(new Spieler(demoGame, playerName));
            }
            sessions.add(demoGame);
        }
    }


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
    public Spiel createSession(@RequestBody CreateGameJson createdGame) throws Exception {
        if (sessions.stream().filter((x) -> x.getSpielID().equals(createdGame.getGameName())).findAny().isPresent()) {
            throw new Exception("Spiel existiert bereit");
        }
        Spiel newGame = new Spiel(createdGame.getGameName());
        try {
            newGame.setRules(createdGame.getRules());
            newGame.addSpieler(new Spieler(newGame, createdGame.getPlayerName()));
        } catch (AddSpielerException e) {
            e.printStackTrace();
        }
        this.sessions.add(newGame);
//        MqttService.publisher.publishData(new Message(MessageType.GameCreated, newGame), "/gameCreated");
        return newGame;
    }

    @RequestMapping(value = "/{id}/start", method = RequestMethod.POST)
    public Spiel startGame(@PathVariable("id") String id) {
        Spiel game = this.session(id);

        if (game != null) {
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

        if (game != null) {
            Spieler a = new Spieler(game, name);
            game.addSpieler(a);
            return true;
        }

        return false;
    }
}
