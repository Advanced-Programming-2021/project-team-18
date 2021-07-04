package view.menu.mainmenu;

import game.User;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.MenuController;
import view.View;
import view.menu.cardcreatormenu.CardCreatorView;
import view.menu.deckmenu.DeckMenuDeckSelectionView;
import view.menu.scoreboard.ScoreboardView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

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
        stage.getScene().setFill(Color.TRANSPARENT);
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
        setCurrentUser(null);
        loadView("login_menu");
    }
    @SneakyThrows
    public void enterCardCreatorMenu(MouseEvent mouseEvent) {
        CardCreatorView.setCurrentUser(currentUser);
        loadView("card_creator_menu");
    }
}
