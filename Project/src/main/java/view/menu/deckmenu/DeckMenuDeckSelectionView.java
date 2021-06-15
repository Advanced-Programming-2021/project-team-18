package view.menu.deckmenu;

import game.Deck;
import game.GameDeck;
import game.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;
import view.menu.mainmenu.MainMenuView;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckMenuDeckSelectionView extends View implements Initializable {
    @Setter private static User currentUser;

    @FXML
    private ListView<String> listView;

    {
        System.out.println("Constructed!");
        System.out.println("currentUser: " + currentUser);
    }

    @FXML
    private void onRemoveButton() {
        ObservableList<String> list = listView.getSelectionModel().getSelectedItems();
        if(list.size() != 1) {
            UtilityView.displayMessage("no deck was selected");
            return ;
        }
        String deckName = list.get(0);
        GameDeck gameDeck = currentUser.getGameDeckByName(deckName);
        currentUser.removeGameDeck(gameDeck);
        updateListView();
    }

    @SneakyThrows
    @FXML
    private void onBackButton() {
        MainMenuView.setCurrentUser(currentUser);
        loadView("mainMenu");
    }

    @SneakyThrows
    @FXML
    private void onEditButton() {
        ObservableList<String> list = listView.getSelectionModel().getSelectedItems();
        if(list.size() != 1) {
            UtilityView.displayMessage("no deck was selected");
            return ;
        }
        DeckMenuSpecificDeck.setCurrentDeck(currentUser.getGameDeckByName(list.get(0)));
        DeckMenuSpecificDeck.setCurrentUser(currentUser);
        loadView("deckView");
    }

    @FXML
    private void onAddNewDeckButton() {
        String name = UtilityView.obtainInformation("enter a name for your deck");
        while(currentUser.getGameDeckByName(name) != null) {
            UtilityView.displayMessage("deck with this name already exists");
            return ;
        }
        currentUser.addGameDeck(new GameDeck(name));
        updateListView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateListView();
    }
    private void updateListView() {
        listView.getItems().clear();
        for(GameDeck gameDeck : currentUser.getDecks())
            listView.getItems().add(gameDeck.getName());
    }
}
