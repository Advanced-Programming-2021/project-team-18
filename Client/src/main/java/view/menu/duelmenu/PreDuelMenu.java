package view.menu.duelmenu;

import game.Game;
import game.User;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import jdk.jshell.execution.Util;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.MenuController;
import utility.Utility;
import view.UtilityView;
import view.View;
import view.animations.HeadAndTailTransition;

public class PreDuelMenu extends View {
    @Setter
    private static String currentToken;
    public TextField textField;
    private static HeadAndTailTransition transition = null;
    public ImageView coinView;

    public void onDuelAI() {
        // todo server
//        if (currentUser.getActiveDeckName() == null) {
//            UtilityView.showError("please pick an active deck first!");
//            return;
//        }
//        startNewGame();
    }

    public void onDuel() {
        // todo server
//        User user = User.getUserByUsername(textField.getText());
//        if (user == null) {
//            UtilityView.showError("no user exists with this username");
//            return;
//        }
//        if (user.equals(currentUser)) {
//            UtilityView.showError("you cannot start a game with yourself :)");
//            return;
//        }
//        if (currentUser.getActiveDeckName() == null) {
//            UtilityView.showError("please pick an active deck first!");
//            return;
//        }
//        if (user.getActiveDeckName() == null) {
//            UtilityView.showError("selected user doesn't have an active deck");
//            return;
//        }
//        boolean randomBoolean = Utility.getARandomNumber(2) == 0;
//        coinView.setLayoutX(300);
//        coinView.setLayoutY(300);
//        transition = new HeadAndTailTransition(coinView, Duration.millis(200), 3, randomBoolean);
//        transition.setCycleCount(-1);
//        transition.setInterpolator(Interpolator.LINEAR);
//        transition.play();
//        new Thread(() -> {
//            do {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } while (!transition.isHasStopped());
//            if (randomBoolean)
//                startNewGame(currentUser, user);
//            else
//                startNewGame(user, currentUser);
//        }).start();
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
    private void startNewGame(User firstUser, User secondUser) {
        // todo server
//        Game game = new Game(firstUser, secondUser, 1);
//        Platform.runLater(() -> {
//            MainGameMenu firstController = loadGameScreen();
//            MainGameMenu secondController = loadGameScreen();
//            firstController.setGame(game);
//            secondController.setGame(game);
//            firstController.setMyUser(firstUser);
//            secondController.setMyUser(secondUser);
//            game.runGame();
//            firstController.setMyPlayer(game.getFirstPlayer());
//            secondController.setMyPlayer(game.getSecondPlayer());
//            Game.setFirstPlayerGraphicsController(firstController);
//            Game.setSecondPlayerGraphicsController(secondController);
//            stage.hide();
//            firstController.refresh();
//            secondController.refresh();
//        });
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
        loadView("main_menu");
    }
}
