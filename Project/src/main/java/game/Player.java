package game;

import card.*;
import data.Printer;
import effects.Effect;
import events.*;
import lombok.Getter;
import lombok.Setter;
import menus.Menu;
import org.jetbrains.annotations.Nullable;
import utility.Utility;
import view.UtilityView;

@Getter
@Setter
public class Player {

    protected static final int HAND_SIZE = 7;
    @Getter
    protected static final int FIELD_SIZE = 5;

    // Initialized in constructor
    protected User user;
    protected Deck graveyard;
    protected Deck remainingDeck;
    protected MonsterCard[] monstersFieldList;
    protected Card[] spellsAndTrapFieldList;
    protected SpellCard fieldZone;
    protected Deck hand;
    protected int lifePoint;
    protected boolean loser;
    protected Card selectedCard;
    protected boolean hasSummonedMonsterThisTurn; // has to be reset at end phase
    protected boolean isAttackPhaseEndedByEffect = false;
    protected Card theSummonedMonsterThisTurn;
    // Initialized by setters
    protected Game game;
    protected Player opponent;


    public Player(User user, Deck deck) {
        this.user = user;
        graveyard = new Deck(105);
        this.remainingDeck = deck.cloneDeck();
        monstersFieldList = new MonsterCard[FIELD_SIZE + 1]; // To have 1-based indexing
        spellsAndTrapFieldList = new Card[FIELD_SIZE + 1];   // To have 1-based indexing
        fieldZone = null;
        hand = new Deck(HAND_SIZE);
        lifePoint = 8000;
        loser = false;
        selectedCard = null;
        hasSummonedMonsterThisTurn = false;
    }

    public void print(String str) {
        Printer.prompt(str);
    }

    protected void increaseLifePoint(int amount) {
        this.lifePoint += amount;
    }

    // Returns true iff the decrement action has been done successfully
    public boolean decreaseLifePoint(int amount, Card causedCard) {
        // event : [lifepoint change]
        LifePointChangeEvent lifePointChangeEvent = new LifePointChangeEvent(this, causedCard, -amount);
        if (!getPermissionFromAllEffects(lifePointChangeEvent)) {
            UtilityView.displayMessage("life point won't change (an effect prevented LP change)");
            return false;
        }
        lifePoint -= amount;
        if (lifePoint < 0) lifePoint = 0;
        if (lifePoint == 0) loser = true;
        notifyAllEffectsForConsideration(lifePointChangeEvent);
        return true;
    }

    protected void setDuelWinner(String nickName) {
        if (this.getUser().getNickname().equals(nickName)) this.getOpponent().setLoser(true);
        else this.setLoser(true);
    }

    public void drawACard(SpellType spellType) {
        Card newCard = null;

        if (spellType == null) {
            newCard = remainingDeck.pop();
        } else {
            for (Card card : remainingDeck.getCardsList())
                if (card instanceof SpellCard && ((SpellCard) card).getCardSpellType() == spellType) {
                    newCard = card;
                    break;
                }
            if (newCard == null)
                return;
            remainingDeck.removeCard(newCard);
        }
        DrawCardEvent drawCardEvent = new DrawCardEvent(newCard);
        if (!getPermissionFromAllEffects(drawCardEvent)) {
            remainingDeck.addCard(newCard);
            return;
        }
        hand.addCard(newCard);
        //print("new card added to the hand: " + newCard.getCardName());
        notifyAllEffectsForConsideration(drawCardEvent);
    }


    public void drawPhase() {
        if (game.isNotFirstTurn()) {
            if (remainingDeck.isEmpty()) {
                UtilityView.displayMessage("No card is remained in " + this.user.getNickname() + "'s deck.\nThey've lost!");
                loser = true;
                return;
            }
            drawACard(null);
        }
        PhaseEndedEvent phaseEndedEvent = new PhaseEndedEvent(Phase.DRAW, this);
        notifyAllEffectsForConsideration(phaseEndedEvent);
    }

    public void standbyPhase() {
        PhaseEndedEvent phaseEndedEvent = new PhaseEndedEvent(Phase.STANDBY, this);
        notifyAllEffectsForConsideration(phaseEndedEvent);
    }

    public void mainPhase1() {
        PhaseEndedEvent phaseEndedEvent = new PhaseEndedEvent(Phase.MAIN1, this);
        notifyAllEffectsForConsideration(phaseEndedEvent);
    }

