package view.menu.shopmenu;

import card.Card;
import game.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import menus.Menu;
import menus.MenuController;
import utility.Utility;
import view.UtilityView;
import view.View;
import view.menu.mainmenu.MainMenuView;

import java.io.IOException;
import java.util.HashMap;

public class ShopMenuView extends View {

    public ScrollPane scrollPane;
    public GridPane grid;
    public BorderPane mainPane;
    public VBox initialVBox;
    public Button initialBackButton;

    private int userBalance;

    @FXML
    public void initialize() {
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        initialVBox.prefWidthProperty().bind(stage.widthProperty().multiply(.33));
        initialBackButton.prefWidthProperty().bind(initialVBox.widthProperty().multiply(.5));
        initialBackButton.prefHeightProperty().bind(initialBackButton.prefWidthProperty().multiply(35.3/98.6));
        initialBackButton.setOnAction(actionEvent -> back());
        Card card;
        ImageView cardImage;
        final int rowSize = 5;
        int i = 0;
        for (String cardName : Card.getAllCardNames()) {
            card = Card.getCardByName(cardName);
            assert card != null;
            try {
                cardImage = new ImageView(card.getImage());
            } catch (Exception e) {
                System.out.println(cardName);
                continue ;
            }
            cardImage.fitWidthProperty().bind(stage.widthProperty().multiply(.66/rowSize));
            cardImage.setPreserveRatio(true);
            Card finalCard = card;
            cardImage.setOnMouseClicked(mouseEvent -> prepareCardForConsideration(finalCard));
            grid.add(cardImage, i % rowSize, i / rowSize);
            i++;
        }
        userBalance = Integer.valueOf(Utility.getRequest(Utility.getSERVER_LOCATION() + "/api/profilemenu/get_balance", null,
                Utility.makeHashMap("token", MenuController.getInstance().getToken())));
    }

    private void prepareCardForConsideration(Card card) {
        VBox cardPane = new VBox();
        cardPane.setPrefWidth(stage.getWidth() * .33);
        cardPane.minWidthProperty().bind(stage.widthProperty().multiply(.33));
        cardPane.maxWidthProperty().bind(stage.widthProperty().multiply(.33));
        ImageView cardImage = new ImageView(card.getImage());
        cardPane.spacingProperty().bind(cardImage.fitHeightProperty().multiply(.2));
        cardImage.fitWidthProperty().bind(stage.widthProperty().multiply(.8 * .33));
        cardImage.setPreserveRatio(true);
        cardPane.getChildren().add(cardImage);
        cardPane.setAlignment(Pos.CENTER);
        Label priceLabel = new Label(String.valueOf(card.getPrice()));
        HBox hBox = new HBox(priceLabel);
        hBox.prefWidthProperty().bind(cardPane.widthProperty().multiply(.8));
        hBox.prefHeightProperty().bind(cardPane.widthProperty().multiply(.3));
        hBox.spacingProperty().bind(cardPane.widthProperty().multiply(.2));
        hBox.setAlignment(Pos.CENTER);
        int cardBalance = MenuController.getInstance().getCardBalance(card.getCardName());
        if (cardBalance > 0) {
            Label balanceLabel = new Label("Balance: " + cardBalance);
            hBox.getChildren().add(balanceLabel);
        }
        Button buyButton = new Button("Buy this card");
        if (card.getPrice() > userBalance) {
            buyButton.getStyleClass().clear();
            buyButton.getStyleClass().add("inactive-button");
        }
        else {
            buyButton.getStyleClass().add("normal-button");
            buyButton.setOnAction(actionEvent -> buyCard(card));
        }
        buyButton.resize(98.6, 35.3);
        buyButton.prefWidthProperty().bind(cardPane.widthProperty().multiply(.5));
        buyButton.prefHeightProperty().bind(buyButton.prefWidthProperty().multiply(35.3/98.6));
        Button backButton = new Button("Back");
        backButton.resize(98.6, 35.3);
        backButton.prefWidthProperty().bind(cardPane.widthProperty().multiply(.5));
        backButton.prefHeightProperty().bind(buyButton.prefWidthProperty().multiply(35.3/98.6));
        backButton.setOnAction(actionEvent -> back());
        cardPane.getChildren().addAll(hBox, buyButton, backButton);
        mainPane.setLeft(cardPane);
    }

    private void buyCard(Card card) {
        userBalance -= card.getPrice();
        Utility.send("/api/shopmenu/buy_card", "token", MenuController.getInstance().getToken(),
                "card_name", card.getCardName());
        prepareCardForConsideration(card);
        UtilityView.displayMessage("Card added successfully");
    }

    public void back(){
        try {
            loadView("main_menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
