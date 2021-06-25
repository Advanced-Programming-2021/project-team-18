package data;

import card.Card;
import card.MonsterCard;
import card.SpellCard;
import card.TrapCard;
import game.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Printer {
    public static void showCard(Card card) {
        if (card == null) {
            Printer.prompt("no card");
        } else {
            card.showCard();
        }
    }

    public static void prompt(String message) {
        if (Game.getActivePlayer() instanceof AIPlayer) return;
        System.out.println(message);
    }

    public static void forcePrompt(String message) {
        System.out.println(message);
    }

    public static void showBoard(Player player, Player opponent) {
        if (player instanceof AIPlayer) return;
        System.out.println(opponent.getUser().getNickname() + ":" + opponent.getLifePoint());
        int handSize = opponent.getHand().getCardsList().size();
        for (int i = 0; i < handSize; i++) System.out.print("\tC");
        System.out.print("\n");
        //System.out.println("\n" + opponent.getRemainingDeck().getCardsList().size());
        int[] opponentSequence = {4, 2, 1, 3, 5};
        showSpellsField(opponent, opponentSequence);
        System.out.print("\n");
        showMonstersField(opponent, opponentSequence);
        System.out.print(opponent.getGraveyard().getCardsList().size() + "\t\t\t\t\t");
        SpellCard spellCard = opponent.getFieldZone();
        if (spellCard == null) System.out.println("E");
        else System.out.println("O");
        System.out.println("\n--------------------------\n");
        int[] playerSequence = {5, 3, 1, 2, 4};
        SpellCard spellCard2 = player.getFieldZone();
        if (spellCard2 == null) System.out.print("E");
        else System.out.print("O");
        System.out.println("\t\t\t\t\t" + player.getGraveyard().getCardsList().size());
        showMonstersField(player, playerSequence);
        showSpellsField(player, playerSequence);
        System.out.print("\n");
        //System.out.println("\t\t\t\t\t\t" + player.getRemainingDeck().getCardsList().size());
        handSize = player.getHand().getCardsList().size();
        for (int i = 0; i < handSize; i++) {
            System.out.print("\tC");
        }
        System.out.print("\n");
        System.out.println(player.getUser().getNickname() + ":" + player.getLifePoint());
    }

    private static void showMonstersField(Player player, int[] playerSequence) {
        for (int i = 0; i < 5; i++) {
            MonsterCard monster = player.getMonstersFieldList()[playerSequence[i]];
            if (monster == null) System.out.print("\tE");
            else if (!monster.isDefenseMode() && monster.isFaceUp()) System.out.print("\tOO");
            else if (monster.isDefenseMode() && monster.isFaceUp()) System.out.print("\tDO");
            else System.out.print("\tDH");
        }
        System.out.print("\n");
    }

    private static void showSpellsField(Player player, int[] playerSequence) {
        for (int i = 0; i < 5; i++) {
            Card spellOrTrap = player.getSpellsAndTrapFieldList()[playerSequence[i]];
            if (spellOrTrap == null) System.out.print("\tE");
            else if (!spellOrTrap.isFaceUp()) System.out.print("\tO");
            else System.out.print("\tH");
        }
    }

    public static void showDeck(GameDeck gameDeck, boolean isSideDeck) {
        StringBuilder result = new StringBuilder();
        result.append("Deck: ").append(gameDeck.getName()).append("\n");
        result.append(isSideDeck ? "Side" : "Main").append("deck:\n");
        Deck deck = gameDeck.getMainDeck();
        if (isSideDeck) deck = gameDeck.getSideDeck();
        result.append("Monsters:\n");
        for (Card card : deck.getCardsList())
            if (card instanceof MonsterCard)
                result.append(card.getCardName()).append(": ").append(card.getCardDescription()).append("\n");
        result.append("Spells and Traps:\n");
        for (Card card : deck.getCardsList())
            if (card instanceof SpellCard || card instanceof TrapCard)
                result.append(card.getCardName()).append(": ").append(card.getCardDescription()).append("\n");
        Printer.prompt(result.toString());
    }

    public static void showScoreBoard() {
        List<User> users = new ArrayList<>(User.getAllUsers());
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