    public void battlePhase() {
        isAttackPhaseEndedByEffect = false;
        PhaseEndedEvent phaseEndedEvent = new PhaseEndedEvent(Phase.BATTLE, this);
        notifyAllEffectsForConsideration(phaseEndedEvent);
    }

    public void endBattlePhaseByEffect() {
        isAttackPhaseEndedByEffect = true;
    }

    public void mainPhase2() {
        PhaseEndedEvent phaseEndedEvent = new PhaseEndedEvent(Phase.MAIN2, this);
        notifyAllEffectsForConsideration(phaseEndedEvent);
    }

    public void endPhase() {
        for (int i = 1; i <= FIELD_SIZE; i++) {
            if (monstersFieldList[i] != null) {
                monstersFieldList[i].setHasAttackedThisTurn(false);
                monstersFieldList[i].setHasChangedPositionThisTurn(false);
            }
        }
        hasSummonedMonsterThisTurn = false;
        theSummonedMonsterThisTurn = null;
        TurnChangeEvent turnChangeEvent = new TurnChangeEvent();
        turnChangeEvent.setPlayer(this);
        notifyAllEffectsForConsideration(turnChangeEvent);
        PhaseEndedEvent phaseEndedEvent = new PhaseEndedEvent(Phase.END, this);
        notifyAllEffectsForConsideration(phaseEndedEvent);
        if (opponent instanceof AIPlayer)
            game.proceedNextPhase();
    }

