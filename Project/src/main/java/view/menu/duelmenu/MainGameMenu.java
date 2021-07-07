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

    public void refresh() {
        // do all updates here
        fieldGridPane.getChildren().clear();
        refreshSelectedCardDetails();
        refreshMyHand();
        refreshOpponentHand();
        refreshMyMonsters();

    }

    private ImageView getCardImageView(Card card , int x , int y , boolean changeSelectedCard) {
        Image image = card.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(fieldGridPane.getCellBounds(x , y).getHeight());
        imageView.setFitWidth(fieldGridPane.getCellBounds(x , y).getWidth());
        if(changeSelectedCard)
            imageView.setOnMouseClicked(mouseEvent -> {
                myPlayer.setSelectedCard(card);
                refresh();
            });
        return imageView;
    }
    @SneakyThrows
    private ImageView getUnknownImageView(Card card , int x , int y , boolean changeSelectedCard) {
        Image image = new Image(getClass().getResource("/cards_images/Unknown.jpg").toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(fieldGridPane.getCellBounds(x , y).getHeight());
        imageView.setFitWidth(fieldGridPane.getCellBounds(x , y).getWidth());
        return imageView;
    }

    private void refreshMyHand() { // [1,10] ... [13,10]
        int handSize = myPlayer.getHand().getSize();
        for(int i = 0;i < handSize;++ i) {
            fieldGridPane.add(getCardImageView(myPlayer.getHand().getCardsList().get(i) , 1 + 2 * i , 10 , true) , 1 + 2 * i , 10);
        }
    }
    private void refreshOpponentHand() { // [1,0] ... [13,0]
        int handSize = myPlayer.getOpponent().getHand().getSize();
        for(int i = 0;i < handSize;++ i) {
            fieldGridPane.add(getUnknownImageView(myPlayer.getOpponent().getHand().getCardsList().get(i) ,1 + 2 * i , 0 , false) , 1 + 2 * i , 0);
        }
    }
    @SneakyThrows
    private void refreshSelectedCardDetails() {
        Card selectedCard = myPlayer.getSelectedCard();
        cardDescription.setText("card description");
        cardTitle.setText("Card title");
        cardImageView.setImage(new Image(getClass().getResource("/cards_images/Unknown.jpg").toURI().toString()));
        if(selectedCard == null) return ;
        cardDescription.setText(selectedCard.getCardDescription());
        cardTitle.setText(selectedCard.getCardName());
        cardImageView.setImage(Card.getCardByName(selectedCard.getCardName()).getImage());
    }
    private void refreshMyMonsters() { // [3 , 6] ... [11 , 6]
        for(int i = 0;i < Player.getFIELD_SIZE();++ i)
            if(myPlayer.getMonstersFieldList()[i] != null) {
                Card card = myPlayer.getMonstersFieldList()[i];
                if(card.isFaceUp())
                    fieldGridPane.add(getCardImageView(myPlayer.getMonstersFieldList()[i] , 3 + 2 * i , 6 , true) , 3 + 2 * i , 6);
                else
                    fieldGridPane.add(getUnknownImageView(myPlayer.getMonstersFieldList()[i], 3 + 2 * i , 6 , true) , 3 + 2 * i , 6);
            }
    }

}
