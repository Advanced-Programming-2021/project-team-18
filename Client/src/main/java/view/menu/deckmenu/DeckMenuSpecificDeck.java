package view.menu.deckmenu;

import card.Card;
import game.GameDeck;
import game.User;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;
import view.components.CardComponent;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckMenuSpecificDeck extends View implements Initializable {
    @Setter
    private static GameDeck currentDeck; // Note : has to be set when entered

    public CardComponent sideDeckCardComponent;
    public CardComponent mainDeckCardComponent;

    @SneakyThrows
    public void addCardToMainDeck(MouseEvent actionEvent) {
        // todo server
//        DeckMenuCardSelection.setCurrentDeck(currentDeck.getMainDeck());
//        DeckMenuCardSelection.setCurrentGameDeck(currentDeck);

        loadView("deck_menu_select_card");
    }

    @SneakyThrows
    public void addCardToSideDeck(MouseEvent actionEvent) {
        // todo server
//        DeckMenuCardSelection.setCurrentDeck(currentDeck.getSideDeck());
//        DeckMenuCardSelection.setCurrentGameDeck(currentDeck);

        loadView("deck_menu_select_card");
    }

    public void onRemoveMainDeckButton(MouseEvent actionEvent) {
        // todo server
//        if (mainDeckCardComponent.getSelectedCardName() == null)
//            return;
//        currentDeck.getMainDeck().removeCard(Card.getCardByName(mainDeckCardComponent.getSelectedCardName()));
//        mainDeckCardComponent.removeCard(Card.getCardByName(mainDeckCardComponent.getSelectedCardName()));
    }

    public void onRemoveSideDeckButton(MouseEvent actionEvent) {
        // todo server
//        if (sideDeckCardComponent.getSelectedCardName() == null)
//            return;
//        currentDeck.getSideDeck().removeCard(Card.getCardByName(sideDeckCardComponent.getSelectedCardName()));
//        sideDeckCardComponent.removeCard(Card.getCardByName(sideDeckCardComponent.getSelectedCardName()));
    }

    @SneakyThrows
    public void onBackButton(MouseEvent actionEvent) {
        loadView("deck_menu_deck_selection");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // todo server
//        for (Card card : currentDeck.getMainDeck().getCardsList())
//            mainDeckCardComponent.addCard(card);
//        for (Card card : currentDeck.getSideDeck().getCardsList())
//            sideDeckCardComponent.addCard(card);
    }

    public void setAsActiveDeck(MouseEvent actionEvent) {
        // todo server
//        currentUser.setActiveDeckName(currentDeck.getName());
        UtilityView.displayMessage("this deck was set as your active deck");
    }
}