    public int getSelectedMonsterCardOnFieldID() {
        for (int i = 1; i <= FIELD_SIZE; ++i)
            if (selectedCard == monstersFieldList[i])
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

    public boolean removeCardFromField(Card card, Card causedCard) {
        if (card == null) return false;

        CardEvent cardEvent = new CardEvent(card, CardEventInfo.DESTROYED, causedCard);
        if (!getPermissionFromAllEffects(cardEvent)) {
            UtilityView.displayMessage("card won't be destroyed(an effect prevented destruction)");
            return false;
        }
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
        notifyAllEffectsForConsideration(cardEvent);
        return true;
    }

    public void forceRemoveCardFromField(Card card) {
        for (int i = 1; i <= FIELD_SIZE; i++) {
            if (monstersFieldList[i] == card) {
                monstersFieldList[i] = null;
                graveyard.addCard(card);
            }
        }
        for (int i = 1; i <= FIELD_SIZE; i++) {
            spellsAndTrapFieldList[i] = null;
            graveyard.addCard(card);
        }
        if (fieldZone == card) {
            fieldZone = null;
            graveyard.addCard(card);
        }
    }


    public boolean addMonsterCardToField(MonsterCard newCard) {
        if (monstersFieldList.length == FIELD_SIZE) return false;
        monstersFieldList[getFirstEmptyPlaceOnMonstersField()] = newCard;
        return true;
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

    public int getSpellCountOnField() {
        int count = 0;
        for (int i = 1; i < FIELD_SIZE; i++) {
            if (spellsAndTrapFieldList[i] != null) count++;
        }
        return count;
    }

    public void flipMonsterOnDefense(MonsterCard monsterCard, Card causedByCard) {
        CardEvent cardEvent = new CardEvent(monsterCard, CardEventInfo.FLIP, causedByCard);
        if (getPermissionFromAllEffects(cardEvent)) {
            monsterCard.setFaceUp(true);
            notifyAllEffectsForConsideration(cardEvent);
        }
    }

    public Card obtainCardFromHand() {
        String response;
        int index = -1;
        while (!(0 <= index && index < hand.getSize())) {
            response = UtilityView.obtainInformationInCertainWay("Please select a card position on hand" + "(a number in range 1 to " + hand.getSize() + ")", "\\d");
            index = Integer.parseInt(response) - 1;
        }
        return hand.getCardsList().get(index);
    }

    public Card obtainSpellTrapFromField() {
        String response;
        int index = -1;
        while (!(1 <= index && index <= FIELD_SIZE)
                || spellsAndTrapFieldList[index] == null
                && !(spellsAndTrapFieldList[index] instanceof SpellCard
                || spellsAndTrapFieldList[index] instanceof TrapCard)) {
            response = UtilityView.obtainInformationInCertainWay("Please select a spell or trap card position on field\n" + "(a number in range 1 to " + 5 + ")", "\\d");
            index = Integer.parseInt(response);
        }
        return spellsAndTrapFieldList[index];
    }


    public boolean obtainConfirmation(String promptMassage) {
        String response = UtilityView.obtainInformationInCertainWay(promptMassage, "yes|no");
        if (response.equals("yes")) return true;
        else return false;
    }

    // todo : change to become graphic compatible
    public Card obtainCardFromDeck(boolean showDeck) {
        if (showDeck) {
            Printer.prompt("You deck contains these cards:");
            for (Card card : remainingDeck.getCardsList()) Printer.showCard(card);
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

    // todo : change to become graphic compatible
    public Card obtainCardFromGraveYard() {
        if (graveyard.isEmpty()) return null;
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

    // todo : change to become graphic compatible
    public MonsterCard obtainMonsterCard(Deck deck) {
        if (deck.isEmpty()) return null;
        int i = 0;
        for (Card card : deck.getCardsList()) {
            i++;
            Printer.prompt(i + ". " + card.getCardName() + ": " + card.getCardDescription());
        }
        String response;
        int index;
        while (true) {
            while (true) {
                Printer.prompt("Please select a card position on deck");
                Printer.prompt("(a number in range 1 to " + deck.getSize() + ")");
                response = Utility.getNextLine();
                if (response.matches("\\d")) break;
                Printer.prompt(Menu.INVALID_COMMAND);
            }
            index = Integer.parseInt(response) - 1;
            if (0 <= index && index < deck.getSize()) {
                if (deck.getCardsList().get(index) instanceof MonsterCard)
                    return (MonsterCard) deck.getCardsList().get(index);
            }
        }
    }

    // Obtains a number in range [l, r), prompting the player first.
    // returns 0/0 if r <= l
    public int obtainNumberInRange(int l, int r, String prompt) {
        int number;
        while (true) {
            try {
                number = Integer.parseInt(UtilityView.obtainInformationInCertainWay(prompt, "-?\\d+"));
            } catch (NumberFormatException ignored) {
                System.out.println("not a number!");
                continue;
            }
            if (l <= number && number < r) return number;
            UtilityView.showError("number out of range!");
        }
    }

    protected boolean isMonsterAddressInvalid(int address) {
        if (address < 1) return true;
        if (address > FIELD_SIZE) return true;
        return monstersFieldList[address] == null;
    }

    // Used to summon monsters with level less than 5
    protected void summonMonsterLowLevel(int placeOnHand, int placeOnField) {
        monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
        hand.removeCardAt(placeOnHand);
        hasSummonedMonsterThisTurn = true;
        selectedCard.setFaceUp(true);
        ((MonsterCard) selectedCard).setDefenseMode(false);
        theSummonedMonsterThisTurn = selectedCard;
    }

    // Used to summon monsters with level 5 or 6
    protected void summonMonsterMediumLevel(int placeOnHand, int tributeAddress) {
        MonsterCard tributeMonster = monstersFieldList[tributeAddress];
        monstersFieldList[tributeAddress] = null;
        graveyard.getCardsList().add(tributeMonster);
        int placeOnField = getFirstEmptyPlaceOnMonstersField();
        monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
        hasSummonedMonsterThisTurn = true;
        theSummonedMonsterThisTurn = selectedCard;
        selectedCard.setFaceUp(true);
        ((MonsterCard) selectedCard).setDefenseMode(false);
        hand.removeCardAt(placeOnHand);
    }

    public void changeMonsterPosition() {
        if (selectedCard == null) {
            UtilityView.showError("no card is selected yet");
            return;
        }
        int cardId = getSelectedMonsterCardOnFieldID();
        if (cardId == -1) {
            UtilityView.showError("you can't change this card position");
            return;
        }
        if (monstersFieldList[cardId].isHasChangedPositionThisTurn()) {
            UtilityView.showError("you already changed this card position in this turn");
            return;
        }
        // maybe change position event ? not needed yet though
        monstersFieldList[cardId].setHasChangedPositionThisTurn(true);
        monstersFieldList[cardId].setDefenseMode(!monstersFieldList[cardId].isDefenseMode());
        UtilityView.displayMessage("monster card position changed successfully");
    }

    // Used to summon monsters with level greater than 6
    protected void summonMonsterHighLevel(int firstTribute, int secondTribute, int placeOnHand) {
        MonsterCard firstTributeCard = monstersFieldList[firstTribute];
        MonsterCard secondTributeCard = monstersFieldList[secondTribute];
        monstersFieldList[firstTribute] = null;
        monstersFieldList[secondTribute] = null;
        graveyard.getCardsList().add(firstTributeCard);
        graveyard.getCardsList().add(secondTributeCard);
        hand.removeCardAt(placeOnHand);
        int placeOnField = getFirstEmptyPlaceOnMonstersField();
        monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
        hasSummonedMonsterThisTurn = true;
        selectedCard.setFaceUp(true);
        ((MonsterCard) selectedCard).setDefenseMode(false);
        theSummonedMonsterThisTurn = selectedCard;
    }


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
        CardEvent cardEvent = new CardEvent(selectedCard, CardEventInfo.ENTRANCE, null);
        if (!getPermissionFromAllEffects(cardEvent)) {

            UtilityView.showError("you don't have permission to summon");
            return;
        }
        selectedCard.setFaceUp(true);
        int monsterLevel = ((MonsterCard) selectedCard).getCardLevel();
        if (monsterLevel <= 4) {
            summonMonsterLowLevel(place, placeOnField);

            notifyAllEffectsForConsideration(cardEvent);
            Printer.showBoard(this, this.opponent);
            return;
        }
        if (monsterLevel <= 6) {
            if (Utility.checkAndPrompt(placeOnField == 1,
                    "there are not enough cards for tribute")) return;
            int address = Integer.parseInt(UtilityView.obtainInformationInCertainWay("input the address of the card you want to tribute", "\\d+"));
            if (Utility.checkAndPrompt(isMonsterAddressInvalid(address),
                    "there are no monsters on this address")) return;
            summonMonsterMediumLevel(place, address);
            notifyAllEffectsForConsideration(cardEvent);
            Printer.showBoard(this, this.opponent);
            return;
        }

        if (Utility.checkAndPrompt(placeOnField == 1 || placeOnField == 2,
                "there are not enough cards for tribute")) return;
        String input1, input2;
        //Printer.prompt("input two addresses for the cards you want to tribute in TWO DIFFERENT LINES:");
        int firstTribute = Integer.parseInt(UtilityView.obtainInformationInCertainWay("input the first address for tribute", "\\d+"));
        int secondTribute = Integer.parseInt(UtilityView.obtainInformationInCertainWay("input the second address for tribute", "\\d+"));
        if (Utility.checkAndPrompt(isMonsterAddressInvalid(firstTribute) || isMonsterAddressInvalid(secondTribute),
                "there is no monster on one of these addresses")) return;
        if (Utility.checkAndPrompt(firstTribute == secondTribute,
                "Tributes are the same!")) return;
        summonMonsterHighLevel(firstTribute, secondTribute, place);
        notifyAllEffectsForConsideration(cardEvent);

        Printer.showBoard(this, this.opponent);
    }


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
        UtilityView.displayMessage("set successfully");
        notifyAllEffectsForConsideration(cardEvent);
        Printer.showBoard(this, this.opponent);
    }


    public void flipSummon() {
        if (selectedCard == null) {
            UtilityView.showError("no card is selected yet");
            return;
        }
        int cardID = getSelectedMonsterCardOnFieldID();
        if (cardID <= 0) {
            UtilityView.showError("you can’t change this card position");
            return;
        }
        if (selectedCard.isFaceUp() || theSummonedMonsterThisTurn == selectedCard) {
            UtilityView.showError("you can’t flip summon this card");
            return;
        }
        // event : [flip]
        CardEvent cardEvent = new CardEvent(selectedCard, CardEventInfo.FLIP, null);

        if (!getPermissionFromAllEffects(cardEvent)) {
            UtilityView.showError("you can't flip your card");
            return;
        }
        selectedCard.setFaceUp(true);

        notifyAllEffectsForConsideration(cardEvent);
        UtilityView.displayMessage("flip summoned successfully");
        Printer.showBoard(this, this.opponent);
    }


    public void attack(int positionToAttack) {
        if (selectedCard == null) {
            UtilityView.showError("no card is selected yet");
            return;
        }
        int cardID = getSelectedMonsterCardOnFieldID();
        System.out.println(cardID);
        if (cardID <= 0) {
            UtilityView.showError("you can’t attack with this card");
            return;
        }
        if (((MonsterCard) selectedCard).isHasAttackedThisTurn()) {
            UtilityView.showError("this card already attacked");
            return;
        }
        if (((MonsterCard) selectedCard).isDefenseMode()) {
            UtilityView.showError("can't attack with a defense position monster");
            return;
        }
        if (positionToAttack <= 0 || positionToAttack >= 6 || opponent.getMonstersFieldList()[positionToAttack] == null)
            return;
        // event : [Attack Event]
        AttackEvent attackEvent = new AttackEvent((MonsterCard) selectedCard, opponent.getMonstersFieldList()[positionToAttack]);
        if (!getPermissionFromAllEffects(attackEvent)) {
            UtilityView.showError("you can't attack");
            return;
        }
        notifyAllEffectsForConsideration(attackEvent);
        ((MonsterCard) selectedCard).attackTo(opponent.getMonstersFieldList()[positionToAttack], this);
        Printer.showBoard(this, this.opponent);
    }

    public void attackDirect() {
        if (selectedCard == null) {
            UtilityView.showError("no card is selected yet");
            return;
        }
        int cardID = getSelectedMonsterCardOnFieldID();
        if (cardID <= 0) {
            UtilityView.showError("you can’t attack with this card");
            return;
        }
        if (((MonsterCard) selectedCard).isHasAttackedThisTurn()) {
            UtilityView.showError("this card has already attacked");
            return;
        }

        boolean doesOpponentHaveMonster = false;
        for (int i = 1; i <= FIELD_SIZE; ++i)
            if (opponent.getMonstersFieldList()[i] != null) {
                doesOpponentHaveMonster = true;
                break;
            }
        if (doesOpponentHaveMonster) {
            UtilityView.showError("can't attack direct when opponent has at least a monster");
            return;
        }
        // event : [Attack Event]
        AttackEvent attackEvent = new AttackEvent((MonsterCard) selectedCard, null);
        if (!getPermissionFromAllEffects(attackEvent)) {
            UtilityView.showError("you can't attack");
            return;
        }
        notifyAllEffectsForConsideration(attackEvent);
        opponent.decreaseLifePoint(((MonsterCard) selectedCard).getCardAttack(), null);
        if (opponent.getLifePoint() <= 0) opponent.setLoser(true);
        ((MonsterCard) selectedCard).setHasAttackedThisTurn(true);
        UtilityView.displayMessage("your opponent receives " + ((MonsterCard) selectedCard).getCardAttack() + " battle damage");
        Printer.showBoard(this, this.opponent);
    }

    // by Pasha
    public void activateEffect() {
        if (selectedCard == null) {
            UtilityView.showError("no card is selected");
            return;
        }
        if (!(selectedCard instanceof SpellCard) && !(selectedCard instanceof TrapCard)) {
            UtilityView.showError("activate effect is only for spells");
            return;
        }
        if (selectedCard.isFaceUp()) {
            UtilityView.showError("you have already activated this card");
            return;
        }
        boolean isFieldSpell = (selectedCard instanceof SpellCard) && (((SpellCard) selectedCard).getCardSpellType() == SpellType.FIELD);
        if (isFieldSpell) {
            if(getSelectedCardOnHandID() == -1 && fieldZone != null) {
                fieldZone.setFaceUp(true);
                CardEvent cardEvent = new CardEvent(fieldZone , CardEventInfo.FLIP , null);
                notifyAllEffectsForConsideration(cardEvent);
                return ;
            }
            fieldZone = (SpellCard) selectedCard;
            fieldZone.setFaceUp(true);
            CardEvent cardEvent = new CardEvent(fieldZone , CardEventInfo.ACTIVATE_EFFECT , null);
            notifyAllEffectsForConsideration(cardEvent);
            return;
        }
        CardEvent activateCardEvent = new CardEvent(selectedCard, CardEventInfo.ACTIVATE_EFFECT, null);
        SpellTrapActivationEvent spellTrapActivationEvent = new SpellTrapActivationEvent(selectedCard);
        if (getSelectedCardOnHandID() != -1) {
            int firstEmptyPlace = getFirstEmptyPlaceOnSpellsField();
            if (firstEmptyPlace == -1) {
                UtilityView.showError("there is no card space on board for you to activate your spell");
            } else {
                CardEvent entranceCardEvent = new CardEvent(selectedCard, CardEventInfo.ENTRANCE, null);
                if (!getPermissionFromAllEffects(entranceCardEvent) || !getPermissionFromAllEffects(activateCardEvent)) {
                    Printer.prompt("you can't activate this spell");
                    return;
                }
                spellsAndTrapFieldList[getFirstEmptyPlaceOnSpellsField()] = selectedCard;
                notifyAllEffectsForConsideration(entranceCardEvent);
            }
        } else if(getSpellOrTrapPositionOnBoard(selectedCard) == -1) {
            UtilityView.showError("invalid card selected for activating");
            return ;
        }
        // event : [cardEvent]

        if (!getPermissionFromAllEffects(activateCardEvent) || !getPermissionFromAllEffects(spellTrapActivationEvent)) {
            UtilityView.showError("you can't activate this spell");
            return;
        }

        notifyAllEffectsForConsideration(activateCardEvent);
        notifyAllEffectsForConsideration(spellTrapActivationEvent);
        UtilityView.displayMessage("spell activated");
        Printer.showBoard(this, this.opponent);
    }

    // by Pasha
    public boolean setSpellOrTrap() {
        int placeOnHand = getSelectedCardOnHandID();
        if (placeOnHand == -1) {
            UtilityView.showError("you can't set this card");
            return false;
        }
        boolean isFieldSpell = (selectedCard instanceof SpellCard) && (((SpellCard) selectedCard).getCardSpellType() == SpellType.FIELD);
        if (isFieldSpell) {
            // event :[cardEvent]
            CardEvent cardEvent = new CardEvent(selectedCard, CardEventInfo.ENTRANCE, null);
            if (!getPermissionFromAllEffects(cardEvent)) {
                UtilityView.showError("you can't set this spell");
                return false;
            }

            removeCardFromField(fieldZone, null);
            fieldZone = (SpellCard) selectedCard;
            hand.removeCard(selectedCard);
            notifyAllEffectsForConsideration(cardEvent);
            UtilityView.displayMessage("set successfully");
            return true;
        }
        if (getFirstEmptyPlaceOnSpellsField() == -1) {
            UtilityView.showError("spell card zone is full");
            return false;
        }
        // event :[cardEvent]
        CardEvent cardEvent = new CardEvent(selectedCard, CardEventInfo.ENTRANCE, null);
        if (!getPermissionFromAllEffects(cardEvent)) {
            UtilityView.showError("you can't set this spell");
            return false;
        }
        spellsAndTrapFieldList[getFirstEmptyPlaceOnSpellsField()] = selectedCard;
        notifyAllEffectsForConsideration(cardEvent);
        hand.removeCard(selectedCard);
        UtilityView.displayMessage("set successfully");
        Printer.showBoard(this, this.opponent);
        return true;
    }

    // todo : change to become graphic compatible
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
        loser = true;
    }

    protected void notifyEffectsOfCard(Event event, @Nullable Card card) {
        if (card == null) return;
        for (Effect effect : card.getEffects())
            if (!effect.isInConsideration()) {
                try {
                    effect.consider(event);
                } catch (Exception ignored) {
                    System.out.println("Sorry, some problems occurred in handling effects :(");
                }
            }
    }

    protected void notifyMyEffectsForConsideration(Event event) {
        notifyEffectsOfCard(event, fieldZone);
        for (int i = 1; i <= FIELD_SIZE; ++i) {
            notifyEffectsOfCard(event, monstersFieldList[i]);
            notifyEffectsOfCard(event, spellsAndTrapFieldList[i]);
        }
    }

    public void notifyAllEffectsForConsideration(Event event) {
        game.notifyGraphic();
        notifyMyEffectsForConsideration(event);
        opponent.notifyMyEffectsForConsideration(event);
    }

    protected boolean getPermissionFromCard(Event event, Card card) {
        boolean permitted = true;
        for (Effect effect : card.getEffects())
            if (!effect.isInConsideration()) {
                try {
                    permitted &= effect.permit(event);
                } catch (Exception ignored) {
                    System.out.println("Sorry, some problem occurred :(");
                }
            }
        return permitted;
    }

    protected boolean getPermissionFromMyEffects(Event event) {
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
