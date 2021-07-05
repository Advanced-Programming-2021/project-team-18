package view.menu.mainmenu;

import game.User;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import lombok.SneakyThrows;
import menus.MenuController;
import view.View;
import view.menu.cardcreatormenu.CardCreatorView;
import view.menu.deckmenu.DeckMenuDeckSelectionView;
import view.menu.import_export_menu.ImportExportMenuView;
import view.menu.scoreboard.ScoreboardView;

import java.io.File;

public class MainMenuView extends View {
    private static User currentUser; // note : has to be set when entered
    public StackPane root;

    @SneakyThrows
    @FXML
    public void initialize() {
        File file = new File(getClass().getResource("/background/background.jpg").toURI());
        Image image = new Image(file.toURI().toString());
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Background background = new Background(backgroundimage);
        //stage.getScene().setFill(Color.TRANSPARENT);
    }

    public static void setCurrentUser(User currentUser) {
        MainMenuView.currentUser = currentUser;
        MenuController.getInstance().setUser(currentUser);
    }

    public void enterDuelMenu() {

    }

    @SneakyThrows
    public void enterDeckMenu() {
        DeckMenuDeckSelectionView.setCurrentUser(currentUser);
        loadView("deck_menu_deck_selection");
    }

    @SneakyThrows
    public void enterScoreboardMenu() {
        ScoreboardView.setUser(currentUser);
        loadView("scoreboard");
    }

    @SneakyThrows
    public void enterProfileMenu() {
        loadView("profile_menu");
    }

    @SneakyThrows
    public void enterShopMenu() {
        loadView("shop_menu");
    }

    @SneakyThrows
    public void enterImportExportMenu() {
        ImportExportMenuView.setCurrentUser(currentUser);
        loadView("import_export_menu");
    }

    @SneakyThrows
    public void logout() {
        setCurrentUser(null);
        loadView("login_menu");
    }

    @SneakyThrows
    public void enterCardCreatorMenu(MouseEvent mouseEvent) {
        CardCreatorView.setCurrentUser(currentUser);
        loadView("card_creator_menu");
    }
}
