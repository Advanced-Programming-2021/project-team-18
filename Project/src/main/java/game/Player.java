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
    private static final String regexshowGraveyard = "show\\sgraveyard";
    private static final String regexshowSelectedCard = "card\\sshow\\s\\-\\-selected";
    private static final String regexSummon = "summon";
    private static final String regexSet = "set.*";
    private static final String regexFlipSummon = "flip\\-summon";
    private static final String regexAttackNormal = "attack\\s(\\d+)";
    private static final String regexAttackDirect = "attack\\sdirect";
    private static final String regexActivateEffect = "activate\\seffect";


    private int turn;
    private User user;
    private Deck graveyard;
    private Deck remainingDeck;
    private MonsterCard[] monstersFieldList;
    private Card[] spellsAndTrapFieldList;
    private SpellCard fieldZone;
    private Player opponent;
    private Deck hand;
    private int lifePoint;
    private Game game;

    private Card selectedCard;
    private boolean hasSummonedMonsterThisTurn = false; // has to be reset at end phase

    public Player(User user, Deck deck) {
        this.user = user;
        this.remainingDeck = deck;
        monstersFieldList = new MonsterCard[6];
        spellsAndTrapFieldList = new Card[6];
        lifePoint = 8000;
        hand = new Deck();
        graveyard = new Deck();
        // TODO
    }

    public void runCommonCommands(String command) {
        if (Utility.getCommandMatcher(command, regexSelect).matches()) {
            selectCard(command);
        } else if (Utility.getCommandMatcher(command, regexshowGraveyard).matches()) {
            showGraveyard();
        } else if (Utility.getCommandMatcher(command, regexshowSelectedCard).matches()) {
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

    public void drawPhase() {
//      TODO : SINA
    }

    public void standbyPhase() {
//      TODO : SINA
    }

    public void mainPhase1() {
//      TODO : PASHA
        Printer.showBoard(this, opponent);
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
        for (int i = 1; i <= 5; i++){
            if(monstersFieldList[i] != null)monstersFieldList[i].setHasAttackedThisTurn(false);
        }
        hasSummonedMonsterThisTurn = false;
        Printer.prompt("its " + opponent.getUser().getNickname() + "’s turn");
    }

    public int getSelectedMonsterCardOnFieldID() {
        for (int i = 1; i <= 5; ++i)
            if (selectedCard == monstersFieldList[i])
                return i;
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
        if (selectedCard.isFaceUp()) {
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

    public void drawCard() {
//      TODO : SINA
    }
}
