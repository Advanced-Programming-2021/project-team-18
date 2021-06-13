package view.menu.mainmenu;

import game.User;
import javafx.event.ActionEvent;
import lombok.Setter;
import lombok.SneakyThrows;
import view.View;
import view.menu.deckmenu.DeckMenuDeckSelectionView;

public class MainMenuView extends View {
    @Setter private static User currentUser; // note : has to be set when entered

    public void enterDuelMenu(ActionEvent actionEvent) {

    }
    @SneakyThrows
    public void enterDeckMenu(ActionEvent actionEvent) {
        DeckMenuDeckSelectionView.setCurrentUser(currentUser);
        loadView("/view/FXML/deckMenuDeckSelection.fxml");
    }

    public void enterScoreboardMenu(ActionEvent actionEvent) {

    }

    public void enterProfileMenu(ActionEvent actionEvent) {

    }

    public void enterShopMenu(ActionEvent actionEvent) {

    }

    public void enterImportExportMenu(ActionEvent actionEvent) {

    }

    @SneakyThrows
    public void logout(ActionEvent actionEvent) {
        currentUser = null;
        loadView("/view/FXML/loginMenu.fxml");
    }
}
