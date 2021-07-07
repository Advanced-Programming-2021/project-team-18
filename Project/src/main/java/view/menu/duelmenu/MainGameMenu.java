package view.menu.duelmenu;

import card.Card;
import events.Phase;
import game.Game;
import game.Player;
import game.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;

import java.net.URL;
import java.util.ResourceBundle;

public class MainGameMenu extends View implements Initializable {

    @FXML
    private ScrollPane buttonsScrollPane;
    @FXML
    private ImageView cardImageView;
    @FXML
    private Text cardTitle;
    @FXML
    private Text cardDescription;
    @FXML
    private GridPane fieldGridPane;
    @FXML
    private Label firstPlayerTitle;
    @FXML
    private Label secondPlayerTitle;
    @FXML
    private ImageView firstPlayerAvatar;
    @FXML
    private ImageView secondPlayerAvatar;
    @FXML
    private VBox buttonsVBox;
    @FXML
    private Text firstPlayerLP;
    @FXML
    private Text secondPlayerLP;
    @FXML
    private Text phaseNameText;
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
        buttonsVBox.getChildren().clear();
        refreshAvatarAndTitles();
        refreshSelectedCardDetails();
        refreshMyHand();
        refreshOpponentHand();
        refreshMyMonsters();
        refreshOpponentMonsters();
        refreshButtonsVBox();
    }

    private void refreshAvatarAndTitles() {
        firstPlayerAvatar.setImage(UtilityView.getAvatarImage(myUser.getAvatarID()));
        secondPlayerAvatar.setImage(UtilityView.getAvatarImage(myPlayer.getOpponent().getUser().getAvatarID()));
        firstPlayerTitle.setText(myUser.getNickname());
        secondPlayerTitle.setText(myPlayer.getOpponent().getUser().getNickname());
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
        if(changeSelectedCard)
            imageView.setOnMouseClicked(mouseEvent -> {
                myPlayer.setSelectedCard(card);
                refresh();
            });
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
            fieldGridPane.add(getUnknownImageView(myPlayer.getOpponent().getHand().getCardsList().get(i) ,1 + 2 * i , 0 , true) , 1 + 2 * i , 0);
        }
    }
    @SneakyThrows
    private void refreshSelectedCardDetails() {
        Card selectedCard = myPlayer.getSelectedCard();
        cardDescription.setText("card description");
        cardTitle.setText("Card title");
        cardImageView.setImage(new Image(getClass().getResource("/cards_images/Unknown.jpg").toURI().toString()));
        if(selectedCard == null || (!selectedCard.isFaceUp() && selectedCard.getPlayer() == myPlayer.getOpponent())) return ;
        cardDescription.setText(selectedCard.getCardDescription());
        cardTitle.setText(selectedCard.getCardName());
        if(selectedCard.isFaceUp() || selectedCard.getPlayer() == myPlayer)
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
    private void refreshOpponentMonsters() { // [3 , 4] ... [11 , 4]
        for(int i = 0;i < Player.getFIELD_SIZE();++ i)
            if(myPlayer.getOpponent().getMonstersFieldList()[i] != null) {
                Card card = myPlayer.getOpponent().getMonstersFieldList()[i];
                if(card.isFaceUp())
                    fieldGridPane.add(getCardImageView(myPlayer.getOpponent().getMonstersFieldList()[i] , 3 + 2 * i , 4 , true) , 3 + 2 * i , 4);
                else
                    fieldGridPane.add(getUnknownImageView(myPlayer.getOpponent().getMonstersFieldList()[i], 3 + 2 * i , 4 , true) , 3 + 2 * i , 4);
            }
    }
    private void refreshButtonsVBox() {
        if(Game.getActivePlayer() != myPlayer)
            return ;
        Button nextPhaseButton = new Button("next phase");
        nextPhaseButton.setOnMouseClicked(event -> {
            game.proceedNextPhase();
            refresh();
        });
        buttonsVBox.getChildren().add(nextPhaseButton);
        if(game.getCurrentPhase() == Phase.MAIN1 || game.getCurrentPhase() == Phase.MAIN2) {
            Button summonButton = new Button("summon");
            summonButton.setOnMouseClicked(event -> {
                myPlayer.summonMonster();
                refresh();
            });
            Button setButton = new Button("set");
            setButton.setOnMouseClicked(event -> {
                myPlayer.setMonster();
                refresh();
            });
            buttonsVBox.getChildren().addAll(summonButton , setButton);
        }
    }

}
