package view.menu.duelmenu;

import card.Card;
import card.MonsterCard;
import card.SpellCard;
import card.TrapCard;
import events.Phase;
import game.Game;
import game.Player;
import game.User;
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
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;

import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @SneakyThrows
    public void refresh() {
        // do all updates here
        if(myPlayer.isLoser() || myPlayer.getOpponent().isLoser()) {
            myStage.close();
            if(myPlayer.isLoser()) {
                UtilityView.displayMessage(myUser.getNickname() + " Won :)");
                loadView("main_menu");
                stage.show();
                return ;
            }
        }
        fieldGridPane.getChildren().clear();
        buttonsVBox.getChildren().clear();
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
        if(Game.getActivePlayer() != myPlayer) return ;
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
                myPlayer.setLoser(true);
                stage.close();
            });
            closeButton.setOnMouseClicked(event2 -> {
                stage.close();
            });
            VBox layout = new VBox();
            layout.getChildren().addAll(label , closeButton , forfeitButton);
            layout.getStylesheets().add(getClass().getResource("/view/CSS/styles.css").toExternalForm());
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            stage.setScene(scene);
            stage.showAndWait();
            refresh();
        });
        buttonsVBox.getChildren().addAll(settingButton);
    }

    private void refreshFieldZone() {
        if(myPlayer.getFieldZone() != null) {
            if(myPlayer.getFieldZone().isFaceUp())
                fieldGridPane.add(getCardImageView(myPlayer.getFieldZone() , 1 , 6 , true) , 1 , 6);
            else
                fieldGridPane.add(getUnknownImageView(myPlayer.getFieldZone() , 1 , 6 , true) , 1 , 6);
        }
        if(myPlayer.getOpponent().getFieldZone() != null) {
            if(myPlayer.getOpponent().getFieldZone().isFaceUp())
                fieldGridPane.add(getCardImageView(myPlayer.getOpponent().getFieldZone() , 1 , 6 , true) , 1 , 6);
            else
                fieldGridPane.add(getUnknownImageView(myPlayer.getOpponent().getFieldZone() , 1 , 6 , true) , 1 , 6);
        }
        String cardName = "none";
        if(myPlayer.getFieldZone() != null)
            cardName = myPlayer.getFieldZone().getCardName();
        else if(myPlayer.getOpponent().getFieldZone() != null)
            cardName = myPlayer.getOpponent().getFieldZone().getCardName();
        if(!cardName.equals("none")) {
            cardName = cardName.replaceAll("\\s" , "_");
            fieldImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(
                    "/background/fields/" + cardName + ".bmp")).toExternalForm()));
        }

    }
    @SneakyThrows
    private void refreshGraveyardAndDrawPile() {
        ImageView myGraveyardImageView = getUnknownImageView(null, 13, 6, false);
        ImageView opponentGraveyardImageView = getUnknownImageView(null, 1, 4, false);
        ImageView myDrawPileImageView = getUnknownImageView(null, 13, 8, false);
        ImageView opponentDrawPileImageView = getUnknownImageView(null, 1, 2, false);
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
                    imageView = getCardImageView(myPlayer.getSpellsAndTrapFieldList()[i], myCardPositions[i], 8, true);
                else
                    imageView = getUnknownImageView(myPlayer.getSpellsAndTrapFieldList()[i], myCardPositions[i], 8, true);
                fieldGridPane.add(imageView, myCardPositions[i], 8);
            }
    }

    private void refreshOpponentSpellsAndTraps() {
        for (int i = 1; i <= Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getOpponent().getSpellsAndTrapFieldList()[i] != null) {
                Card card = myPlayer.getOpponent().getSpellsAndTrapFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getOpponent().getSpellsAndTrapFieldList()[i], opponentCardPositions[i], 2, true);
                else
                    imageView = getUnknownImageView(myPlayer.getOpponent().getSpellsAndTrapFieldList()[i], opponentCardPositions[i], 2, true);
                fieldGridPane.add(imageView, opponentCardPositions[i], 2);
            }
    }

    private Paint getPaint(int red , int green , int blue) {
        return Paint.valueOf(String.format("#%02X%02X%02X" , red , green , blue));
    }
    private void refreshLifePointAndPhase() {
        firstPlayerLP.setText(myPlayer.getLifePoint() + "");
        secondPlayerLP.setText(myPlayer.getOpponent().getLifePoint() + "");
        firstPlayerLP.setFill(getPaint((8000 - myPlayer.getLifePoint()) * 255 / 8000 , myPlayer.getLifePoint() * 255 / 8000,0));
        secondPlayerLP.setFill(getPaint((8000 - myPlayer.getOpponent().getLifePoint()) * 255 / 8000 , myPlayer.getOpponent().getLifePoint() * 255 / 8000,0));
        phaseNameText.setText(game.getCurrentPhase().toString());
    }

    private void refreshAvatarAndTitles() {
        firstPlayerAvatar.setImage(myUser.getAvatar());
        secondPlayerAvatar.setImage(myPlayer.getOpponent().getUser().getAvatar());
        firstPlayerTitle.setText(myUser.getNickname());
        secondPlayerTitle.setText(myPlayer.getOpponent().getUser().getNickname());
    }

    private ImageView getCardImageView(Card card, int x, int y, boolean changeSelectedCard) {
        Image image = card.getImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(fieldGridPane.getCellBounds(x, y).getHeight());
        imageView.setFitWidth(fieldGridPane.getCellBounds(x, y).getWidth());
        if (changeSelectedCard)
            imageView.setOnMouseClicked(mouseEvent -> {
                myPlayer.setSelectedCard(card);
                refresh();
            });
        return imageView;
    }

    @SneakyThrows
    private ImageView getUnknownImageView(Card card, int x, int y, boolean changeSelectedCard) {
        Image image = new Image(getClass().getResource("/cards_images/Unknown.jpg").toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(fieldGridPane.getCellBounds(x, y).getHeight());
        imageView.setFitWidth(fieldGridPane.getCellBounds(x, y).getWidth());
        if (changeSelectedCard)
            imageView.setOnMouseClicked(mouseEvent -> {
                myPlayer.setSelectedCard(card);
                refresh();
            });
        return imageView;
    }

    private void refreshMyHand() { // [1,10] ... [13,10]
        int handSize = myPlayer.getHand().getSize();
        for (int i = 0; i < handSize; ++i) {
            fieldGridPane.add(getCardImageView(myPlayer.getHand().getCardsList().get(i), 1 + 2 * i, 10, true), 1 + 2 * i, 10);
        }
    }

    private void refreshOpponentHand() { // [1,0] ... [13,0]
        int handSize = myPlayer.getOpponent().getHand().getSize();
        for (int i = 0; i < handSize; ++i) {
            fieldGridPane.add(getUnknownImageView(myPlayer.getOpponent().getHand().getCardsList().get(i), 1 + 2 * i, 0, true), 1 + 2 * i, 0);
        }
    }

    @SneakyThrows
    private void refreshSelectedCardDetails() {
        Card selectedCard = myPlayer.getSelectedCard();
        cardDescription.setText("card description");
        cardTitle.setText("Card title");
        cardImageView.setImage(new Image(getClass().getResource("/cards_images/Unknown.jpg").toURI().toString()));
        if (selectedCard == null || (!selectedCard.isFaceUp() && selectedCard.getPlayer() == myPlayer.getOpponent()))
            return;
        cardDescription.setText(selectedCard.getCardDescription());
        cardTitle.setText(selectedCard.getCardName());
        if (selectedCard.isFaceUp() || selectedCard.getPlayer() == myPlayer)
            cardImageView.setImage(Card.getCardByName(selectedCard.getCardName()).getImage());
    }

    private void refreshMyMonsters() {
        for (int i = 1; i <= Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getMonstersFieldList()[i] != null) {
                Card card = myPlayer.getMonstersFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getMonstersFieldList()[i], myCardPositions[i], 6, true);
                else
                    imageView = getUnknownImageView(myPlayer.getMonstersFieldList()[i], myCardPositions[i], 6, true);
                imageView.setRotate( ((MonsterCard)card).isDefenseMode()  ? 90 : 0 );
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
                    imageView = getCardImageView(myPlayer.getOpponent().getMonstersFieldList()[i], opponentCardPositions[i], 4, true);
                else
                    imageView = getUnknownImageView(myPlayer.getOpponent().getMonstersFieldList()[i], opponentCardPositions[i], 4, true);
                imageView.setRotate( ((MonsterCard)card).isDefenseMode()  ? 90 : 0 );
                fieldGridPane.add(imageView, opponentCardPositions[i], 4);
                makeOpponentMonsterSupportDrag(imageView, i);
            }
    }

    private void manageNextPhaseButton() {
        Button nextPhaseButton = new Button("next phase");
        nextPhaseButton.setPrefWidth(buttonsVBox.getWidth());
        nextPhaseButton.setOnMouseClicked(event -> {
            game.proceedNextPhase();
            refresh();
        });
        buttonsVBox.getChildren().add(nextPhaseButton);
    }

    private void manageSummonButton() {
        Button summonButton = new Button("summon");
        summonButton.setPrefWidth(buttonsVBox.getWidth());
        summonButton.setOnMouseClicked(event -> {
            myPlayer.summonMonster();
            refresh();
        });
        buttonsVBox.getChildren().add(summonButton);
    }

    private void manageSetButton() {
        Button setButton = new Button("set");
        setButton.setPrefWidth(buttonsVBox.getWidth());
        setButton.setOnMouseClicked(event -> {
            myPlayer.setMonster();
            refresh();
        });
        buttonsVBox.getChildren().addAll(setButton);
    }

    private void manageChangePositionButton() {
        Button changePosition = new Button("change position");
        changePosition.setPrefWidth(buttonsVBox.getWidth());
        changePosition.setOnMouseClicked(event -> {
            myPlayer.changeMonsterPosition();
            refresh();
        });
        buttonsVBox.getChildren().add(changePosition);
    }

    private void manageFlipSummonButton() {
        Button flipSummon = new Button("flip summon");
        flipSummon.setPrefWidth(buttonsVBox.getWidth());
        flipSummon.setOnMouseClicked(event -> {
            myPlayer.flipSummon();
            refresh();
        });
        buttonsVBox.getChildren().add(flipSummon);
    }

    private void manageAttackDirectButton() {
        Button attackDirect = new Button("attack direct");
        attackDirect.setPrefWidth(buttonsVBox.getWidth());
        attackDirect.setOnMouseClicked(event -> {
            myPlayer.attackDirect();
            refresh();
        });
        buttonsVBox.getChildren().add(attackDirect);
    }

    private void manageSpellOrTrapSetButton() {
        Button setSpellOrTrap = new Button("set spell or trap");
        setSpellOrTrap.setPrefWidth(buttonsVBox.getWidth());
        setSpellOrTrap.setOnMouseClicked(event -> {
            myPlayer.setSpellOrTrap();
            refresh();
        });
        buttonsVBox.getChildren().add(setSpellOrTrap);
    }

    private void manageActivateEffectButton() {
        Button activateEffect = new Button("activate effect");
        activateEffect.setPrefWidth(buttonsVBox.getWidth());
        activateEffect.setOnMouseClicked(event -> {
            myPlayer.activateEffect();
            refresh();
        });
        buttonsVBox.getChildren().add(activateEffect);

    }

    private void refreshButtonsVBox() {
        if (Game.getActivePlayer() != myPlayer)
            return;
        manageNextPhaseButton();
        if (game.getCurrentPhase() == Phase.MAIN1 || game.getCurrentPhase() == Phase.MAIN2) {
            if (myPlayer.getSelectedCard() != null && myPlayer.getSelectedCardOnHandID() != -1 && myPlayer.getHand().getCardsList().get(myPlayer.getSelectedCardOnHandID()) instanceof MonsterCard) {
                manageSummonButton();
                manageSetButton();
            }
            if (myPlayer.getSelectedCard() != null && myPlayer.getSelectedMonsterCardOnFieldID() != -1 && !((MonsterCard) myPlayer.getSelectedCard()).isHasChangedPositionThisTurn())
                manageChangePositionButton();
            if (myPlayer.getSelectedCard() != null && myPlayer.getSelectedMonsterCardOnFieldID() != -1 && !(myPlayer.getSelectedCard()).isFaceUp())
                manageFlipSummonButton();
            if (myPlayer.getSelectedCard() != null && myPlayer.getSelectedCardOnHandID() != -1 && (myPlayer.getSelectedCard() instanceof SpellCard || myPlayer.getSelectedCard() instanceof TrapCard))
                manageSpellOrTrapSetButton();
            if (myPlayer.getSelectedCard() != null && (myPlayer.getSelectedCardOnHandID() != -1 || myPlayer.getSpellOrTrapPositionOnBoard(myPlayer.getSelectedCard()) != 1) && (myPlayer.getSelectedCard() instanceof SpellCard || myPlayer.getSelectedCard() instanceof TrapCard))
                manageActivateEffectButton();
        }
        if (game.getCurrentPhase() == Phase.BATTLE && (game.getTurn() != 1)) {
            if (myPlayer.getSelectedCard() != null && myPlayer.getSelectedMonsterCardOnFieldID() != -1) {
                manageAttackDirectButton();
            }
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
            myPlayer.setSelectedCard(card);
        });
        cardView.setOnMouseDragged(mouseEvent -> mouseEvent.setDragDetect(true));
    }

    private void makeOpponentMonsterSupportDrag(ImageView imageView, int monsterIndex) {
        imageView.setOnDragOver(dragEvent -> {
            if (dragEvent.getDragboard().hasImage()) {
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            dragEvent.consume();
        });

        imageView.setOnDragDropped(dragEvent -> {
            if(game.getCurrentPhase() != Phase.BATTLE || Game.getActivePlayer() != myPlayer)
                return ;
            if (dragEvent.getDragboard().hasImage()) {
                System.out.println("Attack!");
                System.out.println("Defender: " + myPlayer.getOpponent()
                        .getMonstersFieldList()[monsterIndex].getCardName());

                myPlayer.attack(monsterIndex);
                refresh();
                dragEvent.setDropCompleted(true);
            } else dragEvent.setDropCompleted(false);
        });
    }
}