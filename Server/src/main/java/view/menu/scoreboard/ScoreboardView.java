package view.menu.scoreboard;

import game.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import menus.MenuController;
import view.View;
import view.menu.mainmenu.MainMenuView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardView extends View {
    public GridPane gridPane;
    private static User user;

    public static void setUser(User user) {
        ScoreboardView.user = user;
        MenuController.getInstance().setUser(user);
    }

    @FXML
    public void initialize() {
        List<User> usersList = new ArrayList<>(User.getAllUsers());
        Collections.sort(usersList);
        int size = Math.min(usersList.size(), 20);
        for (int i = 0; i < size; i++) {
            User user1 = usersList.get(i);
            if (user1.getUsername().contentEquals(user.getUsername())) {
                Label indexLabel = numericLabel(String.valueOf(i + 1));
                Label usernameText = new Label(user1.getNickname());
                Label scoreText = new Label(String.valueOf(user1.getScore()));
                indexLabel.setTextFill(Color.BLUE);
                usernameText.setTextFill(Color.BLUE);
                scoreText.setTextFill(Color.BLUE);
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
        MainMenuView.setCurrentUser(MenuController.getInstance().getUser());
        loadView("main_menu");
    }
}
