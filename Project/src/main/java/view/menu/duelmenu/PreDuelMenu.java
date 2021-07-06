package view.menu.duelmenu;

import game.User;
import javafx.animation.Interpolator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;
import view.animations.CoinTransition;
import view.animations.HeadAndTailTransition;

import java.util.Random;

public class PreDuelMenu extends View {
    @Setter
    private static User currentUser;
    public TextField textField;
    private static HeadAndTailTransition transition = null;
    public ImageView coinView;
    public void onDuelAI(MouseEvent mouseEvent) {
        // todo
    }

    public void onDuel(MouseEvent mouseEvent) {
        User user = User.getUserByUsername(textField.getText());
        if(user == null) {
            UtilityView.displayMessage("no user exists with this username");
            return ;
        }
        boolean randomBoolean = (new Random()).nextBoolean();
        coinView.setLayoutX(300);
        coinView.setLayoutY(300);
        transition = new HeadAndTailTransition(coinView, Duration.millis(200) , 5 , randomBoolean);
        transition.setCycleCount(-1);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.play();
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(300);
                if(transition.isHasStopped()) {
                    // todo goto game
                }
            }
        }).run();
    }


    @SneakyThrows
    public void backButton(MouseEvent mouseEvent) {
        loadView("main_menu");
    }
}
