package view.menu.shopmenu;

import card.Card;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.SneakyThrows;
import menus.MenuController;
import utility.Utility;
import view.UtilityView;
import view.View;

import java.io.IOException;
import java.util.Objects;

public class ShopMenuView extends View {

    public ScrollPane scrollPane;
    public GridPane grid;
    public BorderPane mainPane;
    public VBox initialVBox;
    public Button initialBackButton;

    private int userBalance;
    private Card selectedCard = null;
    private Timeline refreshPage;

    @FXML
    public void initialize() {
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        initialVBox.prefWidthProperty().bind(stage.widthProperty().multiply(.33));
        initialBackButton.prefWidthProperty().bind(initialVBox.widthProperty().multiply(.5));
        initialBackButton.prefHeightProperty().bind(initialBackButton.prefWidthProperty().multiply(35.3 / 98.6));
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
                continue;
            }
            cardImage.fitWidthProperty().bind(stage.widthProperty().multiply(.66 / rowSize));
            cardImage.setPreserveRatio(true);
            Card finalCard = card;
            cardImage.setOnMouseClicked(mouseEvent -> selectedCard = finalCard);
            grid.add(cardImage, i % rowSize, i / rowSize);
            i++;
        }
        String balanceStr = Utility.getRequest(Utility.getSERVER_LOCATION() + "/api/shopmenu/get_user_balance", null,
                Utility.makeHashMap("token", MenuController.getInstance().getToken()));
        System.out.println("BALANCE RESPONSE: " + balanceStr);
        userBalance = Integer.parseInt(balanceStr);
        refreshPage = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> prepareCardForConsideration(selectedCard)));
        refreshPage.setCycleCount(-1);
        refreshPage.play();
    }

    private Color blackAndWhiteColor(Color color) {
        int mean = (int) ((color.getRed() + color.getGreen() + color.getBlue()) * 255./ 3);
        return Color.rgb(mean, mean, mean);
    }

    private ImageView destroyColorOfImage(Image image) {
        WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelWriter writer = writableImage.getPixelWriter();
        PixelReader reader = writableImage.getPixelReader();
        for (int i = 0; i < writableImage.getHeight(); i++) {
            for (int j = 0; j < writableImage.getWidth(); j++) {
                writer.setColor(j, i, blackAndWhiteColor(reader.getColor(j, i)));
            }
        }
        return new ImageView(writableImage);
    }

    private void prepareCardForConsideration(Card card) {
        System.out.println("PREPARING " + card + "...");
        if (card == null) return;
        int cardBalance = MenuController.getInstance().getCardBalance(card.getCardName());
        boolean isForbidden = MenuController.getInstance().getCardForbid(card.getCardName());
        VBox cardPane = new VBox();
        cardPane.setPrefWidth(stage.getWidth() * .33);
        cardPane.minWidthProperty().bind(stage.widthProperty().multiply(.33));
        cardPane.maxWidthProperty().bind(stage.widthProperty().multiply(.33));
        ImageView cardImage = (isForbidden ? destroyColorOfImage(card.getImage()) : new ImageView(card.getImage()));
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
        if (cardBalance > 0) {
            Label balanceLabel = new Label("Balance: " + cardBalance);
            hBox.getChildren().add(balanceLabel);
        }
        Button buyButton = new Button("Buy this card");
        if (card.getPrice() > userBalance || isForbidden) {
            buyButton.getStyleClass().clear();
            buyButton.getStyleClass().add("inactive-button");
        } else {
            buyButton.setOnAction(actionEvent -> buyCard(card));
        }
        Button sellButton = new Button("Sell this card");
        if (cardBalance > 0) sellButton.setOnAction(actionEvent -> sellCard(card));
        else {
            sellButton.getStyleClass().clear();
            sellButton.getStyleClass().add("inactive-button");
        }
        buyButton.resize(98.6, 35.3);
        buyButton.prefWidthProperty().bind(cardPane.widthProperty().multiply(.5));
        buyButton.prefHeightProperty().bind(buyButton.prefWidthProperty().multiply(35.3 / 98.6));
        sellButton.resize(98.6, 35.3);
        sellButton.prefWidthProperty().bind(cardPane.widthProperty().multiply(.5));
        sellButton.prefHeightProperty().bind(buyButton.prefWidthProperty().multiply(35.3 / 98.6));
        Button backButton = new Button("Back");
        backButton.resize(98.6, 35.3);
        backButton.prefWidthProperty().bind(cardPane.widthProperty().multiply(.5));
        backButton.prefHeightProperty().bind(buyButton.prefWidthProperty().multiply(35.3 / 98.6));
        backButton.setOnAction(actionEvent -> back());
        cardPane.getChildren().addAll(hBox, buyButton, sellButton, backButton);
        mainPane.setLeft(cardPane);
    }

    @SneakyThrows
    private void sellCard(Card card) {
        String response = Utility.send("/api/shopmenu/sell_card", "token", MenuController.getInstance().getToken(),
                "card_name", card.getCardName());
        switch (Objects.requireNonNull(response)) {
            case "username_not_found":
            case "card_not_found":
                UtilityView.showError("Some problem occurred in connection :(");
                return;
            case "insufficient_balance":
                UtilityView.showError("You don't have any instance of this card!");
                return; // Maybe never happens ?!
        }
        userBalance += card.getPrice();
        prepareCardForConsideration(card);
        UtilityView.displayMessage("Card got sold successfully");
    }

    @SneakyThrows
    private void buyCard(Card card) {
        String response = Utility.send("/api/shopmenu/buy_card", "token", MenuController.getInstance().getToken(),
                "card_name", card.getCardName());
        System.out.println("RESPONSE OF SERVER: " + response);
        switch (Objects.requireNonNull(response)) {
            case "username_not_found":
            case "card_not_found":
                UtilityView.showError("Some problem occurred in connection :(");
                return;
            case "insufficient_balance":
                UtilityView.showError("You don't have enough balance :)");
                return;
            // Maybe never happens ?
            case "no_more_card":
                UtilityView.showError("All instances of this card have been sold!");
                return;
        }
        userBalance -= card.getPrice();
        prepareCardForConsideration(card);
        UtilityView.displayMessage("Card added successfully");
    }

    public void back() {
        refreshPage.stop();
        try {
            loadView("main_menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
