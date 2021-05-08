package game;

import card.Card;
import card.MonsterCard;
import card.SpellCard;
import card.TrapCard;
import data.Printer;
import lombok.Getter;
import lombok.Setter;
import utility.Utility;

import java.util.HashMap;
import java.util.regex.Matcher;

@Getter
@Setter
// TODO : SINA
public class Player {
    private static final String regexSelect = "select.+";
    private static final String regexShowGraveyard = "show\\sgraveyard";
    private static final String regexShowSelectedCard = "card\\sshow\\s\\-\\-selected";
    private static final String regexSummon = "summon";
    private static final String regexSet = "set.*";
    private static final String regexFlipSummon = "flip\\-summon";
    private static final String regexAttackNormal = "attack\\s(\\d+)";
    private static final String regexAttackDirect = "attack\\sdirect";
    private static final String regexActivateEffect = "activate\\seffect";
    private static final String SUCCESSFUL_SUMMON = "summoned successfully";
    private static final int HAND_SIZE = 7;
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
    private boolean isLooser;
    private Card selectedCard;
    private boolean hasSummonedMonsterThisTurn; // has to be reset at end phase
    private Card theSummonedMonsterThisTurn;
    // Initialized by setters
    private Game game;
    private Player opponent;


    public Player(User user, Deck deck) {
        this.user = user;
        graveyard = new Deck();
        this.remainingDeck = deck;
        monstersFieldList = new MonsterCard[FIELD_SIZE + 1]; // To have 1-based indexing
        spellsAndTrapFieldList = new Card[FIELD_SIZE + 1];   // To have 1-based indexing
        fieldZone = null;
        hand = new Deck();
        lifePoint = 8000;
        isLooser = false;
        selectedCard = null;
        hasSummonedMonsterThisTurn = false;
    }

    public void runCommonCommands(String command) {
        if (Utility.getCommandMatcher(command, regexSelect).matches()) {
            selectCard(command);
        } else if (Utility.getCommandMatcher(command, regexShowGraveyard).matches()) {
            showGraveyard();
        } else if (Utility.getCommandMatcher(command, regexShowSelectedCard).matches()) {
            showSelectedCard();
        }
    }

    public void runMainPhaseCommands(String command) {
        if (Utility.getCommandMatcher(command, regexSummon).matches()) {
            summonMonster();
        } else if (Utility.getCommandMatcher(command, regexSet).matches()) {
            if (selectedCard == null) {
                Printer.prompt("no card is selected yet");
                return;
            }
            if (selectedCard instanceof MonsterCard) setMonster();
            else if (selectedCard instanceof SpellCard || selectedCard instanceof TrapCard) setSpell();
        } else if (Utility.getCommandMatcher(command, regexFlipSummon).matches()) {
            flipSummon();
        } else if (Utility.getCommandMatcher(command, regexAttackNormal).matches()) {
            attack(command);
        } else if (Utility.getCommandMatcher(command, regexAttackDirect).matches()) {
            attackDirect();
        } else if (Utility.getCommandMatcher(command, regexActivateEffect).matches()) {
            activateEffect();
        }
    }

