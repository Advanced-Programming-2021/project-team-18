package view.menu.scoreboard;

import game.User;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Setter;
import view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreboardView extends View {
    public GridPane gridPane;
    @Setter
    private static User user;

    @FXML
    public void initialize() {
        List<User> usersList = new ArrayList<User>(User.getAllUsers());
        Collections.sort(usersList);
        Collections.reverse(usersList);
        int size = Math.min(usersList.size(), 20);
        for (int i = 0; i < size; i++) {
            User user1 = usersList.get(i);
            if (user1.getUsername().contentEquals(user.getUsername())) {
                Text indexText = textMaker(String.valueOf(i + 1));
                Text usernameText = textMaker(user1.getNickname());
                Text scoreText = textMaker(String.valueOf(user1.getScore()));
                indexText.setStroke(Color.BLUE);
                usernameText.setStroke(Color.BLUE);
                scoreText.setStroke(Color.BLUE);
                gridPane.add(indexText, 0, i);
                gridPane.add(usernameText, 1, i);
                gridPane.add(scoreText, 2, i);
            }
            gridPane.add(textMaker(String.valueOf(i + 1)), 0, i);
            gridPane.add(textMaker(user1.getNickname()), 1, i);
            gridPane.add(textMaker(String.valueOf(user1.getScore())), 2, i);
        }
    }

    private Text textMaker(String context) {
        Text text = new Text(context);
        return text;
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        loadView("/view/FXML/mainMenu.fxml");
    }
}
