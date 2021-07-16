package view.menu.deckmenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import lombok.SneakyThrows;
import menus.MenuController;
import utility.Utility;
import view.UtilityView;
import view.View;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DeckMenuDeckSelectionView extends View implements Initializable {
    private static final String DECK_LIST_LOCATION = "/api/deckmenu/deck_selection/get_deck_names";
    private static final String CREATE_NEW_DECK = "/api/deckmenu/deck_selection/create_new_deck";
    private static final String REMOVE_DECK = "/api/deckmenu/deck_selection/remove_deck";
    @FXML
    private ListView<String> listView;
    private ArrayList<String> deckNames;

    static {
        System.out.println("Constructed!");
        System.out.println("currentUser: " + MenuController.getInstance().getToken());
    }

    @FXML
    private void onRemoveButton() {
        ObservableList<String> list = listView.getSelectionModel().getSelectedItems();
        if (list.size() != 1) {
            UtilityView.showError("no deck was selected");
            return;
        }
        String deckName = list.get(0);
        HashMap<String, String> headers = new HashMap<>() {{
            put("token", MenuController.getInstance().getToken());
            put("name", deckName);
        }};
        Utility.postRequest(Utility.getSERVER_LOCATION() + REMOVE_DECK, null, headers);
        updateListView();
    }

    @SneakyThrows
    @FXML
    private void onBackButton() {
        loadView("main_menu");
    }

    @SneakyThrows
    @FXML
    private void onEditButton() {
        ObservableList<String> list = listView.getSelectionModel().getSelectedItems();
        if (list.size() != 1) {
            UtilityView.showError("no deck was selected");
            return;
        }
        DeckMenuSpecificDeck.setCurrentDeckName(list.get(0));
        loadView("deck_view");
    }

    @FXML
    private void onAddNewDeckButton() {
        String name = UtilityView.obtainInformation("enter a name for your deck");
        while (deckNames.contains(name)) {
            UtilityView.showError("deck with this name already exists");
            return;
        }
        HashMap<String, String> headers = new HashMap<>() {{
            put("token", MenuController.getInstance().getToken());
            put("name", name);
        }};
        Utility.postRequest(Utility.getSERVER_LOCATION() + CREATE_NEW_DECK, null, headers);
        updateListView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateListView();
    }

    private void updateListView() {
        listView.getItems().clear();
        HashMap<String, String> headers = new HashMap<>() {{
            put("token", MenuController.getInstance().getToken());
        }};
        String response = Utility.getRequest(Utility.getSERVER_LOCATION() + DECK_LIST_LOCATION, null, headers);
        deckNames = (new Gson()).fromJson(response, new TypeToken<ArrayList<String>>() {
        }.getType());
        for (String name : deckNames)
            listView.getItems().add(name);
    }
}
