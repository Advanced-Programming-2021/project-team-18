package view;

import card.Card;
import data.DataManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ServerWelcomeView extends View {

    public ScrollPane scrollPane;
    public GridPane grid;
    public VBox initialVBox;
    public Button initialExitButton;
    public BorderPane mainPane;

    @FXML
    public void initialize() {
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        initialVBox.prefWidthProperty().bind(stage.widthProperty().multiply(.33));
        initialExitButton.prefWidthProperty().bind(initialVBox.widthProperty().multiply(.5));
        initialExitButton.prefHeightProperty().bind(initialExitButton.prefWidthProperty().multiply(35.3 / 98.6));
        initialExitButton.setOnAction(actionEvent -> exit());
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
            cardImage.setOnMouseClicked(mouseEvent -> prepareCardForConsideration(finalCard));
            grid.add(cardImage, i % rowSize, i / rowSize);
            i++;
        }
    }


    public void exit() {
        DataManager.saveData();
        System.exit(0);
    }

    private void adjustButtonSize(VBox parent, Button button) {
        button.resize(98.6, 35.3);
        button.prefWidthProperty().bind(parent.widthProperty().multiply(.5));
        button.prefHeightProperty().bind(button.prefWidthProperty().multiply(35.3/98.6));
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
        VBox cardPane = new VBox();
        cardPane.setPrefWidth(stage.getWidth() * .33);
        cardPane.minWidthProperty().bind(stage.widthProperty().multiply(.33));
        cardPane.maxWidthProperty().bind(stage.widthProperty().multiply(.33));

        ImageView cardImage;
        if (Card.isCardForbidden(card.getCardName())) {
            cardImage = destroyColorOfImage(card.getImage());
        } else cardImage = new ImageView(card.getImage());
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
        
        int cardBalance = Card.getBalanceOfCard(card.getCardName());

        Button forbidSwitch = new Button("Switch forbid");
        forbidSwitch.setOnAction(actionEvent -> {
            Card.switchForbidOfCard(card.getCardName());
            prepareCardForConsideration(card);
        });
        Button increaseBalance = new Button("Increase balance");
        increaseBalance.setOnAction(actionEvent -> {
            Card.increaseBalanceOfCard(card.getCardName());
            prepareCardForConsideration(card);
        });
        Button decreaseBalance = new Button("Decrease balance");
        decreaseBalance.getStyleClass().clear();
        decreaseBalance.getStyleClass().add("inactive-button");
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(actionEvent -> exit());

        if (cardBalance > 0) {
            Label balanceLabel = new Label("Balance: " + cardBalance);
            hBox.getChildren().add(balanceLabel);
            decreaseBalance.getStyleClass().clear();
            decreaseBalance.getStyleClass().add("normal-button");
            decreaseBalance.setOnAction(actionEvent -> {
                Card.decreaseBalanceOfCard(card.getCardName());
                prepareCardForConsideration(card);
            });
        }

        adjustButtonSize(cardPane, forbidSwitch);
        adjustButtonSize(cardPane, increaseBalance);
        adjustButtonSize(cardPane, decreaseBalance);
        adjustButtonSize(cardPane, exitButton);
        cardPane.getChildren().addAll(hBox, forbidSwitch, increaseBalance, decreaseBalance, exitButton);
        mainPane.setRight(cardPane);
    }
}
