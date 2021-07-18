package view.menu.duelmenu;

import controller.duelmenu.PreDuelController;
import game.Game;
import game.User;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.SneakyThrows;
import menus.MenuController;
import utility.Utility;
import view.UtilityView;
import view.View;
import view.animations.HeadAndTailTransition;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class PreDuelMenu extends View implements Initializable {

    public TextField textField;
    private static HeadAndTailTransition transition = null;
    public ImageView coinView;
    AtomicBoolean searchForAvailableGame = new AtomicBoolean(true);
    AtomicBoolean searchForStartingGame = new AtomicBoolean(true);

    public void onDuelAI() {
        // todo server
//        if (currentUser.getActiveDeckName() == null) {
//            UtilityView.showError("please pick an active deck first!");
//            return;
//        }
//        startNewGame();
    }

    public void onDuel() {
        String opponent = textField.getText();
        String response = PreDuelController.requestDuel(MenuController.getInstance().getToken() , opponent);
        if(response.equals("request was sent to opponent"))
            UtilityView.displayMessage(response);
        else
            UtilityView.showError(response);

    }
    private void flipCoin(boolean turn) {
        coinView.setLayoutX(300);
        coinView.setLayoutY(300);
        transition = new HeadAndTailTransition(coinView, Duration.millis(200), 3, turn);
        transition.setCycleCount(-1);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.play();
        new Thread(() -> {
            do {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!transition.isHasStopped());
            stageNewGamePage();
        }).start();
    }

    @SneakyThrows
    private MainGameMenu loadGameScreen() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML/" + "main_game" + ".fxml"));
        Parent root = loader.load();
        MainGameMenu controller = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setResizable(false);
        stage.setWidth(800);
        stage.setHeight(660);
        stage.setFullScreen(false);
        controller.setMyStage(stage);
        return controller;
    }

    @SneakyThrows
    private void stageNewGamePage() {
        Platform.runLater(() -> {
            MainGameMenu controller = loadGameScreen();
            stage.hide();
            controller.refresh();
        });
    }

    // Starts a new game with AI
    private void startNewGame() {
        // todo server
//        Game game = new Game(currentUser, null, 1);
//        Platform.runLater(()-> {
//            MainGameMenu controller = loadGameScreen();
//            controller.setGame(game);
//            controller.setMyUser(currentUser);
//            game.runGame();
//            controller.setMyPlayer(game.getFirstPlayer());
//            Game.setFirstPlayerGraphicsController(controller);
//            Game.setSecondPlayerGraphicsController(null);
//            stage.hide();
//            controller.refresh();
//        });
    }

    @SneakyThrows
    public void backButton() {
        searchForAvailableGame.set(false);
        searchForStartingGame.set(false);
        loadView("main_menu");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Thread searchForGameThread = new Thread(() -> {
            while(searchForAvailableGame.get()) {
                try {
                    Thread.sleep(1000);
                    String response = PreDuelController.checkRequest(MenuController.getInstance().getToken());
                    if(response.equals("no user was found"))
                        continue ;
                    Platform.runLater(() -> {
                        String userResponse = UtilityView.obtainInformationInList("do you want to play with " + response + " ?" , Utility.makeArray("yes" , "no"));
                        if(userResponse.equals("yes")) {
                            PreDuelController.acceptRequest(MenuController.getInstance().getToken());
                            searchForAvailableGame.set(false);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread searchForStartingGameThread = new Thread(() -> {
            while(searchForStartingGame.get()) {
                try {
                    Thread.sleep(1000);
                    String response = PreDuelController.receiveLastMessage(MenuController.getInstance().getToken());
                    if(response.equals("no message was found"))
                        continue ;
                    if(response.equals("game incoming")) {
                        searchForStartingGame.set(false);
                        flipCoin(false);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        searchForGameThread.start();
        searchForStartingGameThread.start();
    }
}
