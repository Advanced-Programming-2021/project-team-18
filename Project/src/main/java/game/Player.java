package game;

import card.*;
import data.Printer;
import effects.Effect;
import events.*;
import lombok.Getter;
import lombok.Setter;
import menus.Menu;
import utility.Utility;

import java.util.HashMap;
import java.util.regex.Matcher;

@Getter
@Setter
public class Player {

    private static final String regexSelect = "select.+";
    private static final String regexShowGraveyard = "show\\sgraveyard";
    private static final String regexShowSelectedCard = "card\\sshow\\s\\-\\-selected";
    private static final String regexSummon = "summon";
    private static final String regexSet = "set";
    private static final String regexChangePosition = "set\\s--position\\s(.+)";
    private static final String regexFlipSummon = "flip\\-summon";
    private static final String regexAttackNormal = "attack\\s(\\d+)";
    private static final String regexAttackDirect = "attack\\sdirect";
    private static final String regexActivateEffect = "activate\\seffect";
    private static final String regexNextPhase = "next\\sphase";
    private static final String SUCCESSFUL_SUMMON = "summoned successfully";
    private static final String regexForfeit = "surrender";
    private static final String regexIncreaseLifePoint = "increase\\s--LP\\s(\\d+)";
    private static final String regexSetDuelWinner = "duel\\sset-winner\\s(.+)";
    private static final int HAND_SIZE = 7;
    @Getter
    private static final int FIELD_SIZE = 5;

    // Initialized in constructor
    private User user;
    private Deck graveyard;
    private Deck remainingDeck;
    private MonsterCard[] monstersFieldList;
    private Card[] spellsAndTrapFieldList;
    private SpellCard fieldZone;
    private Deck hand;
    private int lifePoint;
    private boolean loser;
    private Card selectedCard;
    private boolean hasSummonedMonsterThisTurn; // has to be reset at end phase
    private Card theSummonedMonsterThisTurn;
    // Initialized by setters
    private Game game;
    private Player opponent;


    public Player(User user, Deck deck) {
        this.user = user;
        graveyard = new Deck(105);
        this.remainingDeck = deck;
        monstersFieldList = new MonsterCard[FIELD_SIZE + 1]; // To have 1-based indexing
        spellsAndTrapFieldList = new Card[FIELD_SIZE + 1];   // To have 1-based indexing
        fieldZone = null;
        hand = new Deck(HAND_SIZE);
        lifePoint = 8000;
        loser = false;
        selectedCard = null;
        hasSummonedMonsterThisTurn = false;
    }

    private void increaseLifePoint(Matcher matcher) {
        int amount = Integer.parseInt(matcher.group(1));
        this.lifePoint += amount;
    }

    // Returns true iff the decrement action has been done successfully
    public boolean decreaseLifePoint(int amount, Card causedCard) {
        // event : [lifepoint change]
        LifePointChangeEvent lifePointChangeEvent = new LifePointChangeEvent(this, causedCard, -amount);
        if (!getPermissionFromAllEffects(lifePointChangeEvent)) {
            Printer.prompt("lifepoint won't change");
            return false;
        }
        notifyAllEffectsForConsideration(lifePointChangeEvent);
        lifePoint -= amount;
        if (lifePoint < 0) lifePoint = 0;
        if (lifePoint == 0) loser = true;
        return true;
    }

    private void setDuelWinner(Matcher matcher) {
        String nickName = matcher.group(1);
        if (this.getUser().getNickname().equals(nickName)) {
            this.getOpponent().setLoser(true);
        } else {
            this.setLoser(true);
        }
    }

    public void runCommonCommands(String command) {
        Matcher matcher;
        if (Utility.getCommandMatcher(command, regexSelect).matches()) {
            selectCard(command);
        } else if (Utility.getCommandMatcher(command, regexShowGraveyard).matches()) {
            showGraveyard();
        } else if (Utility.getCommandMatcher(command, regexShowSelectedCard).matches()) {
            showSelectedCard();
        } else if ((matcher = Utility.getCommandMatcher(command, regexIncreaseLifePoint)).matches()) {
            increaseLifePoint(matcher);
        } else if ((matcher = Utility.getCommandMatcher(command, regexSetDuelWinner)).matches()) {
            setDuelWinner(matcher);
        } else if (command.equals(regexForfeit)) {
            forfeit();
        }
    }

