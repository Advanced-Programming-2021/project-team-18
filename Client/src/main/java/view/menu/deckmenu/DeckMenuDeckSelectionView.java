package view.menu.deckmenu;

import game.GameDeck;
import game.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.MenuController;
import view.UtilityView;
import view.View;
import view.menu.mainmenu.MainMenuView;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckMenuDeckSelectionView extends View implements Initializable {

    @FXML
    private ListView<String> listView;

    static {
        System.out.println("Constructed!");
        System.out.println("currentUser: " + MenuController.getInstance().getToken());
    }

    @FXML
    private void onRemoveButton() {
        // todo server
//        ObservableList<String> list = listView.getSelectionModel().getSelectedItems();
//        if (list.size() != 1) {
//            UtilityView.showError("no deck was selected");
//            return;
//        }
//        String deckName = list.get(0);
//        GameDeck gameDeck = currentUser.getGameDeckByName(deckName);
//        currentUser.removeGameDeck(gameDeck);
//        updateListView();
    }

    @SneakyThrows
    @FXML
    private void onBackButton() {
        loadView("main_menu");
    }

    @SneakyThrows
    @FXML
    private void onEditButton() {
        // todo server
//        ObservableList<String> list = listView.getSelectionModel().getSelectedItems();
//        if (list.size() != 1) {
//            UtilityView.showError("no deck was selected");
//            return;
//        }
//        DeckMenuSpecificDeck.setCurrentDeck(currentUser.getGameDeckByName(list.get(0)));
//        DeckMenuSpecificDeck.setCurrentUser(currentUser);
//        loadView("deck_view");
    }

    @FXML
    private void onAddNewDeckButton() {
        // todo server
//        String name = UtilityView.obtainInformation("enter a name for your deck");
//        while (currentUser.getGameDeckByName(name) != null) {
//            UtilityView.showError("deck with this name already exists");
//            return;
//        }
//        currentUser.addGameDeck(new GameDeck(name));
//        System.out.println(currentUser.getGameDeckByName(name) == null);
//        updateListView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateListView();
    }

    private void updateListView() {
        // todo server
//        listView.getItems().clear();
//        for (GameDeck gameDeck : currentUser.getDecks())
//            listView.getItems().add(gameDeck.getName());
    }
}
