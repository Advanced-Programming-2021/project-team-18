package view.menu.mainmenu;

import game.User;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.SneakyThrows;
import menus.MenuController;
import view.View;
import view.animations.CoinTransition;
import view.menu.cardcreatormenu.CardCreatorView;
import view.menu.deckmenu.DeckMenuDeckSelectionView;
import view.menu.import_export_menu.ImportExportMenuView;
import view.menu.scoreboard.ScoreboardView;


public class MainMenuView extends View {
    private static User currentUser; // note : has to be set when entered
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
        //vBox.setLayoutX(20);
        //vBox.fillWidthProperty().bind(stage.getScene().widthProperty());
        //vBox.layoutXProperty().bind(anchorPane.widthProperty().divide(2));
        //vBox.layoutYProperty().bind(stage.heightProperty().add(vBox.heightProperty().divide(2)));*/
    }

    private void updateCoin(MouseEvent mouseEvent) {
        stage.setTitle(mouseEvent.getX() + ", " + mouseEvent.getY());
        coinView.setLayoutX(mouseEvent.getSceneX() - coinView.getLayoutBounds().getCenterX());
        coinView.setLayoutY(mouseEvent.getSceneY() - coinView.getLayoutBounds().getCenterY());
    }

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
    public void enterCardCreatorMenu() {
        CardCreatorView.setCurrentUser(currentUser);
        loadView("card_creator_menu");
    }
    @SneakyThrows
    public void onLogoutButton() {
        loadView("welcome");
    }
}
