package view.menu.mainmenu;

import game.User;
import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.SneakyThrows;
import menus.MenuController;
import view.UtilityView;
import view.View;
import view.animations.CoinTransition;
import view.menu.cardcreatormenu.CardCreatorView;
import view.menu.deckmenu.DeckMenuDeckSelectionView;
import view.menu.duelmenu.PreDuelMenu;
import view.menu.import_export_menu.ImportExportMenuView;
import view.menu.scoreboard.ScoreboardView;

import java.awt.*;


public class MainMenuView extends View {
    private static String currentToken;// note : has to be set when entered
    private static CoinTransition transition = null;
    public ImageView coinView;
    public VBox vBox;

    @SneakyThrows
    @FXML
    public void initialize() {
        if (transition != null) transition.stop();
        transition = new CoinTransition(coinView, Duration.millis(800));
        transition.setCycleCount(-1);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.play();
    }

    private void updateCoin(MouseEvent mouseEvent) {
        stage.setTitle(mouseEvent.getX() + ", " + mouseEvent.getY());
        coinView.setLayoutX(mouseEvent.getSceneX() - coinView.getLayoutBounds().getCenterX());
        coinView.setLayoutY(mouseEvent.getSceneY() - coinView.getLayoutBounds().getCenterY());
    }

    @Override
    public void adjustScene() {
        for (Node child : vBox.getChildren()) {
            if (child instanceof HBox) {
                for (Node grandChild : ((HBox) child).getChildren()) {
                    grandChild.setOnMouseMoved(this::updateCoin);
                    grandChild.getStyleClass().clear();
                    grandChild.getStyleClass().add("main-menu-button");
                }
                continue;
            }
            child.setOnMouseMoved(this::updateCoin);
            child.getStyleClass().clear();
            child.getStyleClass().add("main-menu-button");
        }
        stage.getScene().getRoot().setOnMouseMoved(this::updateCoin);
    }

    public static void setCurrentToken(String currentToken) {
        MainMenuView.currentToken = currentToken;
        MenuController.getInstance().setToken(currentToken);
    }

    @SneakyThrows
    public void enterDuelMenu() {
        PreDuelMenu.setCurrentToken(currentToken);
        UtilityView.stopPlayer();
        loadView("pre_duel_menu");
    }

    @SneakyThrows
    public void enterDeckMenu() {
        DeckMenuDeckSelectionView.setCurrentToken(currentToken);
        loadView("deck_menu_deck_selection");
    }

    @SneakyThrows
    public void enterScoreboardMenu() {
        ScoreboardView.setCurrentToken(currentToken);
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
        ImportExportMenuView.setCurrentToken(currentToken);
        loadView("import_export_menu");
    }

    @SneakyThrows
    public void logout() {
        setCurrentToken(null);
        loadView("welcome");
    }

    @SneakyThrows
    public void enterCardCreatorMenu() {
        CardCreatorView.setCurrentToken(currentToken);
        loadView("card_creator_menu");
    }
}
