package game;

import card.Card;
import com.google.gson.annotations.Expose;
import data.Printer;
import events.Phase;
import lombok.Getter;
import lombok.Setter;
import view.menu.duelmenu.MainGameMenu;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class Game {
    @Getter
    private static ArrayList<Game> games = new ArrayList<>();
    @Expose
    private Player activePlayer;
    @Expose
    private final User firstUser;
    @Expose
    private final User secondUser;
    private final HashMap<User, Integer> scores;
    private final HashMap<User, Integer> maxLP;
    private final int duelsCount;
    @Expose
    private Phase currentPhase;
    @Expose
    private Player firstPlayer;
    @Expose
    private Player secondPlayer;
    @Expose
    private int turn;
    @Setter
    @Expose
    private boolean isGameFinished;

    @Setter
    private transient MainGameMenu firstPlayerGraphicsController;
    @Setter
    private transient MainGameMenu secondPlayerGraphicsController;

    public static Game getGameByToken(String token) {
        Game chosenGame = null;
        for(Game game : games)
            if(game.getFirstUser().getToken().equals(token) || game.getSecondUser().getToken().equals(token))
                chosenGame = game;
        return chosenGame;
    }

    public Player getPlayerByUser(User user) {
        if(user.getUsername().equals(firstPlayer.getUser().getUsername()))
            return firstPlayer;
        return secondPlayer;
    }
    // Note: A Game consists of some duels (namely, consists of "duelsCount" duels)
    // If secondUser is null, the game starts between firstUser and Computer
    public Game(User firstUser, User secondUser, int duelsCount) {
        this.firstUser = firstUser;
        if (secondUser == null) {
            this.secondUser = User.getDummyUser();
            System.out.println("GAME STARTED WITH COMPUTER!");
        } else {
            this.secondUser = secondUser;
            System.out.println("GAME STARTED WITH USER!");
        }
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
        firstPlayer.setShouldRefresh(true);
        secondPlayer.setShouldRefresh(true);
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
        // todo
//        Printer.prompt(activePlayer , winner.getUser().getUsername() + " won the game and the score is: <" +
//                scores.get(firstUser) + ">-<" + scores.get(secondUser) + ">"
//        );
//        if (scores.get((winner.getUser())) > duelsCount / 2) {
//            Printer.prompt(activePlayer , winner.getUser().getUsername() + " won the whole match with score: <" +
//                    scores.get(firstUser) + ">-<" + scores.get(secondUser) + ">"
//            );
//            isGameFinished = true;
//            giveAwards(winner.getUser());
//        }
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

    public void  proceedNextPhase() {
        switch (currentPhase) {
            case DRAW:
                ++turn;
                currentPhase = Phase.STANDBY;
                activePlayer.drawPhase();
                break;
            case STANDBY:
                currentPhase = Phase.MAIN1;
                activePlayer.standbyPhase();
                break;
            case MAIN1:
                currentPhase = Phase.BATTLE;
                activePlayer.mainPhase1();
                break;
            case BATTLE:
                currentPhase = Phase.MAIN2;
                activePlayer.battlePhase();
                break;
            case MAIN2:
                currentPhase = Phase.END;
                activePlayer.mainPhase2();
                break;
            case END:
                currentPhase = Phase.DRAW;
                activePlayer = activePlayer.opponent;
                activePlayer.getOpponent().endPhase();
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
