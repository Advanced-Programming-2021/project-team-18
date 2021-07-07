package view.menu.duelmenu;

import card.Card;
import game.Game;
import game.Player;
import game.User;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Setter;
import lombok.SneakyThrows;
import view.View;

import java.net.URL;
import java.util.ResourceBundle;

public class MainGameMenu extends View implements Initializable {
    public ImageView cardImageView;
    public Text cardTitle;
    public Text cardDescription;
    public GridPane fieldGridPane;
    public Label firstPlayerTitle;
    public Label secondPlayerTitle;
    public ImageView firstPlayerAvatar;
    public ImageView secondPlayerAvatar;
    @Setter
    private Game game;
    @Setter
    private User myUser;
    @Setter
    private Player myPlayer;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private ImageView getHandImageView(Card card) {
        Image image = card.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(fieldGridPane.getCellBounds(1 , 10).getHeight());
        imageView.setFitWidth(fieldGridPane.getCellBounds(1 , 10).getWidth());
        return imageView;
    }
    @SneakyThrows
    private ImageView getUnknownImageView() {
        Image image = new Image(getClass().getResource("/cards_images/Unknown.jpg").toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(fieldGridPane.getCellBounds(1 , 10).getHeight());
        imageView.setFitWidth(fieldGridPane.getCellBounds(1 , 10).getWidth());
        return imageView;
    }

    public void refresh() {
        // do all updates here
        fieldGridPane.getChildren().clear();
        refreshMyHand();
        refreshOpponentHand();

    }
    private void refreshMyHand() { // [1,10] ... [13,10]

        int handSize = myPlayer.getHand().getSize();
        for(int i = 0;i < handSize;++ i) {
            fieldGridPane.add(getHandImageView(myPlayer.getHand().getCardsList().get(i)) , 1 + 2 * i , 10);
        }
    }
    private void refreshOpponentHand() { // [1,0] ... [13,0]
        int handSize = myPlayer.getHand().getSize();
        for(int i = 0;i < handSize;++ i) {
            fieldGridPane.add(getUnknownImageView() , 1 + 2 * i , 0);
        }
    }
}
