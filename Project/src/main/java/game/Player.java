package game;

import card.Card;
import card.MonsterCard;
import card.SpellCard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
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

    public Player(User user) {
        this.user = user;
        // TODO
    }

    public void drawCard() {

    }

    public void playCard(int cardIndexInHand, int indexInBoard) {

    }

    public void sacrificeCard(int cardIndexInHand) {

    }

    public void activateSpellOrTrap(int indexInBoard) {

    }

    public void playFieldZoneSpell(int cardIndexInHand) {

    }

    public void decreaseLP(int amount) {

    }

    public void chooseCard(Deck deck) {

    }
}
