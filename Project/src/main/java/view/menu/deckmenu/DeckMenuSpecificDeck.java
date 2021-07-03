package view.menu.deckmenu;

import game.GameDeck;
import game.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;
import lombok.SneakyThrows;
import view.View;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckMenuSpecificDeck extends View implements Initializable {
    @Setter private static GameDeck currentDeck; // Note : has to be set when entered
    @Setter private static User currentUser; // Note : has to be set when entered
    private static int mainDeckSelectId;
    private static int sideDeckSelectId;
    public ImageView mainDeckImageView;
    public ImageView sideDeckImageView;

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
        if(mainDeckSelectId < 0)
            return ;
        currentDeck.getMainDeck().removeCard(currentDeck.getMainDeck().getCardsList().get(mainDeckSelectId));
        if(mainDeckSelectId >= currentDeck.getMainDeck().getCardsList().size())
            -- mainDeckSelectId;
        System.out.println(currentDeck.getMainDeck().getCardsList().size() + " " + mainDeckSelectId);
        updateMainDeckImageView();
    }

    public void onRemoveSideDeckButton(ActionEvent actionEvent) {
        if(sideDeckSelectId < 0)
            return ;
        currentDeck.getSideDeck().removeCard(currentDeck.getSideDeck().getCardsList().get(sideDeckSelectId));
        if(sideDeckSelectId >= currentDeck.getSideDeck().getCardsList().size())
            -- sideDeckSelectId;
        updateSideDeckImageView();
    }

    @SneakyThrows
    public void onBackButton(ActionEvent actionEvent) {
        DeckMenuDeckSelectionView.setCurrentUser(currentUser);
        loadView("deck_menu_deck_selection");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainDeckSelectId = 0;
        if(currentDeck.getMainDeck().getCardsList().size() == 0)
            mainDeckSelectId = -1;
        sideDeckSelectId = 0;
        if(currentDeck.getSideDeck().getCardsList().size() == 0)
            sideDeckSelectId = -1;
        updateMainDeckImageView();
    }
    @SneakyThrows
    private void updateMainDeckImageView() {
        mainDeckImageView.setImage(null);
        if(mainDeckSelectId < 0)
            return ;
        String cardName = currentDeck.getMainDeck().getCardsList().get(mainDeckSelectId).getCardName();
        cardName = cardName.replaceAll(" " , "_");
        File file = new File(getClass().getResource("/cards_images/" + cardName + ".jpg").toURI());
        mainDeckImageView.setImage(new Image(file.toURI().toString()));
    }
    @SneakyThrows
    private void updateSideDeckImageView() {
        sideDeckImageView.setImage(null);
        if(sideDeckSelectId < 0)
            return ;
        String cardName = currentDeck.getSideDeck().getCardsList().get(sideDeckSelectId).getCardName();
        cardName = cardName.replaceAll(" " , "_");
        File file = new File(getClass().getResource("/cards_images/" + cardName + ".jpg").toURI());
        mainDeckImageView.setImage(new Image(file.toURI().toString()));
    }



}
