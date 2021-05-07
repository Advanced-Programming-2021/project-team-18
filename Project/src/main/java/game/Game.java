package game;

import card.Card;
import data.Printer;
import events.Event;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    private final User firstUser;
    private final User secondUser;
    private Player firstPlayer;
    private Player secondPlayer;
    private int turn;
    private final int duelsCount;
    private boolean isGameFinished;
    private ArrayList<Event> eventsList;
    private ArrayList<Card> cardChain;
    private HashMap<User, Integer> scores;

    // Note: A Game consists of some duels (namely, consists of "duelsCount" duels)
    // If secondUser is null, the game starts between firstUser and Computer
    public Game(User firstUser, User secondUser, int duelsCount) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        isGameFinished = false;
        this.duelsCount = duelsCount;
    }

    boolean isFirstTurn() {
        return turn == 1;
    }

    public int getTurn() {
        return turn;
    }

    public boolean isDuelFinished() {
        return firstPlayer.isLooser() || secondPlayer.isLooser();
    }

    public void endGame(Player winner) {
        scores.put(winner.getUser(), scores.get(winner.getUser()) + 1);
        Printer.prompt(winner.getUser().getUsername() + " won the game and the score is: <" +
                scores.get(firstUser) + ">-<" + scores.get(secondUser) + ">"
        );
        if (scores.get((winner.getUser())) > duelsCount / 2) {
            Printer.prompt(winner.getUser().getUsername() + "won the whole match with score: <" +
                    scores.get(firstUser) + ">-<" + scores.get(secondUser) + ">"
            );
            isGameFinished = true;
        }
    }

    private void startNewDuel() {
        firstPlayer = new Player(firstUser, firstUser.getActiveDeck().getMainDeck());
        secondPlayer = new Player(secondUser, secondUser.getActiveDeck().getMainDeck());
        firstPlayer.setGame(this);
        secondPlayer.setGame(this);
        firstPlayer.setOpponent(secondPlayer);
        secondPlayer.setOpponent(firstPlayer);
        scores.put(firstUser, 0);
        scores.put(secondUser, 0);
        turn = 0;
    }

    private void runDuel() {
        Player activePlayer = firstPlayer;
        while (!isDuelFinished()) {
            turn ++;
            activePlayer.drawPhase();
            if (isDuelFinished()) break;
            activePlayer.standbyPhase();
            if (isDuelFinished()) break;
            activePlayer.mainPhase1();
            if (isDuelFinished()) break;
            activePlayer.battlePhase();
            if (isDuelFinished()) break;
            activePlayer.mainPhase2();
            if (isDuelFinished()) break;
            activePlayer.endPhase();
            if (activePlayer == firstPlayer) activePlayer = secondPlayer;
            else activePlayer = firstPlayer;
        }
        if (firstPlayer.isLooser()) endGame(secondPlayer);
        else endGame(firstPlayer);
    }

    public void runGame() {
        while (!isGameFinished) {
            startNewDuel();
            runDuel();
        }
    }
}
