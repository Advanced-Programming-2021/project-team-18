package view.menu.mainmenu;

import game.User;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import lombok.SneakyThrows;
import view.View;
import view.menu.cardcreatormenu.CardCreatorView;
import view.menu.deckmenu.DeckMenuDeckSelectionView;
import view.menu.scoreboard.ScoreboardView;

import java.io.IOException;

public class MainMenuView extends View {
    @Setter
    private static User currentUser; // note : has to be set when entered

    public void enterDuelMenu() {

    }

    @SneakyThrows
    public void enterDeckMenu() {
        DeckMenuDeckSelectionView.setCurrentUser(currentUser);
        loadView("deck_menu_deck_selection");
    }

    public void enterScoreboardMenu() throws IOException {
        ScoreboardView.setUser(currentUser);
        loadView("scoreboard");
    }

    public void enterProfileMenu() throws IOException {
        loadView("profile_menu");
    }

    public void enterShopMenu() throws IOException {
        loadView("shop_menu");
    }

    public void enterImportExportMenu() {

    }

    @SneakyThrows
    public void logout() {
        currentUser = null;
        loadView("login_menu");
    }
    @SneakyThrows
    public void enterCardCreatorMenu(MouseEvent mouseEvent) {
        CardCreatorView.setCurrentUser(currentUser);
        loadView("card_creator_menu");
    }
}
