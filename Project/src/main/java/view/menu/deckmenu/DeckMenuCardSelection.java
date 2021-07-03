package view.menu.deckmenu;

import card.Card;
import game.Deck;
import game.GameDeck;
import game.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import view.UtilityView;
import view.View;
import view.components.CardComponent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckMenuCardSelection extends View implements Initializable {
    @Setter private static Deck currentDeck;
    @Setter private static User currentUser;
    @Setter private static GameDeck currentGameDeck;
    @Getter private static Card lastPickedCard;
    private static int currentCardId;
    public CardComponent cardComponent;

    @SneakyThrows
    public void selectCard(ActionEvent actionEvent) {
        if(cardComponent.getSelectedCardName() == null) {
            UtilityView.displayMessage("no card was selected");
            loadView("deck_view");
            return ;
        }
        lastPickedCard = Card.getCardByName(cardComponent.getSelectedCardName());
        if(currentDeck.getCardCount(lastPickedCard.getCardName()) == 3) {
            UtilityView.displayMessage("you can't add more than 3 cards of a type");
        } else if(currentUser.getCardBalance(lastPickedCard.getCardName()) == currentGameDeck.getMainDeck().getCardCount(lastPickedCard.getCardName()) + currentGameDeck.getSideDeck().getCardCount(lastPickedCard.getCardName())) {
            UtilityView.displayMessage("you already have all owned cards of this type in your deck");
        } else {
            UtilityView.displayMessage("card added successfully");
            currentDeck.addCard(lastPickedCard);
        }
        loadView("deck_view");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Card card : Card.getAllCards()) {
            int count = currentUser.getCardBalance(card.getCardName());
            for(int i = 0;i < count;++ i)
                cardComponent.addCard(card);
        }
    }
}