    public void runMainPhaseCommands(String command) {
        Matcher matcher;
        if (Utility.getCommandMatcher(command, regexSummon).matches()) {
            summonMonster();
        } else if (Utility.getCommandMatcher(command, regexSet).matches()) {
            if (selectedCard == null) {
                Printer.prompt("no card is selected yet");
                return;
            }
            if (selectedCard instanceof MonsterCard) setMonster();
            else if (selectedCard instanceof SpellCard) setSpellOrTrap();
        } else if ((matcher = Utility.getCommandMatcher(command, regexChangePosition)).matches()) {
            changeMonsterPosition(matcher);
        } else if (Utility.getCommandMatcher(command, regexFlipSummon).matches()) {
            flipSummon();
        } else if (Utility.getCommandMatcher(command, regexActivateEffect).matches()) {
            activateEffect();
        }
    }

    public void runBattlePhaseCommands(String command) {
        Matcher matcher;
        if ((matcher = Utility.getCommandMatcher(command, regexAttackNormal)).matches()) {
            attack(matcher);
        } else if (Utility.getCommandMatcher(command, regexAttackDirect).matches()) {
            attackDirect();
        } else if (Utility.getCommandMatcher(command, regexActivateEffect).matches()) {
            activateEffect();
        }
    }

    // By Sina
    public void drawPhase() {
        Printer.prompt("phase: draw phase");
        if (!game.isFirstTurn()) {
            if (remainingDeck.isEmpty()) {
                Printer.prompt("No card is remained in your deck");
                loser = true;
                return;
            }
            Card newCard = remainingDeck.pop();
            DrawCardEvent drawCardEvent = new DrawCardEvent(newCard);
            if (!getPermissionFromAllEffects(drawCardEvent)) {
                remainingDeck.addCard(newCard);
                return;
            }
            notifyAllEffectsForConsideration(drawCardEvent);
            hand.addCard(newCard);
            Printer.prompt("new card added to the hand: " + newCard.getCardName());
        }
        PhaseChangeEvent phaseChangeEvent = new PhaseChangeEvent(Phase.DRAW , this);
        notifyAllEffectsForConsideration(phaseChangeEvent);
    }

    public void standbyPhase() {
        Printer.prompt("phase: standby phase");
        PhaseChangeEvent phaseChangeEvent = new PhaseChangeEvent(Phase.STANDBY , this);
        notifyAllEffectsForConsideration(phaseChangeEvent);
    }

    //      by Pasha
    public void mainPhase1() {
        Printer.prompt("phase: main phase 1");
        Printer.showBoard(this, this.opponent);
        while (!this.isLoser() && !opponent.isLoser()) {
            String command = Utility.getNextLine();
            if (Utility.getCommandMatcher(command, regexNextPhase).matches())
                break;
            runCommonCommands(command);
            runMainPhaseCommands(command);
        }

        PhaseChangeEvent phaseChangeEvent = new PhaseChangeEvent(Phase.MAIN1 , this);
        notifyAllEffectsForConsideration(phaseChangeEvent);
    }

    //          by Kamyar
    public void battlePhase() {
        Printer.prompt("phase: battle phase");
        if (game.isFirstTurn()) return;
        while (!this.isLoser() && !opponent.isLoser()) {
            String command = Utility.getNextLine();
            if (Utility.getCommandMatcher(command, regexNextPhase).matches())
                break;
            runCommonCommands(command);
            runBattlePhaseCommands(command);
        }

        PhaseChangeEvent phaseChangeEvent = new PhaseChangeEvent(Phase.BATTLE , this);
        notifyAllEffectsForConsideration(phaseChangeEvent);
    }

    //          by Pasha
    public void mainPhase2() {
        Printer.prompt("phase: main phase 2");
        while (!this.isLoser() && !opponent.isLoser()) {
            String command = Utility.getNextLine();
            if (Utility.getCommandMatcher(command, regexNextPhase).matches())
                break;
            runCommonCommands(command);
            runMainPhaseCommands(command);
        }
        PhaseChangeEvent phaseChangeEvent = new PhaseChangeEvent(Phase.MAIN2 , this);
        notifyAllEffectsForConsideration(phaseChangeEvent);
    }

    //         by KAMYAR
    public void endPhase() {
//      NOTE : for each monster card .hasAttacked has to be set to false and hasSummonedMonsterThisTurn should be also set to false
        Printer.prompt("phase: End phase");
        for (int i = 1; i <= FIELD_SIZE; i++) {
            if (monstersFieldList[i] != null) {
                monstersFieldList[i].setHasAttackedThisTurn(false);
                monstersFieldList[i].setHasChangedPositionThisTurn(false);
            }
        }
        hasSummonedMonsterThisTurn = false;
        theSummonedMonsterThisTurn = null;
        Printer.prompt("its " + opponent.getUser().getNickname() + "’s turn");
        PhaseChangeEvent phaseChangeEvent = new PhaseChangeEvent(Phase.END , this);
        notifyAllEffectsForConsideration(phaseChangeEvent);
    }

