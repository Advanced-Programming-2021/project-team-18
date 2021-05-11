package game;

import card.Card;
import data.Printer;
import events.Event;


import java.util.ArrayList;
import java.util.HashMap;


public class Game {
    private final User firstUser, secondUser;
    private final ArrayList<Event> eventsList;
    private final ArrayList<Card> cardChain;
    private final HashMap<User, Integer> scores;
    private final HashMap<User, Integer> maxScores;
    private final int duelsCount;

    private Player firstPlayer, secondPlayer;
    private int turn;
    private boolean isGameFinished;

    // Note: A Game consists of some duels (namely, consists of "duelsCount" duels)
    // If secondUser is null, the game starts between firstUser and Computer
    public Game(User firstUser, User secondUser, int duelsCount) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        isGameFinished = false;
        this.duelsCount = duelsCount;
        eventsList = new ArrayList<>();
        cardChain = new ArrayList<>();
        scores = new HashMap<>();
        maxScores = new HashMap<>();
        maxScores.put(firstUser, 0);
        maxScores.put(secondUser, 0);
    }

    boolean isFirstTurn() {
        return turn == 1;
    }

    public int getTurn() {
        return turn;
    }

    public boolean isDuelFinished() {
        return firstPlayer.isLoser() || secondPlayer.isLoser();
    }

    private void endGame(Player winner) {
        scores.put(winner.getUser(), scores.get(winner.getUser()) + 1);
        Printer.prompt(winner.getUser().getUsername() + " won the game and the score is: <" +
                scores.get(firstUser) + ">-<" + scores.get(secondUser) + ">"
        );
        if (scores.get((winner.getUser())) > duelsCount / 2) {
            Printer.prompt(winner.getUser().getUsername() + "won the whole match with score: <" +
                    scores.get(firstUser) + ">-<" + scores.get(secondUser) + ">"
            );
            isGameFinished = true;
            giveAwards(winner.getUser());
        }

        maxScores.put(firstUser, Math.max(maxScores.get(firstUser), scores.get(firstUser)));
        maxScores.put(secondUser, Math.max(maxScores.get(secondUser), scores.get(secondUser)));
    }

    private void drawInitialCards(Player player) {
        player.getRemainingDeck().shuffleDeck();
        for(int i = 0;i < 5;++ i)
            player.getHand().addCard(player.getRemainingDeck().pop());
    }

    private void startNewDuel() {
        firstPlayer = new Player(firstUser, firstUser.getActiveDeck().getMainDeck().cloneDeck());
        secondPlayer = new Player(secondUser, secondUser.getActiveDeck().getMainDeck().cloneDeck());
        firstPlayer.setGame(this);
        secondPlayer.setGame(this);
        firstPlayer.setOpponent(secondPlayer);
        secondPlayer.setOpponent(firstPlayer);
        scores.put(firstUser, 0);
        scores.put(secondUser, 0);
        drawInitialCards(firstPlayer);
        drawInitialCards(secondPlayer);
        turn = 0;
    }

    private void runDuel() {
        Player activePlayer = firstPlayer;
        while (!isDuelFinished()) {
            turn++;
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
        if (firstPlayer.isLoser()) endGame(secondPlayer);
        else endGame(firstPlayer);
    }

    private void giveAwards(User winner) {
        User loser = (winner.equals(firstUser) ? secondUser : firstUser);
        if (duelsCount == 1) {
            winner.increaseBalance(1000 + maxScores.get(winner));
            winner.increaseScore(1000);
            loser.increaseBalance(100);
            return;
        }
        winner.increaseBalance(3000 + 3 * maxScores.get(winner));
        winner.increaseScore(3000);
        loser.increaseBalance(300);
    }

    public void runGame() {
        while (!isGameFinished) {
            startNewDuel();
            runDuel();
        }
    }
}
