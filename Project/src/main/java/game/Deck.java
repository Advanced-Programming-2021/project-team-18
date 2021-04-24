package game;

import card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
// TODO : PASHA
@Getter
@Setter
public class Deck {

    private User owner;
    private ArrayList<Card> cardsList;


    public int getCardCount(String cardName) {
        int count = 0;
        for(Card myCard : cardsList)
            if(myCard.getCardName().equals(cardName))
                ++ count;
        return count;
    }
}
