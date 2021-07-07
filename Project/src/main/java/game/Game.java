package game;

import card.Card;
import data.Printer;
import events.Phase;
import lombok.Getter;
import lombok.Setter;
import view.menu.duelmenu.MainGameMenu;

import java.util.HashMap;

@Getter
public class Game {
    private static Player activePlayer;
    private final User firstUser, secondUser;
    private final HashMap<User, Integer> scores;
    private final HashMap<User, Integer> maxLP;
    private final int duelsCount;
    private Phase currentPhase;
    private Player firstPlayer, secondPlayer;
    private int turn;
    private boolean isGameFinished;
    @Setter
    private static MainGameMenu firstPlayerGraphicsController;
    @Setter
    private static MainGameMenu secondPlayerGraphicsController;

    public static Player getActivePlayer() {
        return activePlayer;
    }

    // Note: A Game consists of some duels (namely, consists of "duelsCount" duels)
    // If secondUser is null, the game starts between firstUser and Computer
    public Game(User firstUser, User secondUser, int duelsCount) {
        this.firstUser = firstUser;
        if (secondUser == null) this.secondUser = User.getDummyUser();
        else this.secondUser = secondUser;
        isGameFinished = false;
        this.duelsCount = duelsCount;
        scores = new HashMap<>();
        scores.put(this.firstUser, 0);
        scores.put(this.secondUser, 0);
        maxLP = new HashMap<>();
        maxLP.put(this.firstUser, 0);
        maxLP.put(this.secondUser, 0);
    }

    public void notifyGraphic() {
        System.out.println("notified");
        firstPlayerGraphicsController.refresh();
        secondPlayerGraphicsController.refresh();
    }

    boolean isNotFirstTurn() {
        return turn != 1;
    }

    public boolean isDuelFinished() {
        return firstPlayer.isLoser() || secondPlayer.isLoser();
    }

    private void endGame(Player winner) {
        scores.put(winner.getUser(), scores.get(winner.getUser()) + 1);
        maxLP.put(winner.getUser(), Math.max(maxLP.get(winner.getUser()), winner.getLifePoint()));
        Printer.prompt(winner.getUser().getUsername() + " won the game and the score is: <" +
                scores.get(firstUser) + ">-<" + scores.get(secondUser) + ">"
        );
        if (scores.get((winner.getUser())) > duelsCount / 2) {
            Printer.prompt(winner.getUser().getUsername() + " won the whole match with score: <" +
                    scores.get(firstUser) + ">-<" + scores.get(secondUser) + ">"
            );
            isGameFinished = true;
            giveAwards(winner.getUser());
        }
    }

    private void drawInitialCards(Player player) {
        player.getRemainingDeck().shuffleDeck();
        for (int i = 0; i < 5; ++i)
            player.getHand().addCard(player.getRemainingDeck().pop());
    }

    private void makeCardsReady(Player player) {
        for (Card card : player.getRemainingDeck().getCardsList()) {
            card.setPlayer(player);
            card.setFaceUp(false);
            card.manageEffect();
        }
    }

    private void startNewDuel() {
        firstPlayer = new Player(firstUser,
                firstUser.getGameDeckByName(firstUser.getActiveDeckName()).getMainDeck().cloneDeck());
        if (secondUser != User.getDummyUser()) secondPlayer = new Player(secondUser,
                secondUser.getGameDeckByName(secondUser.getActiveDeckName()).getMainDeck().cloneDeck());
        else secondPlayer = new AIPlayer();

        makeCardsReady(firstPlayer);
        makeCardsReady(secondPlayer);

        firstPlayer.setGame(this);
        secondPlayer.setGame(this);
        firstPlayer.setOpponent(secondPlayer);
        secondPlayer.setOpponent(firstPlayer);
        drawInitialCards(firstPlayer);
        drawInitialCards(secondPlayer);
        turn = 0;
    }

    private void runDuel() {
//        while (!isDuelFinished()) {
//            turn++;
//            activePlayer.drawPhase();
//            if (isDuelFinished()) break;
//            activePlayer.standbyPhase();
//            if (isDuelFinished()) break;
//            activePlayer.mainPhase1();
//            if (isDuelFinished()) break;
//            activePlayer.battlePhase();
//            if (isDuelFinished()) break;
//            activePlayer.mainPhase2();
//            if (isDuelFinished()) break;
//            activePlayer.endPhase();
//            if (activePlayer == firstPlayer) activePlayer = secondPlayer;
//            else activePlayer = firstPlayer;
//        }
//        if (firstPlayer.isLoser()) endGame(secondPlayer);
//        else endGame(firstPlayer);
    }

    public void proceedNextPhase() {
        if(currentPhase == Phase.DRAW) {
            ++ turn;
            currentPhase = Phase.STANDBY;
            activePlayer.drawPhase();
        } else if(currentPhase == Phase.STANDBY) {
            currentPhase = Phase.MAIN1;
            activePlayer.standbyPhase();
        } else if(currentPhase == Phase.MAIN1) {
            currentPhase = Phase.BATTLE;
            activePlayer.mainPhase1();
        } else if(currentPhase == Phase.BATTLE) {
            currentPhase = Phase.MAIN2;
            activePlayer.battlePhase();
        } else if(currentPhase == Phase.MAIN2) {
            currentPhase = Phase.END;
            activePlayer.mainPhase2();
        } else  {
            currentPhase = Phase.DRAW;
            activePlayer = activePlayer.opponent;
            activePlayer.endPhase();
        }
    }

    private void giveAwards(User winner) {
        User loser = (winner.equals(firstUser) ? secondUser : firstUser);
        winner.increaseBalance(duelsCount * 1000 + duelsCount * maxLP.get(winner));
        winner.increaseScore(duelsCount * 1000);
        loser.increaseBalance(duelsCount * 100);
    }

    public void runGame() {
        startNewDuel();
        currentPhase = Phase.DRAW;
        activePlayer = firstPlayer;

    }
}
