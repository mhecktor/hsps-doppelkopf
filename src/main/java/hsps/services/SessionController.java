import hsps.services.logic.basic.Spiel;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SessionController {

    private List<Spiel> sessions = new ArrayList<>();

    @RequestMapping("/sessions")
    public List<Spiel> sessions(@RequestParam(name = "name", required = false, defaultValue = "Session") String name) {
        return sessions;
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.POST)
    public Spiel createSession(@RequestBody String name) {
        Spiel newGame = new Spiel(name);
        this.sessions.add(newGame);
        return newGame;
    }

    @RequestMapping(value = "/session/{id}/start", method = RequestMethod.POST)
    public Spiel startGame(@PathParam("id") String id) {
        return sessions.stream()
                .filter(x -> x.getSpielId() == id)
                .findFirst()
                .orElse(null);
    }
}
