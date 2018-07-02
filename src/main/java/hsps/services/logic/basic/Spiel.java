package hsps.services.logic.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import hsps.services.MqttService;
import hsps.services.exception.AddSpielerException;
import hsps.services.hibernate.DBSpieler;
import hsps.services.hibernate.DBStatistik;
import hsps.services.hibernate.DatabaseService;
import hsps.services.logic.basic.abstr.AbstractGameState;
import hsps.services.logic.basic.abstr.AbstractRoundBasedGame;
import hsps.services.logic.basic.conditions.EndingCondition;
import hsps.services.logic.basic.conditions.RuleCondition;
import hsps.services.logic.cards.Farbwert;
import hsps.services.logic.cards.Karte;
import hsps.services.logic.cards.Symbolik;
import hsps.services.logic.player.Spieler;
import hsps.services.logic.rules.Armut;
import hsps.services.logic.rules.Rule;
import hsps.services.logic.rules.Schweinchen;
import hsps.services.logic.rules.announcements.Announcement;
import hsps.services.logic.rules.announcements.DecisionAnnouncement;
import hsps.services.logic.rules.announcements.Pflichtansage;
import hsps.services.logic.rules.decision.DecisionRule;
import hsps.services.logic.rules.decision.KoenigSolo;
import hsps.services.logic.rules.decision.Schmeissen;
import hsps.services.logic.rules.stich.StichRule;
import hsps.services.logic.rules.stich.StichRuleNormal;
import hsps.services.logic.rules.stich.StichRuleZweiteDulle;
import hsps.services.model.Rules;
import hsps.services.mqtt.Message;
import hsps.services.mqtt.MessageType;
import hsps.services.mqtt.Topic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Spiel extends AbstractRoundBasedGame {

    public final static boolean SYSTEM = true;
    public final static boolean DEBUG = false;

    @JsonIgnore
    protected List<Karte> kartenSpiel;

    @JsonIgnore
    protected List<StichRule> stichRules;

    protected int currentRoundIndex;

    @JsonIgnore
    protected Spieler[] spielerListe = new Spieler[4];
    @JsonProperty("spieler")
    protected String[] spielerNamen = new String[4];
    protected int anzahlSpieler = 0;
    protected int maxRundenZahl;
    protected int startPlayer = 0;

    @JsonIgnore
    public List<Rule> rules;
    public int ruleIndex = 0;
    public Rule currentRule;

    @JsonIgnore
    public List<DecisionRule> decisionRules;
    public int decisionRuleIndex = 0;
    public DecisionRule currentDecisionRule;

    @JsonIgnore
    public List<Announcement> announcements;

    @JsonIgnore
    public int decisionAnnouncementIndex = 0;
    public DecisionAnnouncement currentDecisionAnnouncement;
    public List<DecisionAnnouncement> decisionAnnouncements;

    protected Stich stich;
    private int playerTurns = 0;
    public boolean validCard = true;

    public Spiel(String spielID) {
        super(spielID);

        aktZustand = new Initialisierend(this);

        this.rules = new ArrayList<Rule>();
        this.stichRules = new ArrayList<StichRule>();
        this.decisionRules = new ArrayList<DecisionRule>();
        this.announcements = new ArrayList<Announcement>();
        this.decisionAnnouncements = new ArrayList<DecisionAnnouncement>();
        // decisionAnnouncements.add( new ReContraAnsage() );

        stichRules.add(0, new StichRuleNormal());

        initConditions();
    }

    private void initConditions() {
        conditions.add(new RuleCondition(AbstractGameState.PerformGame, this));
        conditions.add(new EndingCondition(AbstractGameState.PostTurn, this));
        // conditions.add( new PostAnsagenCondition( AbstractGameState.PostTurn,
        // this ) );
    }

    // Initialisierungen um das Spiel spaeter leichter neustarten zu koennen

    private void resetKartenspiel() {
        kartenSpiel = new ArrayList<Karte>();
        for (int i = 0; i < 2; i++)
            for (Symbolik s : Symbolik.values())
                for (Farbwert f : Farbwert.values())
                    kartenSpiel.add(new Karte(f, s));
        maxRundenZahl = kartenSpiel.size() / spielerListe.length;
    }

    private void resetSpielerkarten() {
        for (Spieler s : spielerListe)
            s.getHand().resetKarten();
    }

    private void resetIndexe() {
        decisionRuleIndex = 0;
        ruleIndex = 0;
        decisionAnnouncementIndex = 0;
        startPlayer = 0;
        playerTurns = 0;
    }

    @Override
    protected synchronized void preGame() {
        resetKartenspiel();
        resetSpielerkarten();
        resetIndexe();

        int kartenAnzahl = kartenSpiel.size();
        int spielerNr = 0;
        while (kartenAnzahl > 0) {
            Karte k = kartenSpiel.remove((int) (Math.random() * kartenAnzahl--));
            spielerListe[spielerNr].getHand().addKarte(k);

            // Sende Karte an den Spieler
            MqttService.publisher.publishData(new Message(MessageType.GetCard, k), Topic.genPlayerTopic(getSpielID(), spielerListe[spielerNr].getName()));

            spielerNr = (spielerNr + 1) % spielerListe.length;
        }

        setAktuellerZustand(new Laufend(this));

        continueGame = true;
    }

    @Override
    protected void performGame() {
        while (decisionRuleIndex < decisionRules.size()) {
            currentDecisionRule = decisionRules.get(decisionRuleIndex++);
            if (currentDecisionRule.test(this)) {
                currentDecisionRule.inform();
                return;
            }
        }

        continueGame = true;

        while (ruleIndex < rules.size()) {
            currentRule = rules.get(ruleIndex++);
            if (currentRule.test(this)) {
                currentRule.perform();
                return;
            }
        }

    }

    @Override
    protected void postGame() {
        beenden();
        auswerten();
    }

    @Override
    protected void preTurn() {
        validCard = true;

        while (decisionAnnouncementIndex < decisionAnnouncements.size()) {
            currentDecisionAnnouncement = decisionAnnouncements.get(decisionAnnouncementIndex++);
            if (currentDecisionAnnouncement.test(this)) {
                currentDecisionAnnouncement.perform();
                return;
            }
        }
    }

    @Override
    protected void performTurn() {
        Spieler spieler = getCurrentSpieler();

        if (stich == null) {
            stich = new Stich(spieler, spieler.getChosenCard());

        } else {
            // Wenn die Karte gueltig war, dann entferne die Karte aus der Hand
            // von dem Spieler und pruefe, wem der Stich nun gehoert
            // Falls nicht gueltig, informiere den Spieler erneut, dass er eine
            // Karte aussuchen solls
            if (pruefeGueltigkeit(spieler, spieler.getChosenCard())) {
                stich.addKarte(spieler.getChosenCard());

                // Die Pruefung, wem der Stich gehoert kann eventuell wieder mit
                // einem Muster vollzogen werden (siehe Notizen)
                // TODO hier gibt es noch Fehler!!!
                boolean aendereZugehoerigkeit = false;
                for (StichRule sr : stichRules) {
                    if (sr.changeBelonging(stich, spieler.getChosenCard())) {
                        aendereZugehoerigkeit = true;
                        break;
                    }
                }

                if (aendereZugehoerigkeit) {
                    stich.setHoechsteKarte(spieler.getChosenCard());
                    stich.setSpieler(spieler);
                }

                validCard = true;

                MqttService.publisher.publishData(new Message(MessageType.ValidCard, spieler.getChosenCard()), Topic.genPlayerTopic(spielID, spieler.getName()));
            } else {
                if (Spiel.DEBUG) System.out.println("!!!--> Ausgewaehlte Karte war nicht gueltig!!!");

                validCard = false;
                MqttService.publisher.publishData(new Message(MessageType.InvalidCard, spieler.getChosenCard()), Topic.genPlayerTopic(spielID, spieler.getName()));
            }
        }

        continueGame = true;
    }

    @Override
    protected void postTurn() {
        int oldRoundIndex = currentRoundIndex;

        if (validCard) {
            getCurrentSpieler().getHand().removeKarte(getCurrentSpieler().getChosenCard());
            playerTurns++;
        }

        if (playerTurns % spielerListe.length == 0) {
            currentRoundIndex++;
            Spieler tSpieler = stich.getSpieler();
            tSpieler.addStich(stich);
            MqttService.publisher.publishData(new Message(MessageType.PlayerGotStich, tSpieler.getName()), String.format("/%s/playerGotStich", this.spielID));
            stich = null;
            startPlayer = getSpielerNr(tSpieler);
            playerTurns = 0;
        }

        if (oldRoundIndex != currentRoundIndex) {
            for (Announcement a : announcements)
                if (a.test(this)) a.perform();
        }

        continueGame = true;
    }

    @Override
    @JsonIgnore
    public synchronized Spieler getCurrentSpieler() {
        return spielerListe[(startPlayer + playerTurns) % spielerListe.length];
    }

    @JsonProperty("currentSpielerName")
    public synchronized String getCurrentSpielerName() {
        return spielerListe[(startPlayer + playerTurns) % spielerListe.length].getName();
    }

    /*
     * TODO Beim Erzeugen des Spiels koennen die Regeln gesetzt werden
     */
    public void setRules(Rules rules) {
        if (rules.isArmut()) this.rules.add(new Armut());

        if (rules.isSchweinchen()) this.rules.add(new Schweinchen());

        if (rules.isKoenigsSolo()) this.decisionRules.add(new KoenigSolo());

        if (rules.isSchmeissen()) this.decisionRules.add(new Schmeissen());

        if (rules.isPflichtAnsage()) this.announcements.add(new Pflichtansage());

        if (rules.isZweiteDulle()) stichRules.add(0, new StichRuleZweiteDulle());
    }

    public void performDecisionRule() {
        currentDecisionRule.perform();
    }

    public void performDecisionAnnouncement() {
        currentDecisionAnnouncement.perform();
    }

    public void addSpieler(Spieler spieler) throws AddSpielerException {
        if (aktZustand.getState() == State.INITIALISIEREND) {
            if (anzahlSpieler < spielerListe.length) {
                MqttService.publisher.publishData(new Message(MessageType.PlayerTopic, spielID + anzahlSpieler), Topic.genPlayerTopic(spielID, spieler.getName()));
                MqttService.publisher.publishData(new Message(MessageType.PlayerJoinedGame, spieler.getName()), String.format("/%s/playerJoined", spielID));
                spielerListe[anzahlSpieler++] = spieler;
                spielerNamen[anzahlSpieler - 1] = spieler.getName();
                if (anzahlSpieler == 4) {
                    MqttService.publisher.publishData(new Message(MessageType.LastPlayerJoined, spieler.getName()), String.format("/%s/lastPlayerJoined", spielID));
                }
            } else {
                throw new AddSpielerException("Es befinden sich bereits 4 Spieler im Spiel. Der Spieler wurde daher nicht aufgenommen!");
            }
        } else {
            throw new AddSpielerException("Das Spiel laeuft bereits. Der Spieler wurde daher nicht mit aufgenommen!");
        }
    }

    public int getSpielerNr(Spieler spieler) {
        int a = 0;
        while (spieler != spielerListe[a])
            a++;
        return a;
    }

    // Methode von Schulte
    public boolean pruefeGueltigkeit(Spieler spieler, Karte karte) {
        if (stich == null)
            return true;
        if (stich.getErsteKarte().isTrumpf()) {
            if (karte.isTrumpf()) {
                return true;
            } else {
                for (int i = 0; i < spieler.getHand().getKarten().size(); i++) {
                    if (spieler.getHand().getKarten().get(i).isTrumpf() == true) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            if (karte.isTrumpf()) {
                for (int i = 0; i < spieler.getHand().getKarten().size(); i++) {
                    if ((spieler.getHand().getKarten().get(i).getFarbwert() == stich.getErsteKarte().getFarbwert()) && (!spieler.getHand().getKarten().get(i).isTrumpf())) {
                        return false;
                    }
                }
                return true;

            } else {
                if (karte.getFarbwert() == stich.getErsteKarte().getFarbwert()) {
                    return true;
                } else {
                    for (int i = 0; i < spieler.getHand().getKarten().size(); i++) {
                        if (spieler.getHand().getKarten().get(i).getFarbwert() == stich.getErsteKarte().getFarbwert()) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
    }

    private void auswerten() {
        int punkteRe = 0;
        int punkteContra = 0;
        boolean siegRe = false;

        for (Spieler s : spielerListe) {
            if (s.isRe()) {
                punkteRe += s.getStichpunkte();
            } else {
                punkteContra += s.getStichpunkte();
            }
        }

        if (punkteRe > punkteContra) {
            System.out.println("Re gewinnt");
            siegRe = true;
        } else {
            System.out.println("Contra gewinnt");
        }

        for (Spieler s : spielerListe) {
            if (s.isRe()) {
                if (siegRe) {
                    s.getStatistik().setSiege(s.getStatistik().getSiege() + 1);
                    s.getStatistik().setPunkte(s.getStatistik().getPunkte() + 1);
                } else {
                    s.getStatistik().setPunkte(s.getStatistik().getPunkte() - 1);
                }
            } else {
                if (!siegRe) {
                    s.getStatistik().setSiege(s.getStatistik().getSiege() + 1);
                    s.getStatistik().setPunkte(s.getStatistik().getPunkte() + 1);
                } else {
                    s.getStatistik().setPunkte(s.getStatistik().getPunkte() - 1);
                }
            }
        }

        saveToDatabase(punkteRe, punkteContra, siegRe);
    }

    private void saveToDatabase(int punkteRe, int punkteContra, boolean siegRe) {
        DBStatistik dbStatistik = new DBStatistik();
        dbStatistik.setCreateDateTime(LocalDateTime.now());
        dbStatistik.setPunktestandKontra(punkteContra);
        dbStatistik.setPunktestandRe(punkteRe);
        dbStatistik.setReGewonnen(siegRe ? 'y' : 'n');
        DatabaseService.saveObject(dbStatistik);

        for (Spieler s : spielerListe) {
            DBSpieler dbSpieler = new DBSpieler();
            dbSpieler.setName(s.getName());
            System.err.println("--------->" + s.isRe());
            dbSpieler.setRe(s.isRe() ? 'y' : 'n');
            dbSpieler.setDbStatistik(dbStatistik);
            dbStatistik.getSpielerliste().add(dbSpieler);
            DatabaseService.saveObject(dbSpieler);
        }
    }

    public void starten() {
        if (anzahlSpieler < spielerListe.length) return;
        wiederaufnehmen();
        gameState = AbstractGameState.PreGame;
        this.next();
    }

    public void neustarten() {
        if (aktZustand.getState() != State.LAUFEND && aktZustand.getState() != State.PAUSIEREND) return;
        if (Spiel.SYSTEM) System.out.println("Spiel wird neugestartet...");
        MqttService.publisher.publishData(new Message(MessageType.RestartGame));
        // beenden();
        // initialisieren();
        // wiederaufnehmen();
        gameState = AbstractGameState.RestartingGame;
        next();
    }

    public List<Karte> getKartenSpiel() {
        return kartenSpiel;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<DecisionRule> getDecisionRules() {
        return decisionRules;
    }

    public List<StichRule> getStichRules() {
        return stichRules;
    }

    public int getCurrentRoundIndex() {
        return currentRoundIndex;
    }

    public Spieler[] getSpielerListe() {
        return spielerListe;
    }

    public int getAnzahlSpieler() {
        return anzahlSpieler;
    }

    public int getMaxRundenZahl() {
        return maxRundenZahl;
    }

    public int getStartPlayer() {
        return startPlayer;
    }

    public Stich getStich() {
        return stich;
    }
}