    public int getSelectedMonsterCardOnFieldID() {
        for (int i = 1; i <= FIELD_SIZE; ++i)
            if (selectedCard == monstersFieldList[i])
                return i;
        return -1;
    }

    public int getSelectedSpellOrTrapCardOnFieldID() {
        for (int i = 1; i <= FIELD_SIZE; ++i)
            if (selectedCard == spellsAndTrapFieldList[i])
                return i;
        return -1;
    }

    public int getSelectedCardOnHandID() {
        for (int i = 0; i < hand.getCardsList().size(); i++) {
            if (selectedCard == hand.getCardsList().get(i)) return i;
        }
        return -1;
    }

    public int getFirstEmptyPlaceOnMonstersField() {
        for (int i = 1; i <= FIELD_SIZE; i++) {
            if (monstersFieldList[i] == null) return i;
        }
        return -1;
    }

    public int getFirstEmptyPlaceOnSpellsField() {
        for (int i = 1; i <= FIELD_SIZE; i++) {
            if (spellsAndTrapFieldList[i] == null) return i;
        }
        return -1;
    }

    public int getMonsterPositionOnBoard(MonsterCard card) {
        for (int i = 1; i <= FIELD_SIZE; ++i)
            if (monstersFieldList[i] == card)
                return i;
        return -1;
    }

    public int getSpellOrTrapPositionOnBoard(Card card) {
        for (int i = 1; i <= FIELD_SIZE; ++i)
            if (spellsAndTrapFieldList[i] == card)
                return i;
        return -1;
    }

    // Returns true iff the card has actually been removed
    public boolean removeCardFromField(Card card, Card causedCard) {
        if (card == null) return false;

        // event : [card event]
        CardEvent cardEvent = new CardEvent(card, CardEventInfo.DESTROYED, causedCard);
        if (!getPermissionFromAllEffects(cardEvent)) {
            Printer.prompt("monster won't be destroyed");
            return false;
        }
        notifyAllEffectsForConsideration(cardEvent);
        for (int i = 1; i <= FIELD_SIZE; i++) {
            if (monstersFieldList[i] == card) {
                monstersFieldList[i] = null;
                graveyard.addCard(card);
            }
            if (spellsAndTrapFieldList[i] == card) {
                spellsAndTrapFieldList[i] = null;
                graveyard.addCard(card);
            }
        }
        if (card == fieldZone) {
            fieldZone = null;
            graveyard.addCard(card);
        }
        return true;
    }

    public void addMonsterCardToField(MonsterCard newCard) {
        if (monstersFieldList.length == FIELD_SIZE) return;
        monstersFieldList[getFirstEmptyPlaceOnMonstersField()] = newCard;
    }

    public boolean removeCardFromHand(Card card) {
        if (hand.removeCard(card)) {
            graveyard.addCard(card);
            return true;
        }
        return false;
    }

    public void removeCardFromDeck(Card card) {
        if (remainingDeck.removeCard(card)) {
            graveyard.addCard(card);
        }
    }

    public void flipMonsterOnDefense(MonsterCard monsterCard, Card causedByCard) {
        CardEvent cardEvent = new CardEvent(monsterCard, CardEventInfo.FLIP, causedByCard);
        if (getPermissionFromAllEffects(cardEvent)) {
            monsterCard.setFaceUp(true);
            notifyAllEffectsForConsideration(cardEvent);
        }
    }

    public void summonRitualMonster(MonsterCard monsterCard) {
        int placeOnBoard = getFirstEmptyPlaceOnMonstersField();
        if (placeOnBoard == -1) {
            Printer.prompt("no place for monster to be summoned");
            return;
        }
        monsterCard.setFaceUp(true);
        CardEvent cardEvent = new CardEvent(monsterCard, CardEventInfo.ENTRANCE, null);
        if (!getPermissionFromAllEffects(cardEvent)) return;
        monstersFieldList[placeOnBoard] = monsterCard;
        notifyAllEffectsForConsideration(cardEvent);
    }

    public Card obtainCardFromHand() {
        Printer.prompt("Your hand contains these cards: ");
        for (Card card : hand.getCardsList()) {
            Printer.showCard(card);
        }
        String response;
        int index = -1;
        while (!(0 <= index && index < hand.getSize())) {
            while (true) {
                Printer.prompt("Please select a card position on hand");
                Printer.prompt("(a number in range 1 to " + hand.getSize() + ")");
                response = Utility.getNextLine();
                if (response.matches("\\d")) break;
                Printer.prompt(Menu.INVALID_COMMAND);
            }
            index = Integer.parseInt(response) - 1;
        }
        return hand.getCardsList().get(index);
    }

