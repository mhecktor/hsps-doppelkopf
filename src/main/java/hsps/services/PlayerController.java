package hsps.services;

import hsps.services.exception.NotAValidCardException;
import hsps.services.exception.NotYourTurnException;
import hsps.services.exception.YouDontHaveThatCardException;
import hsps.services.logic.basic.Spiel;
import hsps.services.logic.basic.Stich;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.player.Hand;
import hsps.services.logic.player.Spieler;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/players/{gameId}/{playerId}")
public class PlayerController {
    @Autowired
    SessionController sessionController;

    @RequestMapping("/hand")
    public Hand getHand(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId) {
        Spiel game = sessionController.session(gameId);
        return Arrays.asList(game.getSpielerListe())
                .stream()
                .filter(x -> x.getName().equals(playerId))
                .findFirst()
                .orElse(null)
                .getHand();
    }

    @RequestMapping("/playCard")
    public void playCard(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId, @RequestBody() Karte card) throws NotAValidCardException, YouDontHaveThatCardException, NotYourTurnException {
        Spiel game = sessionController.session(gameId);
        Spieler player = Arrays.asList(game.getSpielerListe())
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
        player.setChosenCard( playersCard );
        player.performTurn();
        MqttService.publisher.publishData(new Message(MessageType.CardPlayed, card), String.format("/%s/cardPlayed", gameId));
        //game.spielzugAusfuehren(player, playersCard);
        System.out.println(player.getHand().getKarten().size());
    }

    @RequestMapping("/performDecision")
    public void performDecision(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId, @RequestBody() Boolean decision) {
        Spiel game = sessionController.session(gameId);
        Spieler player = Arrays.asList(game.getSpielerListe())
                .stream()
                .filter(x -> x.getName().equals(playerId))
                .findFirst()
                .orElse(null);
        player.performDecisionRule(decision);
    }

    @RequestMapping("/performAnnouncement")
    public void performAnnouncement(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId, @RequestBody() Boolean decision) {
        Spiel game = sessionController.session(gameId);
        Spieler player = Arrays.asList(game.getSpielerListe())
                .stream()
                .filter(x -> x.getName().equals(playerId))
                .findFirst()
                .orElse(null);
        player.performDecisionAnnouncement(decision);
    }

    @RequestMapping(value = "/stichs", method = RequestMethod.GET )
    public List<Stich> stichs(@PathVariable("gameId") String gameId, @PathVariable("playerId") String playerId) {
        Spiel game = sessionController.session(gameId);
        Spieler player = Arrays.asList(game.getSpielerListe())
                .stream()
                .filter(x -> x.getName().equals(playerId))
                .findFirst()
                .orElse(null);
        return player.getGesammelteStiche();
    }
}
