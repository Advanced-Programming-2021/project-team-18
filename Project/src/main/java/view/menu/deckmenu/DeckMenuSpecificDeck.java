package view.menu.deckmenu;

import card.Card;
import game.GameDeck;
import game.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;
import view.components.CardComponent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckMenuSpecificDeck extends View implements Initializable {
    @Setter private static GameDeck currentDeck; // Note : has to be set when entered
    @Setter private static User currentUser; // Note : has to be set when entered
    public CardComponent sideDeckCardComponent;
    public CardComponent mainDeckCardComponent;

    @SneakyThrows
    public void addCardToMainDeck(ActionEvent actionEvent) {
        DeckMenuCardSelection.setCurrentDeck(currentDeck.getMainDeck());
        DeckMenuCardSelection.setCurrentGameDeck(currentDeck);
        DeckMenuCardSelection.setCurrentUser(currentUser);
        loadView("deck_menu_select_card");
    }

    @SneakyThrows
    public void addCardToSideDeck(ActionEvent actionEvent) {
        DeckMenuCardSelection.setCurrentDeck(currentDeck.getSideDeck());
        DeckMenuCardSelection.setCurrentGameDeck(currentDeck);
        DeckMenuCardSelection.setCurrentUser(currentUser);
        loadView("deck_menu_select_card");
    }

    public void onRemoveMainDeckButton(ActionEvent actionEvent) {
        if(mainDeckCardComponent.getSelectedCardName() == null)
            return ;
        currentDeck.getMainDeck().removeCard(Card.getCardByName(mainDeckCardComponent.getSelectedCardName()));
        mainDeckCardComponent.removeCard(Card.getCardByName(mainDeckCardComponent.getSelectedCardName()));
    }

    public void onRemoveSideDeckButton(ActionEvent actionEvent) {
        if(sideDeckCardComponent.getSelectedCardName() == null)
            return ;
        currentDeck.getSideDeck().removeCard(Card.getCardByName(sideDeckCardComponent.getSelectedCardName()));
        sideDeckCardComponent.removeCard(Card.getCardByName(sideDeckCardComponent.getSelectedCardName()));
    }

    @SneakyThrows
    public void onBackButton(ActionEvent actionEvent) {
        DeckMenuDeckSelectionView.setCurrentUser(currentUser);
        loadView("deck_menu_deck_selection");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Card card : currentDeck.getMainDeck().getCardsList())
            mainDeckCardComponent.addCard(card);
    }

    public void setAsActiveDeck(ActionEvent actionEvent) {
        currentUser.setActiveDeckName(currentDeck.getName());
        UtilityView.displayMessage("this deck was set as your active deck");
    }
}