    public boolean obtainConfirmation(String promptMassage) {
        String response;
        while (true) {
            Printer.prompt(promptMassage);
            response = Utility.getNextLine();
            if (response.equals("yes")) return true;
            if (response.equals("no")) return false;
            Printer.prompt(Menu.INVALID_COMMAND);
        }
    }

    public Card obtainCardFromDeck(boolean showDeck) {
        if (showDeck) {
            Printer.prompt("You deck contains these cards:");
            for (Card card : remainingDeck.getCardsList()) {
                Printer.showCard(card);
            }
        }
        String response;
        int index = -1;
        while (!(0 <= index && index < remainingDeck.getSize())) {
            while (true) {
                Printer.prompt("Please select a card position on deck");
                Printer.prompt("(a number in range 1 to " + remainingDeck.getSize() + ")");
                response = Utility.getNextLine();
                if (response.matches("\\d")) break;
                Printer.prompt(Menu.INVALID_COMMAND);
            }
            index = Integer.parseInt(response) - 1;
        }
        return remainingDeck.getCardsList().get(index);
    }

    public Card obtainCardFromGraveYard() {
        showGraveyard();
        Printer.prompt("Enter the number of the monster card which you want to transform scanner to:");
        String input = Utility.getNextLine();
        int size = graveyard.getSize();
        while (!input.matches("\\d{1,4}")
                || Integer.parseInt(input) > size
                || Integer.parseInt(input) < 1) {
            Printer.prompt("Invalid input\nPlease try again.");
            input = Utility.getNextLine();
        }
        int index = Integer.parseInt(input);
        return graveyard.getCardsList().get(index);
    }

    // by Kamyar
    public void selectCard(String command) {
        if (command.matches("select -d")) {
            if (selectedCard == null) {
                Printer.prompt("no card is selected yet");
                return;
            }
            Printer.prompt("card deselected");
            selectedCard = null;
            return;
        }
        HashMap<String, String> map = Utility.getCommand(command);
        if (map == null) {
            Printer.prompt(Menu.INVALID_COMMAND);
            return;
        }
        boolean isOpponentsCard = false;
        String place;
        int placeID;
        if (Utility.isCommandValid(map, new String[]{"monster"}, new String[]{"opponent"})) {
            if (map.containsKey("opponent")) {
                if (Utility.checkAndPrompt(!Utility.areAttributesValid(map, new String[]{"opponent"}, new String[]{"monster"}), "invalid selection"))
                    return;
                place = map.get("opponent");
                isOpponentsCard = true;
            } else {
                if (Utility.checkAndPrompt(!Utility.areAttributesValid(map, new String[]{"monster"}, null), "invalid selection"))
                    return;
                place = map.get("monster");
            }
            if (Utility.checkAndPrompt((!place.matches("\\d+")), "invalid selection")) return;
            placeID = Integer.parseInt(place);
            if (Utility.checkAndPrompt((placeID > FIELD_SIZE || placeID < 1), "invalid selection")) return;
            Card selectCandidateCard = (isOpponentsCard ? opponent.monstersFieldList[placeID] : monstersFieldList[placeID]);
            if (Utility.checkAndPrompt(selectCandidateCard == null, "no card found in the given position")) return;
            selectedCard = selectCandidateCard;
            Printer.prompt("card selected");
        } else if (Utility.isCommandValid(map, new String[]{"spell"}, new String[]{"opponent"})) {
            if (map.containsKey("opponent")) {
                if (Utility.checkAndPrompt(!Utility.areAttributesValid(map, new String[]{"opponent"}, new String[]{"spell"}), "invalid selection"))
                    return;
                place = map.get("opponent");
                isOpponentsCard = true;
            } else {
                if (Utility.checkAndPrompt(!Utility.areAttributesValid(map, new String[]{"spell"}, null), "invalid selection"))
                    return;
                place = map.get("spell");
            }
            if (Utility.checkAndPrompt((!place.matches("\\d+")), "invalid selection")) return;
            placeID = Integer.parseInt(place);
            if (Utility.checkAndPrompt((placeID > FIELD_SIZE || placeID < 1), "invalid selection")) return;
            Card selectCandidateCard = (isOpponentsCard ? opponent.spellsAndTrapFieldList[placeID] : spellsAndTrapFieldList[placeID]);
            if (Utility.checkAndPrompt(selectCandidateCard == null, "no card found in the given position")) return;
            selectedCard = selectCandidateCard;
            Printer.prompt("card selected");
        } else if (Utility.isCommandValid(map, new String[]{"field"}, new String[]{"opponent"})) {
            if (map.containsKey("opponent")) {
                if (Utility.checkAndPrompt(!Utility.areAttributesValid(map, new String[]{"opponent"}, new String[]{"field"}), "invalid selection"))
                    return;
                isOpponentsCard = true;
            } else {
                if (Utility.checkAndPrompt(!Utility.areAttributesValid(map, new String[]{"field"}, null), "invalid selection"))
                    return;
            }
            Card selectCandidateCard = (isOpponentsCard ? opponent.fieldZone : fieldZone);
            if (Utility.checkAndPrompt(selectCandidateCard == null, "no card found in the given position")) return;
            selectedCard = selectCandidateCard;
            Printer.prompt("card selected");
        } else if (Utility.isCommandValid(map, new String[]{"hand"}, null)) {
            if (Utility.checkAndPrompt(!Utility.areAttributesValid(map, new String[]{"hand"}, null), "invalid selection"))
                return;
            place = map.get("hand");
            if (Utility.checkAndPrompt((!place.matches("\\d+")), "invalid selection")) return;
            placeID = Integer.parseInt(place) - 1;
            if (Utility.checkAndPrompt((placeID >= hand.getCardsList().size() || placeID < 0), "invalid selection"))
                return;
            selectedCard = hand.getCardsList().get(placeID);
            Printer.prompt("card selected");

        } else {
            Printer.prompt("invalid selection");
        }

    }

