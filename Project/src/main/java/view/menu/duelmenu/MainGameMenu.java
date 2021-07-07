package view.menu.duelmenu;

import game.Game;
import game.User;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import view.View;

import java.net.URL;
import java.util.ResourceBundle;

public class MainGameMenu extends View implements Initializable {
    public ImageView cardImageView;
    public Text cardTitle;
    public Text cardDescription;
    public GridPane FieldGridPane;
    public Label firstPlayerTitle;
    public Label secondPlayerTitle;
    public ImageView firstPlayerAvatar;
    public ImageView secondPlayerAvatar;
    @Setter
    private Game game;
    @Setter
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void refresh() {
        // do all updates here
    }
}
