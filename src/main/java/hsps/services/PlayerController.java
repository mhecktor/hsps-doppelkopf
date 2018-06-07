package hsps.services;

import hsps.services.exception.NotAValidCardException;
import hsps.services.exception.NotYourTurnException;
import hsps.services.exception.YouDontHaveThatCardException;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.player.Hand;
import hsps.services.logic.player.Spieler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/players/{gameId}/{playerId}")
public class PlayerController {
    @Autowired
    SessionController sessionController;

    @RequestMapping("/hand")
    public Hand getHand(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId) {
        Spiel game = sessionController.session(gameId);
        return Arrays.asList(game.getSpielerliste())
                .stream()
                .filter(x -> x.getName().equals(playerId))
                .findFirst()
                .orElse(null)
                .getHand();
    }

    @RequestMapping("/playCard")
    public void playCard(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId, @RequestBody() Karte card) throws NotAValidCardException, YouDontHaveThatCardException, NotYourTurnException {
        Spiel game = sessionController.session(gameId);
        Spieler player = Arrays.asList(game.getSpielerliste())
                .stream()
                .filter(x -> x.getName().equals(playerId))
                .findFirst()
                .orElse(null);
        if(!player
                .getHand()
                .getKarten()
                .stream()
                .filter(x ->
                        x.getSymbolik().toString().equals(card.getSymbolik().toString())
                        && x.getFarbwert().compareTo(card.getFarbwert()) == 0 ).findAny().isPresent()) {
            throw new YouDontHaveThatCardException();
        }
        Karte playersCard = player
                .getHand()
                .getKarten()
                .stream()
                .filter(x ->
                        x.getSymbolik().toString().equals(card.getSymbolik().toString())
                                && x.getFarbwert().compareTo(card.getFarbwert()) == 0 ).findFirst().orElse(null);
        game.spielzugAusfuehren(player, playersCard);
        System.out.println(player.getHand().getKarten().size());
    }
}