    private boolean isMonsterAddressInvalid(int address) {
        if (address < 1) return true;
        if (address > FIELD_SIZE) return true;
        return monstersFieldList[address] == null;
    }

    // Used to summon monsters with level less than 5
    private void summonMonsterLowLevel(int placeOnHand, int placeOnField) {
        monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
        hand.removeCardAt(placeOnHand);
        hasSummonedMonsterThisTurn = true;
        selectedCard.setFaceUp(true);
        ((MonsterCard) selectedCard).setDefenseMode(false);
        theSummonedMonsterThisTurn = selectedCard;
    }

    // Used to summon monsters with level 5 or 6
    private void summonMonsterMediumLevel(int placeOnHand, int tributeAddress) {
        MonsterCard tributedMonster = monstersFieldList[tributeAddress];
        monstersFieldList[tributeAddress] = null;
        graveyard.getCardsList().add(tributedMonster);
        int placeOnField = getFirstEmptyPlaceOnMonstersField();
        monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
        hasSummonedMonsterThisTurn = true;
        theSummonedMonsterThisTurn = selectedCard;
        selectedCard.setFaceUp(true);
        ((MonsterCard) selectedCard).setDefenseMode(false);
        hand.removeCardAt(placeOnHand);
    }

    private void changeMonsterPosition(Matcher matcher) {
        String position = matcher.group(1);
        if (selectedCard == null) {
            Printer.prompt("no card is selected yet");
            return;
        }
        int cardId = getSelectedMonsterCardOnFieldID();
        if (cardId == -1) {
            Printer.prompt("you can't change this card position");
            return;
        }
        if ((monstersFieldList[cardId].isDefenseMode() && position.equals("defense")) || (!monstersFieldList[cardId].isDefenseMode() && position.equals("attack"))) {
            Printer.prompt("this card is already in the wanted position");
            return;
        }
        if (monstersFieldList[cardId].isHasChangedPositionThisTurn()) {
            Printer.prompt("you already changed this card position in this turn");
            return;
        }
        // maybe change position event ? not needed yet though
        monstersFieldList[cardId].setHasChangedPositionThisTurn(true);
        monstersFieldList[cardId].setDefenseMode(!monstersFieldList[cardId].isDefenseMode());
        Printer.prompt("monster card position changed successfully");
    }

    // Used to summon monsters with level greater than 6
    private void summonMonsterHighLevel(int firstTribute, int secondTribute, int placeOnHand) {
        MonsterCard firstTributedCard = monstersFieldList[firstTribute];
        MonsterCard secondTributedCard = monstersFieldList[secondTribute];
        monstersFieldList[firstTribute] = null;
        monstersFieldList[secondTribute] = null;
        graveyard.getCardsList().add(firstTributedCard);
        graveyard.getCardsList().add(secondTributedCard);
        hand.removeCardAt(placeOnHand);
        int placeOnField = getFirstEmptyPlaceOnMonstersField();
        monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
        hasSummonedMonsterThisTurn = true;
        selectedCard.setFaceUp(true);
        ((MonsterCard) selectedCard).setDefenseMode(false);
        theSummonedMonsterThisTurn = selectedCard;
    }

