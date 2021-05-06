package data;

import card.Card;
import card.MonsterCard;
import card.SpellCard;
import card.TrapCard;
import game.Deck;
import game.GameDeck;
import game.Player;
import game.User;

import java.util.Collections;
import java.util.List;

public class Printer {
    public static void showCard(Card card) {
        card.showCard();
    }

    public static void prompt(String message) {
        System.out.println(message);
    }

    public static void showBoard(Player player, Player opponent) {
        System.out.println(opponent.getUser().getNickname() + ":" + opponent.getLifePoint());
        int handSize = opponent.getHand().getCardsList().size();
        for (int i = 0; i < handSize; i++) System.out.print("\tC");
        System.out.println("\n" + opponent.getRemainingDeck().getCardsList().size());
        int[] opponentSequence = {4, 2, 1, 3, 5};
        showSpellsField(opponent, opponentSequence);
        System.out.print("\n");
        showMonstersField(opponent, opponentSequence);
        System.out.print(opponent.getGraveyard().getCardsList().size() + "\t\t\t\t\t");
        SpellCard spellCard = opponent.getFieldZone();
        if (spellCard == null) System.out.println("O");
        else System.out.println("E");
        System.out.println("\n--------------------------\n");
        int[] playerSequence = {5, 3, 1, 2, 4};
        SpellCard spellCard2 = player.getFieldZone();
        if (spellCard2 == null) System.out.print("O");
        else System.out.print("E");
        System.out.println("\t\t\t\t\t" + player.getGraveyard().getCardsList().size());
        showMonstersField(player, playerSequence);
        showSpellsField(player, playerSequence);
        System.out.println("\t\t\t\t\t\t" + player.getRemainingDeck().getCardsList().size());
        handSize = player.getHand().getCardsList().size();
        for(int i = 0 ; i < handSize ; i++){
            System.out.print("C\t");
        }
        System.out.print("\n");
        System.out.println(player.getUser().getNickname() + ":" + player.getLifePoint());
    }

    private static void showMonstersField(Player player, int[] playerSequence) {
        for (int i = 0; i < 5; i++) {
            MonsterCard monster = player.getMonstersFieldList()[playerSequence[i]];
            Card card = (Card) monster;
            if (monster == null) System.out.print("\tE");
            else if (!monster.isDefenseMode() && card.isFaceUp()) System.out.print("\tOO");
            else if (monster.isDefenseMode() && card.isFaceUp()) System.out.print("\tDO");
            else System.out.print("\tDH");
        }
        System.out.print("\n");
    }

    private static void showSpellsField(Player player, int[] playerSequence) {
        for (int i = 0; i < 5 ; i++) {
            Card spellOrTrap = player.getSpellsAndTrapFieldList()[playerSequence[i]];
            if (spellOrTrap == null) System.out.print("\tE");
            else if (spellOrTrap.isFaceUp()) System.out.print("\tO");
            else System.out.print("\tH");
        }
    }

    public static void showGraveyard(Deck deck) {

    }

    public static void showDeck(GameDeck gameDeck, boolean isSideDeck) {
        String result = "";
        result += "Deck: " + gameDeck.getName() + "\n";
        result += (isSideDeck ? "Side" : "Main") + "deck:\n";
        Deck deck = gameDeck.getMainDeck();
        if (isSideDeck) deck = gameDeck.getSideDeck();
        result += "Monsters:\n";
        for (Card card : deck.getCardsList())
            if (card instanceof MonsterCard)
                result += card.getCardName() + ": " + card.getCardDescription() + "\n";
        result += "Spells and Traps:\n";
        for (Card card : deck.getCardsList())
            if (card instanceof SpellCard || card instanceof TrapCard)
                result += card.getCardName() + ": " + card.getCardDescription() + "\n";
        Printer.prompt(result);
    }

    public static void showScoreBoard() {
        List<User> users = User.getAllUsers();
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