package view.menu.duelmenu;

import game.User;
import javafx.animation.Interpolator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.MenuController;
import utility.Utility;
import view.UtilityView;
import view.View;
import view.animations.CoinTransition;
import view.animations.HeadAndTailTransition;
import view.menu.mainmenu.MainMenuView;

import java.util.Random;

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
                Thread.sleep(300);
                if (transition.isHasStopped()) {
                    // todo goto game
                }
            }
        }).start();
    }


    @SneakyThrows
    public void backButton() {
        ((MainMenuView)(loadView("main_menu"))).adjustScene();

    }
}
