package view.menu.mainmenu;

import game.User;
import lombok.Setter;
import lombok.SneakyThrows;
import view.View;
import view.menu.deckmenu.DeckMenuDeckSelectionView;
import view.menu.scoreboard.ScoreboardView;

import java.io.IOException;

import java.io.IOException;

public class MainMenuView extends View {
    @Setter private static User currentUser; // note : has to be set when entered

    public void enterDuelMenu() {

    }
    @SneakyThrows
    public void enterDeckMenu() {
        DeckMenuDeckSelectionView.setCurrentUser(currentUser);
        loadView("deckMenuDeckSelection");
    }

    public void enterScoreboardMenu(ActionEvent actionEvent) throws IOException {
        ScoreboardView.setUser(currentUser);
        loadView("/view/FXML/scoreboard.fxml");
    }

    public void enterProfileMenu() throws IOException {
        loadView("profileMenu");
    }

    public void enterShopMenu() throws IOException {
        loadView("shopMenu");
    }

    public void enterImportExportMenu() {

    }

    @SneakyThrows
    public void logout() {
        currentUser = null;
        loadView("loginMenu");
    }
}
