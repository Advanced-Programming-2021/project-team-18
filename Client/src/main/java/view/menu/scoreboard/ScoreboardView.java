package view.menu.scoreboard;

import game.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Setter;
import menus.MenuController;
import view.View;
import view.menu.mainmenu.MainMenuView;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardView extends View {
    public GridPane gridPane;
    private Timeline timeline;
    @FXML
    public void initialize() {
        refresh();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(10), event -> {
            refresh();
            event.consume();
        });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void refresh() {
        ArrayList<Node> children = new ArrayList<>();
        for (Node node : gridPane.getChildren()) {
            children.add(node);
        }
        for (Node node : children) {
            gridPane.getChildren().remove(node);
        }
        List<SimplifiedUser> usersList = MenuController.getInstance().getScoreboard();
        int size = usersList.size();
        for (int i = 0; i < size; i++) {
            SimplifiedUser user1 = usersList.get(i);
            if (user1.isOnline()) {
                Label indexLabel = numericLabel(String.valueOf(i + 1));
                Label usernameText = new Label(user1.getNickname());
                Label scoreText = new Label(String.valueOf(user1.getScore()));
                indexLabel.setTextFill(Color.GREEN);
                usernameText.setTextFill(Color.GREEN);
                scoreText.setTextFill(Color.GREEN);
                gridPane.add(indexLabel, 0, i);
                gridPane.add(usernameText, 1, i);
                gridPane.add(scoreText, 2, i);
                continue;
            }
            gridPane.add(numericLabel(String.valueOf(i + 1)), 0, i);
            gridPane.add(new Label(user1.getNickname()), 1, i);
            gridPane.add(new Label(String.valueOf(user1.getScore())), 2, i);
        }
    }

    private Label numericLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("number-label");
        return label;
    }

    public void back() throws IOException {
        timeline.stop();
        loadView("main_menu");
    }
}


