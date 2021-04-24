package data;

import card.Card;
import card.MonsterCard;
import card.SpellCard;
import card.TrapCard;
import game.Deck;
import game.GameDeck;
import game.User;

import java.util.ArrayList;
import java.util.Collections;

public class Printer {
    public static void showCard(Card card) {
        card.showCard();
    }

    public static void prompt(String message) {
        System.out.println(message);
    }

    public static void showDeck(GameDeck gameDeck , boolean isSideDeck) {
        String result = "";
        result += "Deck: " + gameDeck.getName() + "\n";
        result += (isSideDeck ? "Side" : "Main") + "deck:\n";
        Deck deck = gameDeck.getMainDeck();
        if(isSideDeck) deck = gameDeck.getSideDeck();
        result += "Monsters:\n";
        for(Card card : deck.getCardsList())
            if(card instanceof MonsterCard)
                result += card.getCardName() + ": " + card.getCardDescription() + "\n";
        result += "Spells and Traps:\n";
        for(Card card : deck.getCardsList())
            if(card instanceof SpellCard || card instanceof TrapCard)
                result += card.getCardName() + ": " + card.getCardDescription() + "\n";
        Printer.prompt(result);
    }
    public static void showScoreBoard() {
        ArrayList<User> users = User.getAllUsers();
        Collections.sort(users);
        int ind = 0;
        int rank = 0;
        int prevScore = -1;
        for (User user : users) {
            if (user.getScore() == prevScore) {
                System.out.println(rank + "- " + user.getNickname() + ": " + user.getScore());
            } else {
                prevScore = user.getScore();
                System.out.println(ind + 1 + "- " + user.getNickname() + ": " + user.getScore());
                rank = ind + 1;
            }
            ind++;
        }
    }

}