package view.menu.deckmenu;

import game.GameDeck;
import game.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import lombok.SneakyThrows;
import menus.MenuController;
import view.UtilityView;
import view.View;
import view.menu.mainmenu.MainMenuView;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckMenuDeckSelectionView extends View implements Initializable {
    private static User currentUser;

    public static void setCurrentUser(User currentUser) {
        DeckMenuDeckSelectionView.currentUser = currentUser;
        MenuController.getInstance().setUser(currentUser);
    }

    @FXML
    private ListView<String> listView;

    static {
        System.out.println("Constructed!");
        System.out.println("currentUser: " + currentUser);
    }

    @FXML
    private void onRemoveButton() {
        ObservableList<String> list = listView.getSelectionModel().getSelectedItems();
        if (list.size() != 1) {
            UtilityView.displayMessage("no deck was selected");
            return;
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
        loadView("main_menu");
    }

    @SneakyThrows
    @FXML
    private void onEditButton() {
        ObservableList<String> list = listView.getSelectionModel().getSelectedItems();
        if (list.size() != 1) {
            UtilityView.displayMessage("no deck was selected");
            return;
        }
        DeckMenuSpecificDeck.setCurrentDeck(currentUser.getGameDeckByName(list.get(0)));
        DeckMenuSpecificDeck.setCurrentUser(currentUser);
        loadView("deck_view");
    }

    @FXML
    private void onAddNewDeckButton() {
        String name = UtilityView.obtainInformation("enter a name for your deck");
        while (currentUser.getGameDeckByName(name) != null) {
            UtilityView.displayMessage("deck with this name already exists");
            return;
        }
        currentUser.addGameDeck(new GameDeck(name));
        System.out.println(currentUser.getGameDeckByName(name) == null);
        updateListView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateListView();
    }

    private void updateListView() {
        listView.getItems().clear();
        for (GameDeck gameDeck : currentUser.getDecks())
            listView.getItems().add(gameDeck.getName());
    }
}
