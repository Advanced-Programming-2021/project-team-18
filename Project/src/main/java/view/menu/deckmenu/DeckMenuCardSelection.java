package view.menu.deckmenu;

import card.Card;
import game.Deck;
import game.GameDeck;
import game.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import menus.Menu;
import menus.MenuController;
import view.UtilityView;
import view.View;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DeckMenuCardSelection extends View implements Initializable {
    @Setter
    private static Deck currentDeck;
    private static User currentUser;
    @Setter
    private static GameDeck currentGameDeck;
    @Getter
    private static Card lastPickedCard;
    private static int currentCardId;
    public ImageView imageView;

    public static void setCurrentUser(User currentUser) {
        DeckMenuCardSelection.currentUser = currentUser;
        MenuController.getInstance().setUser(currentUser);
    }

    public void leftButton(ActionEvent actionEvent) {
        if (currentCardId > 0)
            --currentCardId;
        updateImageView();
    }

    public void rightButton(ActionEvent actionEvent) {
        if (currentCardId < Card.getAllCards().size() - 1)
            ++currentCardId;
        updateImageView();
    }

    @SneakyThrows
    public void selectCard(ActionEvent actionEvent) {
        lastPickedCard = Card.getAllCards().get(currentCardId);
        if (currentDeck.getCardCount(lastPickedCard.getCardName()) == 3) {
            UtilityView.displayMessage("you can't add more than 3 cards of a type");
        } else if (currentUser.getCardBalance(lastPickedCard.getCardName()) == currentGameDeck.getMainDeck().getCardCount(lastPickedCard.getCardName()) + currentGameDeck.getSideDeck().getCardCount(lastPickedCard.getCardName())) {
            UtilityView.displayMessage("you already have all owned cards of this type in your deck");
        } else {
            UtilityView.displayMessage("card added successfully");
            currentDeck.addCard(lastPickedCard);
        }
        loadView("deck_view");
    }

    @SneakyThrows
    private void updateImageView() {
        imageView.setImage(null);
        String cardName = Card.getAllCards().get(currentCardId).getCardName();
        cardName = cardName.replaceAll(" ", "_");
        File file = new File((Objects.requireNonNull(getClass().getResource(
                "/cards_images/" + cardName + ".jpg"))).toURI());
        imageView.setImage(new Image(file.toURI().toString()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentCardId = 0;
        updateImageView();
    }
}
