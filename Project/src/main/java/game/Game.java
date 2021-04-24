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
        firstPlayer = new Player(firstUser);
        secondPlayer = new Player(secondUser);
    }

    public void runGame() {

    }
}
