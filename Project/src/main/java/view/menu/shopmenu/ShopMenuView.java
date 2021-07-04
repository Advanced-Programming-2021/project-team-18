package view.menu.shopmenu;

import card.Card;
import game.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import menus.MenuController;
import view.UtilityView;
import view.View;

public class ShopMenuView extends View {

    public ScrollPane scrollPane;
    public GridPane grid;
    public Text cardNameText;
    public Rectangle textBox;
    public BorderPane mainPane;

    @FXML
    public void initialize() {
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        textBox.widthProperty().bind(stage.widthProperty().multiply(.33));
        Card card;
        ImageView cardImage;
        final int rowSize = 5;
        int i = 0;
        for (String cardName : Card.getAllCardNames()) {
            card = Card.getCardByName(cardName);
            assert card != null;
            try {
                cardImage = new ImageView(card.getImage());
                cardImage.fitWidthProperty().bind(stage.widthProperty().multiply(.66/rowSize));
                cardImage.setPreserveRatio(true);
                Card finalCard = card;
                cardImage.setOnMouseClicked(mouseEvent -> prepareCardForConsideration(finalCard));
                grid.add(cardImage, i % rowSize, i / rowSize);

                i++;
            } catch (Exception e) {
                System.out.println(cardName);
            }
        }
    }

    private void prepareCardForConsideration(Card card) {
        VBox cardPane = new VBox();
        cardPane.setPrefWidth(stage.getWidth() * .33);
        cardPane.minWidthProperty().bind(stage.widthProperty().multiply(.33));
        ImageView cardImage = new ImageView(card.getImage());
        cardPane.spacingProperty().bind(cardImage.fitHeightProperty().multiply(.2));
        cardImage.fitWidthProperty().bind(cardPane.widthProperty().multiply(.8));
        cardImage.setPreserveRatio(true);
        cardPane.getChildren().add(cardImage);
        cardPane.setAlignment(Pos.CENTER);
        User user = MenuController.getInstance().getUser();
        Label priceLabel = new Label(String.valueOf(card.getPrice()));
        HBox hBox = new HBox(priceLabel);
        hBox.prefWidthProperty().bind(cardPane.widthProperty().multiply(.8));
        hBox.prefHeightProperty().bind(cardPane.widthProperty().multiply(.3));
        hBox.spacingProperty().bind(cardPane.widthProperty().multiply(.2));
        hBox.setAlignment(Pos.CENTER);
        if (user.getCardBalance(card) > 0) {
            Label balanceLabel = new Label("Balance: " + user.getCardBalance(card));
            hBox.getChildren().add(balanceLabel);
        }
        Button buyButton = new Button("Buy this card");
        if (card.getPrice() > user.getBalance()) {
            buyButton.getStyleClass().add("inactive-button");
        }
        else {
            buyButton.getStyleClass().add("normal-button");
            buyButton.setOnAction(actionEvent -> buyCard(card));
        }
        buyButton.resize(98.6, 35.3);
        buyButton.prefWidthProperty().bind(cardPane.widthProperty().multiply(.5));
        buyButton.prefHeightProperty().bind(buyButton.prefWidthProperty().multiply(35.3/98.6));
        cardPane.getChildren().add(hBox);
        cardPane.getChildren().add(buyButton);
        mainPane.setLeft(cardPane);
    }

    private void buyCard(Card card) {
        User user = MenuController.getInstance().getUser();
        user.addCardBalance(card.getCardName());
        user.decreaseBalance(card.getPrice());
        prepareCardForConsideration(card);
        UtilityView.displayMessage("Card added successfully");
    }
}
