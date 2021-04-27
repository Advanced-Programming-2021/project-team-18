package game;

import card.Card;
import card.MonsterCard;
import card.SpellCard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// TODO : SINA
public class Player {
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
    private GameDeck gameDeck;
    private Game game;
    private Card selectedCard;


    public Player(User user) {
        this.user = user;
        // TODO
    }

    public void drawPhase() {
//      TODO : SINA
    }

    public void standbyPhase() {
//      TODO : SINA
    }

    public void mainPhase1() {
//      TODO : PASHA
    }

    public void battlePhase() {
//      TODO : KAMYAR
    }

    public void mainPhase2() {
//      TODO : PASHA
    }

    public void endPhase() {
//      TODO : KAMYAR
    }

    public void showBoard() {
//      TODO : KAMYAR
    }

    public void selectCard() {
//      TODO : KAMYAR
    }

    public void summonMonster() {
//      TODO : KAMYAR
    }

    public void setMonster() {
//      TODO : KAMYAR
    }

    public void flipSummon() {
//      TODO : PASHA
    }

    public void attack() {
//      TODO : PASHA
    }

    public void attackDirect() {
//      TODO : PASHA
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