    // by Kamyar
    public void summonMonster() {
//      Note: This function should be modified later to support
        if (Utility.checkAndPrompt(selectedCard == null,
                "no card is selected yet")) return;
        int place = getSelectedCardOnHandID();
        if (Utility.checkAndPrompt(!(selectedCard instanceof MonsterCard) || place == -1,
                "you can’t summon this card")) return;
        int placeOnField = getFirstEmptyPlaceOnMonstersField();
        if (Utility.checkAndPrompt(placeOnField == -1,
                "monster card zone is full")) return;
        if (Utility.checkAndPrompt(hasSummonedMonsterThisTurn,
                "you already summoned/set on this turn")) return;
        // event : [summon]
        selectedCard.setFaceUp(true);
        CardEvent cardEvent = new CardEvent(selectedCard, CardEventInfo.ENTRANCE, null);
        if (!getPermissionFromAllEffects(cardEvent)) {
            Printer.prompt("you don't have permission to summon");
            return;
        }
        selectedCard.setFaceUp(true);
        int monsterLevel = ((MonsterCard) selectedCard).getCardLevel();
        if (monsterLevel <= 4) {
            summonMonsterLowLevel(place, placeOnField);
            Printer.prompt(SUCCESSFUL_SUMMON);
            notifyAllEffectsForConsideration(cardEvent);
            Printer.showBoard(this, this.opponent);
            return;
        }
        if (monsterLevel <= 6) {
            if (Utility.checkAndPrompt(placeOnField == 1,
                    "there are not enough cards for tribute")) return;
            Printer.prompt("input the address of the card you want to tribute:");
            int address = Integer.parseInt(Utility.getNextLine());
            if (Utility.checkAndPrompt(isMonsterAddressInvalid(address),
                    "there are no monsters on this address")) return;
            summonMonsterMediumLevel(place, address);
            Printer.prompt(SUCCESSFUL_SUMMON);
            notifyAllEffectsForConsideration(cardEvent);
            Printer.showBoard(this, this.opponent);
            return;
        }

        if (Utility.checkAndPrompt(placeOnField == 1 || placeOnField == 2,
                "there are not enough cards for tribute")) return;
        Printer.prompt("input two addresses for the cards you want to tribute in TWO DIFFERENT LINES:");
        int firstTribute = Integer.parseInt(Utility.getNextLine());
        int secondTribute = Integer.parseInt(Utility.getNextLine());
        if (Utility.checkAndPrompt(isMonsterAddressInvalid(firstTribute) || isMonsterAddressInvalid(secondTribute),
                "there is no monster on one of these addresses")) return;
        // Note: What if the two addresses above are the same?
        if (Utility.checkAndPrompt(firstTribute == secondTribute,
                "Tributes are the same!")) return;
        summonMonsterHighLevel(firstTribute, secondTribute, place);
        Printer.prompt(SUCCESSFUL_SUMMON);
        notifyAllEffectsForConsideration(cardEvent);
        Printer.showBoard(this, this.opponent);
    }

    // by Kamyar
    public void setMonster() {
        if (Utility.checkAndPrompt((selectedCard == null), "no card is selected yet")) return;
        int placeOnHand = getSelectedCardOnHandID();
        if (Utility.checkAndPrompt((placeOnHand == -1), "you can’t set this card")) return;
        int placeOnMonstersZone = getFirstEmptyPlaceOnMonstersField();
        if (Utility.checkAndPrompt((placeOnMonstersZone == -1), "monster card zone is full")) return;
        if (Utility.checkAndPrompt(hasSummonedMonsterThisTurn, "you already summoned/set on this turn")) return;
        // event : [summon]
        selectedCard.setFaceUp(false);
        CardEvent cardEvent = new CardEvent(selectedCard, CardEventInfo.ENTRANCE, null);
        if (!getPermissionFromAllEffects(cardEvent))
            return;

        hand.removeCardAt(placeOnHand);
        monstersFieldList[placeOnMonstersZone] = (MonsterCard) selectedCard;
        selectedCard.setFaceUp(false);
        hasSummonedMonsterThisTurn = true;
        ((MonsterCard) selectedCard).setDefenseMode(true);
        theSummonedMonsterThisTurn = selectedCard;
        Printer.prompt("set successfully");
        notifyAllEffectsForConsideration(cardEvent);
        Printer.showBoard(this, this.opponent);
    }

    // by Pasha
    public void flipSummon() {
        if (selectedCard == null) {
            Printer.prompt("no card is selected yet");
            return;
        }
        int cardID = getSelectedMonsterCardOnFieldID();
        if (cardID <= 0) {
            Printer.prompt("you can’t change this card position");
            return;
        }
        if (selectedCard.isFaceUp() || theSummonedMonsterThisTurn == selectedCard) {
            Printer.prompt("you can’t flip summon this card");
            return;
        }
        // event : [flip]
        CardEvent cardEvent = new CardEvent(selectedCard, CardEventInfo.FLIP, null);
        if (!getPermissionFromAllEffects(cardEvent)) {
            Printer.prompt("you can't flip your card");
            return;
        }
        selectedCard.setFaceUp(false);
        notifyAllEffectsForConsideration(cardEvent);
        Printer.prompt("flip summoned successfully");
        Printer.showBoard(this, this.opponent);
    }

