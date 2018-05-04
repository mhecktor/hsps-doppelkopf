package hsps.services.model;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SessionController {

    private List<Session> sessions = new ArrayList<Session>();

    @RequestMapping("/sessions")
    public List<Session> sessions(@RequestParam(name="name", required=false, defaultValue="Meine") String name) {
        return sessions;
    }

    @RequestMapping(value = "/sessions", method= RequestMethod.POST)
    public Session createSession(@RequestBody Session session) {
        this.sessions.add(session);
        return session;
    }
}