    public void runBattlePhaseCommands(String command) {
        if (Utility.getCommandMatcher(command, regexAttackNormal).matches()) {
            attack(command);
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
                isLooser = true;
                return;
            }
            Card newCard = remainingDeck.pop();
            hand.addCard(newCard);
            Printer.prompt("new card added to the hand: " + newCard.getCardName());
        }
    }

    public void standbyPhase() {
        Printer.prompt("phase: standby phase");
    }

    public void mainPhase1() {
        Printer.prompt("phase: main phase 1");
//      TODO : PASHA
        Printer.showBoard(this, this.opponent);
        while (true) {
            String command = Utility.getNextLine();
            runCommonCommands(command);
            runMainPhaseCommands(command);
        }
    }

    public void battlePhase() {
        Printer.prompt("phase: battle phase");
//      TODO : KAMYAR
        if (game.isFirstTurn()) return;
        while (true) {
            String command = Utility.getNextLine();
            runCommonCommands(command);
            runBattlePhaseCommands(command);
        }
    }

    public void mainPhase2() {
        Printer.prompt("phase: main phase 2");
//      TODO : PASHA
        while (true) {
            String command = Utility.getNextLine();
            runCommonCommands(command);
            runMainPhaseCommands(command);
        }
    }

    public void endPhase() {
        Printer.prompt("phase: end phase");
//      TODO : KAMYAR
//      NOTE : for each monster card .hasAttacked has to be set to false and hasSummonedMonsterThisTurn should be also set to false
        Printer.prompt("phase: End phase");
        for (int i = 1; i <= 5; i++) {
            if (monstersFieldList[i] != null) monstersFieldList[i].setHasAttackedThisTurn(false);
        }
        hasSummonedMonsterThisTurn = false;
        theSummonedMonsterThisTurn = null;
        Printer.prompt("its " + opponent.getUser().getNickname() + "’s turn");
    }

    public int getSelectedMonsterCardOnFieldID() {
        for (int i = 1; i <= 5; ++i)
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
        for (int i = 1; i <= 5; i++) {
            if (monstersFieldList[i] == null) return i;
        }
        return -1;
    }

    public void showBoard() {
        Printer.showBoard(this, opponent);
    }

    public void selectCard(String command) {
//      TODO : KAMYAR
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
            if (Utility.checkAndPrompt((placeID > 5 || placeID < 1), "invalid selection")) return;
            Card selectCandidateCard = (isOpponentsCard ? opponent.monstersFieldList[placeID] : monstersFieldList[placeID]);
            if (Utility.checkAndPrompt(selectCandidateCard == null, "no card found in the given position")) return;
            selectedCard = selectCandidateCard;
            Printer.prompt("card selected");
            return;
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
            if (Utility.checkAndPrompt((placeID > 5 || placeID < 1), "invalid selection")) return;
            Card selectCandidateCard = (isOpponentsCard ? opponent.spellsAndTrapFieldList[placeID] : spellsAndTrapFieldList[placeID]);
            if (Utility.checkAndPrompt(selectCandidateCard == null, "no card found in the given position")) return;
            selectedCard = selectCandidateCard;
            Printer.prompt("card selected");
            return;
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
            return;
        } else if (Utility.isCommandValid(map, new String[]{"hand"}, null)) {
            if (Utility.checkAndPrompt(!Utility.areAttributesValid(map, new String[]{"hand"}, null), "invalid selection"))
                return;
            place = map.get("hand");
            if (Utility.checkAndPrompt((!place.matches("\\d+")), "invalid selection")) return;
            placeID = Integer.parseInt(place);
            if (Utility.checkAndPrompt((placeID >= hand.getCardsList().size() || placeID < 0), "invalid selection"))
                return;
            Card selectCandidateCard = hand.getCardsList().get(placeID);
            selectedCard = selectCandidateCard;
            Printer.prompt("card selected");
            return;

        } else {
            Printer.prompt("invalid selection");
        }

    }

    private boolean isMonsterAddressValid(int address) {
        if (address < 1) return false;
        if (address > FIELD_SIZE) return false;
        return monstersFieldList[address] != null;
    }

    // Used to summon monsters with level less than 5
    private void summonMonsterLowLevel(int placeOnHand, int placeOnField) {
        monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
        hand.removeCard(placeOnHand);
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
        hand.removeCard(placeOnHand);
    }

    // Used to summon monsters with level greater than 6
    private void summonMonsterHighLevel(int firstTribute, int secondTribute, int placeOnHand) {
        MonsterCard firstTributedCard = monstersFieldList[firstTribute];
        MonsterCard secondTributedCard = monstersFieldList[secondTribute];
        monstersFieldList[firstTribute] = null;
        monstersFieldList[secondTribute] = null;
        graveyard.getCardsList().add(firstTributedCard);
        graveyard.getCardsList().add(secondTributedCard);
        hand.removeCard(placeOnHand);
        int placeOnField = getFirstEmptyPlaceOnMonstersField();
        monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
        hasSummonedMonsterThisTurn = true;
        selectedCard.setFaceUp(true);
        ((MonsterCard) selectedCard).setDefenseMode(false);
        theSummonedMonsterThisTurn = selectedCard;
    }

    public void summonMonster() {
//      TODO : KAMYAR
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
        int monsterLevel = ((MonsterCard) selectedCard).getCardLevel();
        if (monsterLevel <= 4) {
            summonMonsterLowLevel(place, placeOnField);
            Printer.prompt(SUCCESSFUL_SUMMON);
            return;
        }
        if (monsterLevel <= 6) {
            if (Utility.checkAndPrompt(placeOnField == 1,
                    "there are not enough cards for tribute")) return;
            Printer.prompt("input the address of the card you want to tribute:");
            int address = Integer.parseInt(Utility.getNextLine());
            if (Utility.checkAndPrompt(!isMonsterAddressValid(address),
                    "there no monsters on this address")) return;
            summonMonsterMediumLevel(place, address);
            Printer.prompt(SUCCESSFUL_SUMMON);
            return;
        }

        if (Utility.checkAndPrompt(placeOnField == 1 || placeOnField == 2,
                "there are not enough cards for tribute")) return;
        Printer.prompt("input two addresses for the cards you want to tribute in TWO DIFFERENT LINES:");
        int firstTribute = Integer.parseInt(Utility.getNextLine());
        int secondTribute = Integer.parseInt(Utility.getNextLine());
        if (Utility.checkAndPrompt(!isMonsterAddressValid(firstTribute) || !isMonsterAddressValid(secondTribute),
                "there is no monster on one of these addresses")) return;
        // Note: What if the two addresses above are the same?
        summonMonsterHighLevel(firstTribute, secondTribute, place);
        Printer.prompt(SUCCESSFUL_SUMMON);
    }

    public void setMonster() {
//      TODO : KAMYAR
        if (Utility.checkAndPrompt((selectedCard == null), "no card is selected yet")) return;
        int placeOnHand = getSelectedCardOnHandID();
        if (Utility.checkAndPrompt((placeOnHand == -1), "you can’t set this card")) return;
        int placeOnMonstersZone = getFirstEmptyPlaceOnMonstersField();
        if (Utility.checkAndPrompt((placeOnMonstersZone == -1), "monster card zone is full")) return;
        if (Utility.checkAndPrompt(hasSummonedMonsterThisTurn, "you already summoned/set on this turn")) return;
        hand.removeCard(placeOnHand);
        monstersFieldList[placeOnMonstersZone] = (MonsterCard) selectedCard;
        selectedCard.setFaceUp(false);
        hasSummonedMonsterThisTurn = true;
        ((MonsterCard) selectedCard).setDefenseMode(true);
        theSummonedMonsterThisTurn = selectedCard;
        Printer.prompt("set successfully");
    }

    public void flipSummon() {
//      TODO : PASHA
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
        selectedCard.setFaceUp(false);
        Printer.prompt("flip summoned successfully");
    }

    public void attack(String command) {
//      TODO : PASHA
        Matcher matcher = Utility.getCommandMatcher(command, regexAttackNormal);
        matcher.matches();
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
        if (positionToAttack > 0 && positionToAttack < 6 && opponent.getMonstersFieldList()[positionToAttack] != null) {
            ((MonsterCard) selectedCard).attackTo(opponent.getMonstersFieldList()[positionToAttack]);
//          TODO : NOTE : MonsterCard.attack() should handle these things
        }

    }

    public void attackDirect() {
//      TODO : PASHA
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
        opponent.setLifePoint(opponent.getLifePoint() - ((MonsterCard) selectedCard).getCardAttack());
        ((MonsterCard) selectedCard).setHasAttackedThisTurn(true);
        Printer.prompt("your opponent receives " + ((MonsterCard) selectedCard).getCardAttack() + " battle damage");
    }

    public void activateEffect() {
//      TODO : PASHA
    }

    public void setSpell() {
//      TODO : PASHA
    }

    public void setTrap() {
//      TODO : PASHA
    }

    public void showGraveyard() {
        if (Utility.checkAndPrompt(graveyard.isEmpty(), "graveyard empty")) return;
        int i = 0;
        for (Card deadCard : graveyard.getAllCards()) {
            i ++;
            Printer.prompt(i + ". " + deadCard.getCardName() + ":" + deadCard.getCardDescription());
        }
    }

    public void showSelectedCard() {
        if (Utility.checkAndPrompt(
                selectedCard == null,
                "no card is selected yet")) return;
        if (Utility.checkAndPrompt(
                selectedCard.getPlayer() == opponent && !selectedCard.isFaceUp(),
                "card is not visible" )) return;
        Printer.showCard(selectedCard);
    }

    public void forfeit() {
        isLooser = true; // ENJOY THE DESIGN: THE COMPLICATED FUNCTION "FORFEIT" IS HANDLED IN ONE LINE :)
    }

    private Card drawCard() {
        return remainingDeck.pop();
    }


}
