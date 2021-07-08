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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;

import java.net.URL;
import java.util.ResourceBundle;

public class MainGameMenu extends View implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void refresh() {
        // do all updates here
        fieldGridPane.getChildren().clear();
        buttonsVBox.getChildren().clear();
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
    private void refreshMySpellsAndTraps() {
        for (int i = 0; i < Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getSpellsAndTrapFieldList()[i] != null) {
                Card card = myPlayer.getSpellsAndTrapFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getSpellsAndTrapFieldList()[i], 3 + 2 * i, 8, true);
                else
                    imageView = getUnknownImageView(myPlayer.getSpellsAndTrapFieldList()[i], 3 + 2 * i, 8, true);
                fieldGridPane.add(imageView, 3 + 2 * i, 8);
            }
    }
    private void refreshOpponentSpellsAndTraps() {
        for (int i = 0; i < Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getOpponent().getSpellsAndTrapFieldList()[i] != null) {
                Card card = myPlayer.getOpponent().getSpellsAndTrapFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getOpponent().getSpellsAndTrapFieldList()[i], 3 + 2 * i, 2, true);
                else
                    imageView = getUnknownImageView(myPlayer.getOpponent().getSpellsAndTrapFieldList()[i], 3 + 2 * i, 2, true);
                fieldGridPane.add(imageView, 3 + 2 * i, 2);
            }
    }
    private void refreshLifePointAndPhase() {
        firstPlayerLP.setText(myPlayer.getLifePoint() + "");
        secondPlayerLP.setText(myPlayer.getOpponent().getLifePoint() + "");
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

    private void refreshMyMonsters() { // [3 , 6] ... [11 , 6]
        for (int i = 0; i < Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getMonstersFieldList()[i] != null) {
                Card card = myPlayer.getMonstersFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getMonstersFieldList()[i], 3 + 2 * i, 6, true);
                else
                    imageView = getUnknownImageView(myPlayer.getMonstersFieldList()[i], 3 + 2 * i, 6, true);
                if (((MonsterCard) card).isDefenseMode())
                    imageView.setRotate(90);
                fieldGridPane.add(imageView, 3 + 2 * i, 6);
            }
    }

    private void refreshOpponentMonsters() { // [3 , 4] ... [11 , 4]
        for (int i = 0; i < Player.getFIELD_SIZE(); ++i)
            if (myPlayer.getOpponent().getMonstersFieldList()[i] != null) {
                Card card = myPlayer.getOpponent().getMonstersFieldList()[i];
                ImageView imageView;
                if (card.isFaceUp())
                    imageView = getCardImageView(myPlayer.getOpponent().getMonstersFieldList()[i], 3 + 2 * i, 4, true);
                else
                    imageView = getUnknownImageView(myPlayer.getOpponent().getMonstersFieldList()[i], 3 + 2 * i, 4, true);
                if (card instanceof MonsterCard && ((MonsterCard) card).isDefenseMode())
                    imageView.setRotate(90);
                fieldGridPane.add(imageView, 3 + 2 * i, 4);
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

    private void manageAttackButton() {
        // todo
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
            if (myPlayer.getSelectedCard() != null && myPlayer.getSelectedMonsterCardOnFieldID() != -1 && !( myPlayer.getSelectedCard()).isFaceUp())
                manageFlipSummonButton();
            if(myPlayer.getSelectedCard() != null && myPlayer.getSelectedCardOnHandID() != -1 && (myPlayer.getSelectedCard() instanceof SpellCard || myPlayer.getSelectedCard() instanceof TrapCard))
                manageSpellOrTrapSetButton();
            if(myPlayer.getSelectedCard() != null && (myPlayer.getSelectedCardOnHandID() != -1 || myPlayer.getSpellOrTrapPositionOnBoard(myPlayer.getSelectedCard()) != 1) && (myPlayer.getSelectedCard() instanceof SpellCard || myPlayer.getSelectedCard() instanceof TrapCard))
                manageActivateEffectButton();
        }
        if (game.getCurrentPhase() == Phase.BATTLE && (game.getTurn() != 1)) {
            if (myPlayer.getSelectedCard() != null && myPlayer.getSelectedMonsterCardOnFieldID() != -1) {
//                manageAttackButton();
                if (true) // todo : if opponent has no monsters
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
            cardImage.fitWidthProperty().bind(stage.widthProperty().multiply(.8/rowCount));
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
}