    // by Pasha
    public void attack(Matcher matcher) {
        int positionToAttack = Integer.parseInt(matcher.group(1));

        if (selectedCard == null) {
            Printer.prompt("no card is selected yet");
            return;
        }
        int cardID = getSelectedMonsterCardOnFieldID();
        if (cardID <= 0) {
            Printer.prompt("you can’t attack with this card");
            return;
        }
        if (((MonsterCard) selectedCard).isHasAttackedThisTurn()) {
            Printer.prompt("this card already attacked");
            return;
        }
        if (((MonsterCard) selectedCard).isDefenseMode()) {
            Printer.prompt("can't attack with a defense position monster");
            return;
        }
        if (positionToAttack <= 0 || positionToAttack >= 6 || opponent.getMonstersFieldList()[positionToAttack] == null)
            return;
        // event : [Attack Event]
        AttackEvent attackEvent = new AttackEvent((MonsterCard) selectedCard, opponent.getMonstersFieldList()[positionToAttack]);
        if (!getPermissionFromAllEffects(attackEvent)) {
            Printer.prompt("you can't attack");
            return;
        }
        notifyAllEffectsForConsideration(attackEvent);
        ((MonsterCard) selectedCard).attackTo(opponent.getMonstersFieldList()[positionToAttack], this);
        Printer.showBoard(this, this.opponent);

    }

    // by Pasha
    public void attackDirect() {
        if (selectedCard == null) {
            Printer.prompt("no card is selected yet");
            return;
        }
        int cardID = getSelectedMonsterCardOnFieldID();
        if (cardID <= 0) {
            Printer.prompt("you can’t attack with this card");
            return;
        }
        if (((MonsterCard) selectedCard).isHasAttackedThisTurn()) {
            Printer.prompt("this card already attacked");
            return;
        }

        boolean doesOpponentHaveMonster = false;
        for (int i = 1; i <= FIELD_SIZE; ++i)
            if (opponent.getMonstersFieldList()[i] != null) {
                doesOpponentHaveMonster = true;
                break;
            }
        if (doesOpponentHaveMonster) {
            Printer.prompt("can't attack direct when opponent has at least a monster");
            return;
        }
        // event : [Attack Event]
        AttackEvent attackEvent = new AttackEvent((MonsterCard) selectedCard, null);
        if (!getPermissionFromAllEffects(attackEvent)) {
            Printer.prompt("you can't attack");
            return;
        }
        opponent.decreaseLifePoint(((MonsterCard) selectedCard).getCardAttack(), null);
        if (opponent.getLifePoint() <= 0)
            opponent.setLoser(true);
        ((MonsterCard) selectedCard).setHasAttackedThisTurn(true);
        notifyAllEffectsForConsideration(attackEvent);
        Printer.prompt("your opponent receives " + ((MonsterCard) selectedCard).getCardAttack() + " battle damage");
        Printer.showBoard(this, this.opponent);
    }

    // by Pasha
    public void activateEffect() {
        if (selectedCard == null) {
            Printer.prompt("no card is selected");
            return;
        }
        if (!(selectedCard instanceof SpellCard) && !(selectedCard instanceof TrapCard)) {
            Printer.prompt("activate effect is only for spells");
            return;
        }
        if (selectedCard.isFaceUp()) {
            Printer.prompt("you have already activated this card");
            return;
        }
        boolean isFieldSpell = (selectedCard instanceof SpellCard) && (((SpellCard) selectedCard).getCardSpellType() == SpellType.FIELD);
        if (isFieldSpell) {
            Printer.prompt("you can't activate field spells");
            return;
        }
        CardEvent activateCardEvent = new CardEvent(selectedCard, CardEventInfo.ACTIVATE_EFFECT, null);
        if (getSelectedCardOnHandID() != -1) {
            int firstEmptyPlace = getFirstEmptyPlaceOnSpellsField();
            if (firstEmptyPlace == -1) {
                Printer.prompt("there is no card space on board for you to activate your spell");
            } else {
                CardEvent entranceCardEvent = new CardEvent(selectedCard, CardEventInfo.ENTRANCE, null);
                if (!getPermissionFromAllEffects(entranceCardEvent) || !getPermissionFromAllEffects(activateCardEvent)) {
                    Printer.prompt("you cant activate this spell");
                    return;
                }
                notifyAllEffectsForConsideration(entranceCardEvent);
                spellsAndTrapFieldList[getFirstEmptyPlaceOnSpellsField()] = selectedCard;
            }
        }
        // event : [cardEvent]

        if (!getPermissionFromAllEffects(activateCardEvent)) {
            Printer.prompt("you can't activate this spell");
            return;
        }
        notifyAllEffectsForConsideration(activateCardEvent);
        Printer.prompt("spell activated");
        Printer.showBoard(this, this.opponent);
    }

