package view.components;

import card.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Getter
public class CardComponent extends SplitPane implements Initializable {
    private static final int ROW_COUNT = 3;
    private int count = 0;
    private ArrayList<String> cardsInGridList = new ArrayList<>();
    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;

    @Getter
    private String selectedCardName;

    @SneakyThrows
    private void refreshGrid() {
        imageView.setImage(new Image(getClass().getResource("/cards_images/Unknown.jpg").toURI().toString()));
        gridPane.getChildren().clear();

        count = 0;
        for (String cardName : cardsInGridList) {
            Card card = Card.getCardByName(cardName);
            ImageView cardImageView = new ImageView(card.getImage());
            cardImageView.fitWidthProperty().bind(scrollPane.widthProperty().divide(ROW_COUNT * 1.5));
            cardImageView.setPreserveRatio(true);
            cardImageView.setOnMouseClicked(mouseEvent -> {
                imageView.setImage(cardImageView.getImage());
                selectedCardName = card.getCardName();
            });
            gridPane.add(cardImageView, count % ROW_COUNT, count / ROW_COUNT);
            ++count;
        }
    }

    @SneakyThrows
    public CardComponent() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML/card_component.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.setClassLoader(getClass().getClassLoader());
        loader.load();
    }

    public void addCard(Card card) {
        cardsInGridList.add(card.getCardName());
        selectedCardName = null;
        refreshGrid();
    }

    public void removeCard(Card card) {
        int indexToRemove = -1;
        for (int i = 0; i < count; ++i)
            if (cardsInGridList.get(i).equals(card.getCardName()))
                indexToRemove = i;
        if (indexToRemove >= 0)
            cardsInGridList.remove(indexToRemove);
        selectedCardName = null;
        refreshGrid();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToWidth(true);
        imageView.setPreserveRatio(true);
        refreshGrid();
    }
}
