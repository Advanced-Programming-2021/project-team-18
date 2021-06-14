package view.menu.shopmenu;

import card.Card;
import card.MonsterCard;
import data.Printer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.View;

import java.io.IOException;
import java.util.Objects;

public class ShopMenuView extends View {

    public GridPane grid;

    private Pane getCardNode(Card card) {
        if (card instanceof MonsterCard) {
            /*StackPane stackPane = new StackPane();
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(100);
            rectangle.setHeight(200);
            rectangle.setFill(Color.rgb(0, 0, 0, 0.5));
            Label nameLabel = new Label("Card Name: " + card.getCardName());
            nameLabel.setMaxWidth(100);
            Label levelLabel = new Label("Level: " + ((MonsterCard) card).getCardLevel());
            levelLabel.setMaxWidth(100);
            stackPane.getChildren().addAll(rectangle, nameLabel, levelLabel);*/
            VBox root = new VBox();
            Label nameLabel = new Label("Name: " + card.getCardName());
            System.out.println("Card Name: " + card.getCardName());
            Label levelLabel = new Label("Level: " + ((MonsterCard) card).getCardLevel());
            root.getChildren().addAll(nameLabel, levelLabel);
            /*root.setPrefWidth(100);
            root.setPrefHeight(200);*/
            return root;
        }
        return null;
    }

    @FXML
    public void initialize() throws IOException {
        for (int i = 0; i < 5; i++) {
            Card card = Card.getAllCards().get(i);
            Pane root = getCardNode(card);
            if (root != null) {
                grid.add(root,i, 0);
            }
        }
    }
}
