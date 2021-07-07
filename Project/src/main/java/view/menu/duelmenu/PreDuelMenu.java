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
import lombok.SneakyThrows;
import menus.MenuController;
import utility.Utility;
import view.UtilityView;
import view.View;
import view.animations.HeadAndTailTransition;
import view.menu.mainmenu.MainMenuView;

public class PreDuelMenu extends View {
    private static User currentUser;
    public TextField textField;
    private static HeadAndTailTransition transition = null;
    public ImageView coinView;

    public void onDuelAI() {
        // todo
    }

    public static void setCurrentUser(User currentUser) {
        MenuController.getInstance().setUser(currentUser);
        PreDuelMenu.currentUser = currentUser;
    }

    public void onDuel() {
        User user = User.getUserByUsername(textField.getText());
        if (user == null) {
            UtilityView.displayMessage("no user exists with this username");
            return;
        }
        if (user.equals(MenuController.getInstance().getUser())) {
            UtilityView.displayMessage("you cannot start a game with yourself :)");
            return;
        }
        boolean randomBoolean = Utility.getARandomNumber(2) == 0;
        coinView.setLayoutX(300);
        coinView.setLayoutY(300);
        transition = new HeadAndTailTransition(coinView, Duration.millis(200), 3, randomBoolean);
        transition.setCycleCount(-1);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.play();
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    Thread.sleep(500);
                    if (transition.isHasStopped())
                        break;
                }
                if (randomBoolean)
                    startNewGame(currentUser, user);
                else
                    startNewGame(user, currentUser);
            }
        }).start();
    }

    @SneakyThrows
    private MainGameMenu loadGameScreen(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML/" + "main_game" + ".fxml"));
        Parent root = loader.load();
        MainGameMenu controller = (MainGameMenu) loader.getController();
        Stage stage = new Stage();
        stage.setScene((new Scene(root)));
        stage.show();
        return controller;
    }
    @SneakyThrows
    private void startNewGame(User firstUser, User secondUser) {
        Game game = new Game(firstUser , secondUser , 1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainGameMenu firstController = loadGameScreen(firstUser);
                MainGameMenu secondController = loadGameScreen(secondUser);
                firstController.setGame(game);
                secondController.setGame(game);
                firstController.setUser(firstUser);
                secondController.setUser(secondUser);
                stage.close();
            }
        });

    }


    @SneakyThrows
    public void backButton() {
        ((MainMenuView) (loadView("main_menu"))).adjustScene();

    }
}
