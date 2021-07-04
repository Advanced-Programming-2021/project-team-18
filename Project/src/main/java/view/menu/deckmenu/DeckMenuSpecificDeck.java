package view.menu.deckmenu;

import game.GameDeck;
import game.User;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.MenuController;
import view.View;
import view.components.CardComponent;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DeckMenuSpecificDeck extends View implements Initializable {
    @Setter
    private static GameDeck currentDeck; // Note : has to be set when entered
    private static User currentUser; // Note : has to be set when entered
    private static int mainDeckSelectId;
    private static int sideDeckSelectId;
    public ImageView mainDeckImageView;
    public ImageView sideDeckImageView;

    public static void setCurrentUser(User currentUser) {
        DeckMenuSpecificDeck.currentUser = currentUser;
        MenuController.getInstance().setUser(currentUser);
    }

    public void leftMainDeck(ActionEvent actionEvent) {
        if(mainDeckSelectId > 0)
            -- mainDeckSelectId;
        updateMainDeckImageView();
    }

    public void rightMainDeck(ActionEvent actionEvent) {
        if(mainDeckSelectId < currentDeck.getMainDeck().getCardsList().size() - 1)
            ++ mainDeckSelectId;
        updateMainDeckImageView();
    }

    public void leftSideDeck(ActionEvent actionEvent) {
        if(sideDeckSelectId > 0)
            -- sideDeckSelectId;
        updateSideDeckImageView();
    }

    public void rightSideDeck(ActionEvent actionEvent) {
        if(sideDeckSelectId < currentDeck.getSideDeck().getCardsList().size() - 1)
            ++ sideDeckSelectId;
        updateSideDeckImageView();
    }

    @SneakyThrows
    public void addCardToMainDeck(MouseEvent actionEvent) {
        DeckMenuCardSelection.setCurrentDeck(currentDeck.getMainDeck());
        DeckMenuCardSelection.setCurrentGameDeck(currentDeck);
        DeckMenuCardSelection.setCurrentUser(currentUser);
        loadView("deck_menu_select_card");
    }

    @SneakyThrows
    public void addCardToSideDeck(MouseEvent actionEvent) {
        DeckMenuCardSelection.setCurrentDeck(currentDeck.getSideDeck());
        DeckMenuCardSelection.setCurrentGameDeck(currentDeck);
        DeckMenuCardSelection.setCurrentUser(currentUser);
        loadView("deck_menu_select_card");
    }

    public void onRemoveMainDeckButton(MouseEvent actionEvent) {
        if (mainDeckCardComponent.getSelectedCardName() == null)
            return;
        currentDeck.getMainDeck().removeCard(Card.getCardByName(mainDeckCardComponent.getSelectedCardName()));
        mainDeckCardComponent.removeCard(Card.getCardByName(mainDeckCardComponent.getSelectedCardName()));
    }

    public void onRemoveSideDeckButton(MouseEvent actionEvent) {
        if (sideDeckCardComponent.getSelectedCardName() == null)
            return;
        currentDeck.getSideDeck().removeCard(Card.getCardByName(sideDeckCardComponent.getSelectedCardName()));
        sideDeckCardComponent.removeCard(Card.getCardByName(sideDeckCardComponent.getSelectedCardName()));
    }

    @SneakyThrows
    public void onBackButton(MouseEvent actionEvent) {
        DeckMenuDeckSelectionView.setCurrentUser(currentUser);
        loadView("deck_menu_deck_selection");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Card card : currentDeck.getMainDeck().getCardsList())
            mainDeckCardComponent.addCard(card);
    }

    public void setAsActiveDeck(MouseEvent actionEvent) {
        currentUser.setActiveDeckName(currentDeck.getName());
        UtilityView.displayMessage("this deck was set as your active deck");
    }
}
