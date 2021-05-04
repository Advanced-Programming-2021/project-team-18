package game;

import card.Card;
import events.Event;
import java.util.ArrayList;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private boolean turn;
    private ArrayList<Event> eventsList;
    private ArrayList<Card> cardChain;

    public Game(User firstUser , User secondUser) {
        firstPlayer = new Player(firstUser , firstUser.getActiveDeck().getMainDeck());
        secondPlayer = new Player(secondUser , secondUser.getActiveDeck().getMainDeck());
//        TODO
        firstPlayer.setGame(this);
        secondPlayer.setGame(this);
        firstPlayer.setOpponent(secondPlayer);
        secondPlayer.setOpponent(firstPlayer);
    }

    public void runGame() {
//        TODO
    }

    public void endGame(Player winner) {
//      TODO : SINA
    }

    public void endGameWithTie() {
//      TODO : KAMYAR
    }
}
