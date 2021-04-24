package data;

import card.Card;
import game.GameDeck;
import game.User;

import java.util.ArrayList;
import java.util.Collections;

public class Printer {
    public static void showCard(Card card) {

    }

    public static void prompt(String message) {

    }

    public static void showDeck(GameDeck deck , boolean isSideDeck) {

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