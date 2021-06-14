package view.menu.deckmenu;

import game.Deck;
import javafx.fxml.Initializable;
import lombok.Setter;
import view.View;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckMenuCardSelection extends View implements Initializable {
    @Setter private static Deck currentDeck;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
