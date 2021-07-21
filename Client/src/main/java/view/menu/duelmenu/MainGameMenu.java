package view.menu.duelmenu;

import card.Card;
import card.MonsterCard;
import controller.duelmenu.MainGameController;
import events.Phase;
import game.Game;
import game.Player;
import game.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.MenuController;
import view.UtilityView;
import view.View;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainGameMenu extends View implements Initializable {

    @FXML
    private ImageView fieldImageView;
    @FXML
    private ScrollPane buttonsScrollPane;
    @FXML
    private ImageView cardImageView;
    @FXML
    private Text cardTitle;
    @FXML
    private Text cardDescription;
    @FXML
    private GridPane fieldGridPane;
    @FXML
    private Label firstPlayerTitle;
    @FXML
    private Label secondPlayerTitle;
    @FXML
    private ImageView firstPlayerAvatar;
    @FXML
    private ImageView secondPlayerAvatar;
    @FXML
    private VBox buttonsVBox;
    @FXML
    private Text firstPlayerLP;
    @FXML
    private Text secondPlayerLP;
    @FXML
    private Text phaseNameText;
    @Setter
    private Game game;
    @Setter
    private User myUser;
    @Setter
    private Player myPlayer;
    @Setter
    private Stage myStage;
    private int[] myCardPositions = {0, 7, 9, 5, 11, 3};
    private int[] opponentCardPositions = {0, 7, 5, 9, 3, 11};
    private AtomicBoolean refreshBoardCondition;
    private Timeline refresher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("INITIALIZING...");
        refreshBoardCondition = new AtomicBoolean(true);
        refresher = new Timeline(new KeyFrame(Duration.millis(200), actionEvent -> {
            String duelMessage;
            if (MainGameController.shouldRefresh(MenuController.getInstance().getToken())) Platform.runLater(() -> refresh());
            duelMessage = MainGameController.getDuelMessage(MenuController.getInstance().getToken());
            if (!duelMessage.equals("null")) {
                UtilityView.showError(duelMessage);
            }
        }));
        refresher.setCycleCount(-1);
        refresher.play();
    }

    private void manageGame() {
        game = MainGameController.getGameByToken(MenuController.getInstance().getToken());
        if (game.getFirstUser().getToken().equals(MenuController.getInstance().getToken())) {
            myUser = game.getFirstUser();
            myPlayer = game.getFirstPlayer();
        } else {
            myUser = game.getSecondUser();
            myPlayer = game.getSecondPlayer();
        }
        game.getFirstPlayer().setGame(game);
        game.getSecondPlayer().setGame(game);
        game.getFirstPlayer().setOpponent(game.getSecondPlayer());
        game.getSecondPlayer().setOpponent(game.getFirstPlayer());
        if (game.getFirstPlayer().getSelectedCard() != null)
            game.getFirstPlayer().getSelectedCard().setPlayer(game.getFirstPlayer());
        if (game.getSecondPlayer().getSelectedCard() != null)
            game.getSecondPlayer().getSelectedCard().setPlayer(game.getSecondPlayer());
    }

    @SneakyThrows
    public void refresh() {
        manageGame();
        fieldGridPane.getChildren().clear();
        buttonsVBox.getChildren().clear();
        checkWinOrLoss();
        refreshSettingButton();
        refreshFieldZone();
        refreshGraveyardAndDrawPile();
        refreshLifePointAndPhase();
        refreshAvatarAndTitles();
        refreshSelectedCardDetails();
        refreshMyHand();
        refreshOpponentHand();
        refreshMyMonsters();
        refreshOpponentMonsters();
        refreshMySpellsAndTraps();
        refreshOpponentSpellsAndTraps();
        refreshButtonsVBox();
    }

    private void refreshSettingButton() {
        if (!game.getActivePlayer().getUser().getToken().equals(myUser.getToken())) return;
        Button settingButton = new Button("setting");
        settingButton.setPrefWidth(buttonsVBox.getWidth());
        settingButton.setOnMouseClicked(event -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setMinWidth(250);
            Label label = new Label("game is paused");
            Button closeButton = new Button("resume");
            Button forfeitButton = new Button("forfeit");
            forfeitButton.setOnMouseClicked(event2 -> {
                MainGameController.forfeit(MenuController.getInstance().getToken());
                stage.close();
            });
            closeButton.setOnMouseClicked(event2 -> {
                stage.close();
            });
            VBox layout = new VBox();
            layout.getChildren().addAll(label, closeButton, forfeitButton);
            layout.getStylesheets().add(getClass().getResource("/view/CSS/styles.css").toExternalForm());
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            stage.setScene(scene);
            stage.show();
        });
        buttonsVBox.getChildren().addAll(settingButton);
    }

    private ImageView getCardImageView(Card card, int x, int y, boolean changeSelectedCard, String command) {
        Image image = card.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(fieldGridPane.getCellBounds(x, y).getHeight());
        imageView.setFitWidth(fieldGridPane.getCellBounds(x, y).getWidth());
        if (changeSelectedCard)
            imageView.setOnMouseClicked(mouseEvent -> {
                MainGameController.selectCard(MenuController.getInstance().getToken(), command);
            });
        return imageView;
    }

    @SneakyThrows
    private ImageView getUnknownImageView(Card card, int x, int y, boolean changeSelectedCard, String command) {
        Image image = new Image(getClass().getResource("/cards_images/Unknown.jpg").toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(fieldGridPane.getCellBounds(x, y).getHeight());
        imageView.setFitWidth(fieldGridPane.getCellBounds(x, y).getWidth());
        if (changeSelectedCard)
            imageView.setOnMouseClicked(mouseEvent -> {
                MainGameController.selectCard(MenuController.getInstance().getToken(), command);
            });
        return imageView;
    }

    @SneakyThrows
    private void checkWinOrLoss() {
        if (myPlayer.isLoser()) {
            refreshBoardCondition.set(false);
            refresher.stop();
            myStage.close();
            loadView("main_menu");
            stage.show();
            UtilityView.displayMessage("you won :)");
        }
        if (myPlayer.getOpponent().isLoser()) {
            refreshBoardCondition.set(false);
            refresher.stop();
            myStage.close();
            loadView("main_menu");
            stage.show();
            UtilityView.displayMessage("you lost :(");
        }
    }

    private void refreshFieldZone() {
        // get myPlayer from server
        if (myPlayer.getFieldZone() != null) {
            if (myPlayer.getFieldZone().isFaceUp())
                fieldGridPane.add(getCardImageView(myPlayer.getFieldZone(), 1, 6, true, "me field 1"), 1, 6);
            else
                fieldGridPane.add(getUnknownImageView(myPlayer.getFieldZone(), 1, 6, true, "me field 1"), 1, 6);
        }
        if (myPlayer.getOpponent().getFieldZone() != null) {
            if (myPlayer.getOpponent().getFieldZone().isFaceUp())
                fieldGridPane.add(getCardImageView(myPlayer.getOpponent().getFieldZone(), 1, 6, true, "opponent field 1"), 1, 6);
            else
                fieldGridPane.add(getUnknownImageView(myPlayer.getOpponent().getFieldZone(), 1, 6, true, "opponent field 1"), 1, 6);
        }
        String cardName = "none";
        if (myPlayer.getFieldZone() != null)
            cardName = myPlayer.getFieldZone().getCardName();
        else if (myPlayer.getOpponent().getFieldZone() != null)
            cardName = myPlayer.getOpponent().getFieldZone().getCardName();
        if (!cardName.equals("none")) {
            cardName = cardName.replaceAll("\\s", "_");
            fieldImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(
                    "/background/fields/" + cardName + ".bmp")).toExternalForm()));
        }

    }

    @SneakyThrows
    private void refreshGraveyardAndDrawPile() {
        ImageView myGraveyardImageView = getUnknownImageView(null, 13, 6, false, "");
        ImageView opponentGraveyardImageView = getUnknownImageView(null, 1, 4, false, "");
        ImageView myDrawPileImageView = getUnknownImageView(null, 13, 8, false, "");
        ImageView opponentDrawPileImageView = getUnknownImageView(null, 1, 2, false, "");
        if (myPlayer.getGraveyard().getSize() != 0)
            fieldGridPane.add(myGraveyardImageView, 13, 6);
        if (myPlayer.getOpponent().getGraveyard().getSize() != 0)
            fieldGridPane.add(opponentGraveyardImageView, 1, 4);
        fieldGridPane.add(myDrawPileImageView, 13, 8);
        fieldGridPane.add(opponentDrawPileImageView, 1, 2);
    }

    private void refreshMySpellsAndTraps() {
        for (int i = 1; i <= Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getSpellsAndTrapFieldList()[i] != null) {
                Card card = myPlayer.getSpellsAndTrapFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getSpellsAndTrapFieldList()[i], myCardPositions[i], 8, true, "me spell " + i);
                else
                    imageView = getUnknownImageView(myPlayer.getSpellsAndTrapFieldList()[i], myCardPositions[i], 8, true, "me spell " + i);
                fieldGridPane.add(imageView, myCardPositions[i], 8);
            }
    }

    private void refreshOpponentSpellsAndTraps() {
        for (int i = 1; i <= Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getOpponent().getSpellsAndTrapFieldList()[i] != null) {
                Card card = myPlayer.getOpponent().getSpellsAndTrapFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getOpponent().getSpellsAndTrapFieldList()[i], opponentCardPositions[i], 2, true, "opponent spell " + i);
                else
                    imageView = getUnknownImageView(myPlayer.getOpponent().getSpellsAndTrapFieldList()[i], opponentCardPositions[i], 2, true, "opponent spell " + i);
                fieldGridPane.add(imageView, opponentCardPositions[i], 2);
            }
    }

    private Paint getPaint(int red, int green, int blue) {
        return Paint.valueOf(String.format("#%02X%02X%02X", Math.min(red, 255), Math.min(green, 255), blue));
    }

    private void refreshLifePointAndPhase() {
        firstPlayerLP.setText(myPlayer.getLifePoint() + "");
        secondPlayerLP.setText(myPlayer.getOpponent().getLifePoint() + "");
        firstPlayerLP.setFill(getPaint((8000 - myPlayer.getLifePoint()) * 255 / 8000, myPlayer.getLifePoint() * 255 / 8000, 0));
        secondPlayerLP.setFill(getPaint((8000 - myPlayer.getOpponent().getLifePoint()) * 255 / 8000, myPlayer.getOpponent().getLifePoint() * 255 / 8000, 0));
        phaseNameText.setText(game.getCurrentPhase().toString());
    }

    private void refreshAvatarAndTitles() {
        firstPlayerAvatar.setImage(myUser.getAvatar());
        secondPlayerAvatar.setImage(myPlayer.getOpponent().getUser().getAvatar());
        firstPlayerTitle.setText(myUser.getNickname());
        secondPlayerTitle.setText(myPlayer.getOpponent().getUser().getNickname());
    }

    private void refreshMyHand() { // [1,10] ... [13,10]
        int handSize = myPlayer.getHand().getSize();
        for (int i = 0; i < handSize; ++i) {
            fieldGridPane.add(getCardImageView(myPlayer.getHand().getCardsList().get(i), 1 + 2 * i, 10, true, "me hand " + i), 1 + 2 * i, 10);
        }
    }

    private void refreshOpponentHand() { // [1,0] ... [13,0]
        int handSize = myPlayer.getOpponent().getHand().getSize();
        for (int i = 0; i < handSize; ++i) {
            fieldGridPane.add(getUnknownImageView(myPlayer.getOpponent().getHand().getCardsList().get(i), 1 + 2 * i, 0, true, "opponent hand " + i), 1 + 2 * i, 0);
        }
    }

    @SneakyThrows
    private void refreshSelectedCardDetails() {
        Card selectedCard = myPlayer.getSelectedCard();
        cardDescription.setText("card description");
        cardTitle.setText("Card title");
        cardImageView.setImage(new Image(Objects.requireNonNull(
                getClass().getResource("/cards_images/Unknown.jpg")).toURI().toString()));
        if (selectedCard == null || (!selectedCard.isFaceUp() && !selectedCard.getPlayer().getUser().getToken().equals(myUser.getToken())))
            return;
        Card copyCard = Card.getCardByName(selectedCard.getCardName());
        cardDescription.setText(copyCard.getCardDescription());
        cardTitle.setText(copyCard.getCardName());
        cardImageView.setImage(copyCard.getImage());
    }

    private void refreshMyMonsters() {
        for (int i = 1; i <= Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getMonstersFieldList()[i] != null) {
                Card card = myPlayer.getMonstersFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getMonstersFieldList()[i], myCardPositions[i], 6, true, "me monster " + i);
                else
                    imageView = getUnknownImageView(myPlayer.getMonstersFieldList()[i], myCardPositions[i], 6, true, "me monster " + i);
                imageView.setRotate(((MonsterCard) card).isDefenseMode() ? 90 : 0);
                fieldGridPane.add(imageView, myCardPositions[i], 6);
                makeMyMonsterSupportDrag(imageView, card);
            }
    }

    private void refreshOpponentMonsters() {
        for (int i = 1; i <= Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getOpponent().getMonstersFieldList()[i] != null) {
                Card card = myPlayer.getOpponent().getMonstersFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getOpponent().getMonstersFieldList()[i], opponentCardPositions[i], 4, true, "opponent monster " + i);
                else
                    imageView = getUnknownImageView(myPlayer.getOpponent().getMonstersFieldList()[i], opponentCardPositions[i], 4, true, "opponent monster " + i);
                imageView.setRotate(((MonsterCard) card).isDefenseMode() ? 90 : 0);
                fieldGridPane.add(imageView, opponentCardPositions[i], 4);
                makeOpponentMonsterSupportDrag(imageView, i);
            }
    }

    private void manageNextPhaseButton() {
        Button nextPhaseButton = new Button("next phase");
        nextPhaseButton.setPrefWidth(buttonsVBox.getWidth());
        nextPhaseButton.setOnMouseClicked(event -> {
            MainGameController.nextPhase(MenuController.getInstance().getToken());
        });
        buttonsVBox.getChildren().add(nextPhaseButton);
    }

    private void manageSummonButton() {
        Button summonButton = new Button("summon");
        summonButton.setPrefWidth(buttonsVBox.getWidth());
        summonButton.setOnMouseClicked(event -> {
            MainGameController.summonMonster(MenuController.getInstance().getToken());
        });
        buttonsVBox.getChildren().add(summonButton);
    }

    private void manageSetButton() {
        Button setButton = new Button("set");
        setButton.setPrefWidth(buttonsVBox.getWidth());
        setButton.setOnMouseClicked(event -> {
            MainGameController.setMonster(MenuController.getInstance().getToken());
        });
        buttonsVBox.getChildren().addAll(setButton);
    }

    private void manageChangePositionButton() {
        Button changePosition = new Button("change position");
        changePosition.setPrefWidth(buttonsVBox.getWidth());
        changePosition.setOnMouseClicked(event -> {
            MainGameController.changePosition(MenuController.getInstance().getToken());
        });
        buttonsVBox.getChildren().add(changePosition);
    }

    private void manageFlipSummonButton() {
        Button flipSummon = new Button("flip summon");
        flipSummon.setPrefWidth(buttonsVBox.getWidth());
        flipSummon.setOnMouseClicked(event -> {
            MainGameController.flipSummon(MenuController.getInstance().getToken());
        });
        buttonsVBox.getChildren().add(flipSummon);
    }

    private void manageAttackDirectButton() {
        Button attackDirect = new Button("attack direct");
        attackDirect.setPrefWidth(buttonsVBox.getWidth());
        attackDirect.setOnMouseClicked(event -> {
            MainGameController.attackDirect(MenuController.getInstance().getToken());
        });
        buttonsVBox.getChildren().add(attackDirect);
    }

    private void manageSpellOrTrapSetButton() {
        Button setSpellOrTrap = new Button("set spell or trap");
        setSpellOrTrap.setPrefWidth(buttonsVBox.getWidth());
        setSpellOrTrap.setOnMouseClicked(event -> {
            MainGameController.setSpellOrTrap(MenuController.getInstance().getToken());
        });
        buttonsVBox.getChildren().add(setSpellOrTrap);
    }

    private void manageActivateEffectButton() {
        Button activateEffect = new Button("activate effect");
        activateEffect.setPrefWidth(buttonsVBox.getWidth());
        activateEffect.setOnMouseClicked(event -> {
            MainGameController.activateEffect(MenuController.getInstance().getToken());
        });
        buttonsVBox.getChildren().add(activateEffect);

    }

    private void refreshButtonsVBox() {
        if (!game.getActivePlayer().getUser().getToken().equals(myUser.getToken()))
            return;
        manageNextPhaseButton();
        if (game.getCurrentPhase() == Phase.MAIN1 || game.getCurrentPhase() == Phase.MAIN2) {
            manageSummonButton();
            manageSetButton();
            manageChangePositionButton();
            manageFlipSummonButton();
            manageSpellOrTrapSetButton();
            manageActivateEffectButton();
        }
        if (game.getCurrentPhase() == Phase.BATTLE && (game.getTurn() != 1)) {
            manageAttackDirectButton();
        }
    }

    public void showMyGraveyard() {
        Parent graveyardRoot = computeGraveyardRoot(myPlayer);
        myStage.getScene().setRoot(graveyardRoot);
    }

    public void showOpponentGraveyard() {
        Parent graveyardRoot = computeGraveyardRoot(myPlayer.getOpponent());
        myStage.getScene().setRoot(graveyardRoot);
    }

    private Parent computeGraveyardRoot(Player player) {
        final int rowCount = 5;
        int i = 0;
        GridPane grid = new GridPane();
        for (Card card : player.getGraveyard().getCardsList()) {
            ImageView cardImage = new ImageView(card.getImage());
            cardImage.fitWidthProperty().bind(stage.widthProperty().multiply(.8 / rowCount));
            cardImage.setPreserveRatio(true);
            grid.add(cardImage, i % rowCount, i / rowCount);
            i++;
        }
        ScrollPane scrollPane = new ScrollPane(grid);
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(scrollPane);
        Button backButton = new Button("Back");
        final Parent previousRoot = myStage.getScene().getRoot();
        backButton.setOnAction(actionEvent -> myStage.getScene().setRoot(previousRoot));
        HBox bottomBox = new HBox(backButton);
        bottomBox.setAlignment(Pos.CENTER);
        mainPane.setBottom(bottomBox);
        mainPane.getStylesheets().add(getClass().getResource("/view/CSS/styles.css").toExternalForm());
        return mainPane;
    }

    private void makeMyMonsterSupportDrag(ImageView cardView, Card card) {
        cardView.setOnDragDetected(mouseEvent -> {
            Dragboard dragboard = cardView.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putImage(card.getImage());
            dragboard.setContent(content);
            // todo send to server
//            myPlayer.setSelectedCard(card);
        });
        cardView.setOnMouseDragged(mouseEvent -> mouseEvent.setDragDetect(true));
    }

    private void makeOpponentMonsterSupportDrag(ImageView cardView, int monsterIndex) {
        cardView.setOnDragOver(dragEvent -> {
            if (dragEvent.getDragboard().hasImage()) {
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            dragEvent.consume();
        });

        cardView.setOnDragDropped(dragEvent -> {
            if (game.getCurrentPhase() != Phase.BATTLE || (!game.getActivePlayer().getUser().getToken().equals(myUser.getToken())))
                return;
            if (dragEvent.getDragboard().hasImage()) {
                System.out.println("Attack!");
                System.out.println("Defender: " + myPlayer.getOpponent()
                        .getMonstersFieldList()[monsterIndex].getCardName());
                MainGameController.attackMonster(MenuController.getInstance().getToken(), monsterIndex);
                dragEvent.setDropCompleted(true);
            } else dragEvent.setDropCompleted(false);
        });
    }
}