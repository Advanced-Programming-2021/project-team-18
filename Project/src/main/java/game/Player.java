package game;

import card.Card;
import card.MonsterCard;
import card.SpellCard;
import data.Printer;
import lombok.Getter;
import lombok.Setter;
import utility.Utility;

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
    private Card theSummonedCardThisTurn;
    // Initialized by setters
    private Game game;
    private Player opponent;


    public Player(User user, Deck deck) {
        this.user = user;
        graveyard = new Deck();
        this.remainingDeck = deck;
        monstersFieldList = new MonsterCard[6];
        spellsAndTrapFieldList = new Card[6];
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
            setMonster(command);
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

    // By Sina
    public void drawPhase() {
        Printer.prompt("phase: draw phase");
        if (!game.isFirstTurn()) {
            if (remainingDeck.isEmpty()) {
                Printer.prompt("No card is remained in your deck");
                isLooser = true;
                return;
            }
            hand.addCard(drawCard());
        }
    }

    public void standbyPhase() {
        Printer.prompt("phase: standby phase");
//      TODO : SINA
    }

    public void mainPhase1() {
//      TODO : PASHA
        Printer.showBoard(this, this.opponent);
        while (true) {
            String command = Utility.getNextLine();
            runCommonCommands(command);
            runMainPhaseCommands(command);
        }
    }

    public void battlePhase() {
//      TODO : KAMYAR
    }

    public void mainPhase2() {
//      TODO : PASHA
        while (true) {
            String command = Utility.getNextLine();
            runCommonCommands(command);
            runMainPhaseCommands(command);
        }
    }

    public void endPhase() {
//      TODO : KAMYAR
//      NOTE : for each monster card .hasAttacked has to be set to false and hasSummonedMonsterThisTurn should be also set to false
        Printer.prompt("phase: End phase");
        for (int i = 1; i <= 5; i++) {
            if (monstersFieldList[i] != null) monstersFieldList[i].setHasAttackedThisTurn(false);
        }
        hasSummonedMonsterThisTurn = false;
        theSummonedCardThisTurn = null;
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
    }


    public void summonMonster() {
//      TODO : KAMYAR
//      This function should be modfied later to support
        if (selectedCard == null) {
            Printer.prompt("no card is selected yet");
            return;
        }
        int place = getSelectedCardOnHandID();
        if (!(selectedCard instanceof MonsterCard)
                || place == -1) {
            Printer.prompt("you can’t summon this card");
            return;
        }
        int placeOnField = getFirstEmptyPlaceOnMonstersField();
        if (placeOnField == -1) {
            Printer.prompt("monster card zone is full");
            return;
        }
        if (hasSummonedMonsterThisTurn) {
            Printer.prompt("you already summoned/set on this turn");
            return;
        }
        if (((MonsterCard) selectedCard).getCardLevel() <= 4) {
            monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
            hand.removeCard(place);
            hasSummonedMonsterThisTurn = true;
            theSummonedCardThisTurn = selectedCard;
            Printer.prompt("summoned successfully");
            return;
        }
        if (((MonsterCard) selectedCard).getCardLevel() == 5
                || ((MonsterCard) selectedCard).getCardLevel() == 6) {
            if (placeOnField == 1) {
                Printer.prompt("there are not enough cards for tribute");
                return;
            }
            Printer.prompt("input the address of the card you want to tribute:");
            String inp = Utility.getNextLine();
            int address = Integer.parseInt(inp);
            if (address > 5 || address < 1 || monstersFieldList[address] == null) {
                Printer.prompt("there no monsters on this address");
                return;
            }
            MonsterCard tributedMonster = monstersFieldList[address];
            monstersFieldList[address] = null;
            graveyard.getCardsList().add(tributedMonster);
            placeOnField = getFirstEmptyPlaceOnMonstersField();
            monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
            hasSummonedMonsterThisTurn = true;
            theSummonedCardThisTurn = selectedCard;
            hand.removeCard(place);
            Printer.prompt("summoned successfully");
            return;
        }
        if (((MonsterCard) selectedCard).getCardLevel() == 7
                || ((MonsterCard) selectedCard).getCardLevel() == 8) {
            if (placeOnField == 1
                    || placeOnField == 2) {
                Printer.prompt("there are not enough cards for tribute");
                return;
            }
            Printer.prompt("input two addresses for the cards you want to tribute in TWO DIFFERENT LINES:");
            int firstTribute = Integer.parseInt(Utility.getNextLine());
            int secondTribute = Integer.parseInt(Utility.getNextLine());
            if (firstTribute > 5
                    || firstTribute < 1
                    || monstersFieldList[firstTribute] == null
                    || secondTribute > 5
                    || secondTribute < 1
                    || monstersFieldList[secondTribute] == null) {
                Printer.prompt("there is no monster on one of these addresses");
                return;
            }
            MonsterCard firstTributedCard = monstersFieldList[firstTribute];
            MonsterCard secondTributedCard = monstersFieldList[secondTribute];
            monstersFieldList[firstTribute] = null;
            monstersFieldList[secondTribute] = null;
            graveyard.getCardsList().add(firstTributedCard);
            graveyard.getCardsList().add(secondTributedCard);
            hand.removeCard(place);
            placeOnField = getFirstEmptyPlaceOnMonstersField();
            monstersFieldList[placeOnField] = (MonsterCard) selectedCard;
            hasSummonedMonsterThisTurn = true;
            theSummonedCardThisTurn = selectedCard;
            Printer.prompt("summoned successfully");
        }
    }

    public void setMonster(String command) {
//      TODO : KAMYAR
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
        if (selectedCard.isFaceUp() || theSummonedCardThisTurn == selectedCard) {
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
        if (positionToAttack > 0 && positionToAttack < 6 && ((MonsterCard) opponent.getMonstersFieldList()[positionToAttack]) != null) {
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
//      TODO : SINA
    }

    public void showSelectedCard() {
//      TODO : SINA
    }

    public void forfeit() {
//      TODO : SINA
    }

    private Card drawCard() {
        return remainingDeck.pop();
    }
}
