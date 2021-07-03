package view.menu.shopmenu;

import card.Card;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.View;

public class ShopMenuView extends View {

    public ScrollPane scrollPane;
    public GridPane grid;
    public Text cardNameText;

    @FXML
    public void initialize() {
        scrollPane.setVmax(600);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToWidth(true);

        Card card;
        ImageView cardImage;
        final int rowSize = 5;
        int i = 0;
        for (String cardName : Card.getAllCardNames()) {
            card = Card.getCardByName(cardName);
            assert card != null;
            try {
                cardImage = new ImageView(card.getImage());
                cardImage.setFitWidth(150);
                cardImage.setPreserveRatio(true);
                Card finalCard = card;
                cardImage.setOnMouseClicked(mouseEvent -> cardNameText.setText(finalCard.getCardName()));
                grid.add(cardImage, i % rowSize, i / rowSize);
                i++;
            } catch (Exception e) {
                System.out.println(cardName);
            }
        }
    }
}