    // by Pasha
    public void setSpellOrTrap() {
        int placeOnHand = getSelectedCardOnHandID();
        if (placeOnHand == -1) {
            Printer.prompt("you can't set this card");
            return;
        }
        boolean isFieldSpell = (selectedCard instanceof SpellCard) && (((SpellCard) selectedCard).getCardSpellType() == SpellType.FIELD);
        if (getFirstEmptyPlaceOnSpellsField() == -1 && !isFieldSpell) {
            Printer.prompt("spell card zone is full");
            return;
        }
        Printer.prompt("set successfully");
        if (isFieldSpell) {
            // event :[cardEvent]
            CardEvent cardEvent = new CardEvent(selectedCard, CardEventInfo.ENTRANCE, null);
            if (!getPermissionFromAllEffects(cardEvent)) {
                Printer.prompt("you cant set this spell");
                return;
            }

            notifyAllEffectsForConsideration(cardEvent);
            removeCardFromField(fieldZone, null);
            fieldZone = (SpellCard) selectedCard;
            return;
        }
        // event :[cardEvent]
        CardEvent cardEvent = new CardEvent(selectedCard, CardEventInfo.ENTRANCE, null);
        if (!getPermissionFromAllEffects(cardEvent)) {
            Printer.prompt("you cant set this spell");
            return;
        }

        notifyAllEffectsForConsideration(cardEvent);
        spellsAndTrapFieldList[getFirstEmptyPlaceOnSpellsField()] = selectedCard;
        Printer.showBoard(this, this.opponent);
    }


    public void showGraveyard() {
        if (Utility.checkAndPrompt(graveyard.isEmpty(), "graveyard empty")) return;
        int i = 0;
        for (Card deadCard : graveyard.getCardsList()) {
            i++;
            Printer.prompt(i + ". " + deadCard.getCardName() + ":" + deadCard.getCardDescription());
        }
    }

    public void showSelectedCard() {
        if (Utility.checkAndPrompt(
                selectedCard == null,
                "no card is selected yet")) return;
        if (Utility.checkAndPrompt(
                selectedCard.getPlayer() == opponent && !selectedCard.isFaceUp(),
                "card is not visible")) return;
        Printer.showCard(selectedCard);
    }

    public void forfeit() {
        loser = true; // ENJOY THE DESIGN: THE COMPLICATED FUNCTION "FORFEIT" IS HANDLED IN ONE LINE :)
    }

    private void notifyEffectsOfCard(Event event, Card card) {
        for (Effect effect : card.getEffects())
            effect.consider(event);
    }

    private void notifyMyEffectsForConsideration(Event event) {
        if (fieldZone != null)
            notifyEffectsOfCard(event, fieldZone);
        for (int i = 1; i <= FIELD_SIZE; ++i) {
            if (monstersFieldList[i] != null)
                notifyEffectsOfCard(event, monstersFieldList[i]);
            if (spellsAndTrapFieldList[i] != null)
                notifyEffectsOfCard(event, spellsAndTrapFieldList[i]);
        }
    }

    private void notifyAllEffectsForConsideration(Event event) {
        notifyMyEffectsForConsideration(event);
        opponent.notifyMyEffectsForConsideration(event);
    }

    private boolean getPermissionFromCard(Event event, Card card) {
        boolean permitted = true;
        for (Effect effect : card.getEffects())
            permitted &= effect.permit(event);
        return permitted;
    }

    private boolean getPermissionFromMyEffects(Event event) {
        boolean permitted = true;
        if (fieldZone != null)
            permitted = getPermissionFromCard(event, fieldZone);
        for (int i = 1; i <= FIELD_SIZE; ++i) {
            if (monstersFieldList[i] != null)
                permitted &= getPermissionFromCard(event, monstersFieldList[i]);
            if (spellsAndTrapFieldList[i] != null)
                permitted &= getPermissionFromCard(event, spellsAndTrapFieldList[i]);
        }
        return permitted;
    }

    public boolean getPermissionFromAllEffects(Event event) {
        return getPermissionFromMyEffects(event) && this.getOpponent().getPermissionFromMyEffects(event);
    }

    public int getNumberOfMonstersInGraveyard() {
        int result = 0;
        for (Card card : graveyard.getCardsList()) {
            if (card instanceof MonsterCard) result++;
        }
        return result;
    }


    public boolean equals(Player checkPlayer) {
        return user.getUsername().contentEquals(checkPlayer.getUser().getUsername());
    }
}
