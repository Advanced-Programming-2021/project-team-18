package menus;

import card.Card;
import data.Printer;
import game.Deck;
import game.GameDeck;
import game.User;
import utility.Utility;

import java.util.HashMap;
import java.util.regex.Matcher;

// by Pasha
public class DeckMenu extends Menu {
    User user;

    public DeckMenu(User user) {
        this.user = user;
    }

    public void showCard(Matcher matcher) {
        Printer.showCard(Card.getCardByName(matcher.group(1)));
    }

    public void createDeck(Matcher matcher) {
        String deckName = matcher.group(1);
        if (user.getGameDeckByName(deckName) != null) {
            Printer.prompt("deck with name " + deckName + " already exists");
            return;
        }
        Printer.prompt("deck created successfully!");
        user.addGameDeck(new GameDeck(deckName));
    }

    public void deleteDeck(Matcher matcher) {
        String deckName = matcher.group(1);
        GameDeck deck = user.getGameDeckByName(deckName);
        if (deck == null) {
            Printer.prompt("deck with name " + deckName + " does not exist");
            return;
        }
        Printer.prompt("deck deleted successfully");
        user.removeGameDeck(deck);
    }

    public void setActiveDeck(Matcher matcher) {
        String deckName = matcher.group(1);
        GameDeck deck = user.getGameDeckByName(deckName);
        if (deck == null) {
            Printer.prompt("deck with name " + deckName + " does not exist");
            return;
        }
        Printer.prompt("deck activated successfully");
        user.setActiveDeckName(deck.getName());
    }

    public void addCardToDeck(Matcher matcher) {
        HashMap<String, String> map = Utility.getCommand(matcher.group(1));
        String[] mustAttributes = {"card", "deck"};
        String[] optionalAttributes = {"side"};
        if (!Utility.isCommandValid(map, mustAttributes, optionalAttributes)) {
            Printer.prompt(INVALID_COMMAND);
            return;
        }
        String cardName = map.get("card");
        String deckName = map.get("deck");
        boolean isSideDeck = matcher.group(1).contains("--side");
        Card card = Card.getCardByName(cardName);
        GameDeck gameDeck = user.getGameDeckByName(deckName);
        if (gameDeck == null) {
            Printer.prompt("deck with name " + deckName + " does not exist");
            return;
        }
        Deck deck = gameDeck.getMainDeck();
        if (isSideDeck) deck = gameDeck.getSideDeck();

        if (Card.getCardByName(cardName) == null || user.getCardBalance(cardName) == gameDeck.getMainDeck().getCardCount(cardName) + gameDeck.getSideDeck().getCardCount(cardName)) {
            Printer.prompt("card with name " + map.get("card") + " does not exist");
            return;
        }
        if ((isSideDeck && deck.getCardsList().size() == 15) || (!isSideDeck && deck.getCardsList().size() == 60)) {
            Printer.prompt((isSideDeck ? "side" : "main") + " deck is full");
            return;
        }
        if (gameDeck.getMainDeck().getCardCount(cardName) + gameDeck.getSideDeck().getCardCount(cardName) == 3) {
            Printer.prompt("there are already three cards with name " + cardName + " in deck " + deckName);
            return;
        }
        Printer.prompt("card added to deck successfully");
        deck.getCardsList().add(card);
    }

    public void removeCardFromDeck(Matcher matcher) {
        HashMap<String, String> map = Utility.getCommand(matcher.group(1));
        String[] mustAttributes = {"card", "deck"};
        String[] optionalAttributes = {"side"};
        if (!Utility.isCommandValid(map, mustAttributes, optionalAttributes)) {
            Printer.prompt(INVALID_COMMAND);
            return;
        }
        String cardName = map.get("card");
        String deckName = map.get("deck");
        boolean isSideDeck = matcher.group(1).contains("--side");
        Card card = Card.getCardByName(cardName);
        GameDeck gameDeck = user.getGameDeckByName(deckName);
        if (gameDeck == null) {
            Printer.prompt("deck with name " + deckName + " does not exist");
            return;
        }
        Deck deck = gameDeck.getMainDeck();
        if (isSideDeck) deck = gameDeck.getSideDeck();
        if (Card.getCardByName(cardName) == null || deck.getCardCount(cardName) == 0) {
            Printer.prompt("card with name " + cardName + " does not exist in " + (isSideDeck ? "side" : "main") + " deck");
            return;
        }
        if (gameDeck.getMainDeck().getCardCount(cardName) + gameDeck.getSideDeck().getCardCount(cardName) == 3) {
            Printer.prompt("there are already three cards with name " + cardName + " in deck " + deckName);
            return;
        }
        Printer.prompt("card removed from deck successfully");
        deck.getCardsList().remove(card);
    }

    public String getAllDecksFormat(GameDeck deck) {
        return deck.getName() + ": main deck " + deck.getMainDeck().getCardsList().size() + ", side deck " + deck.getSideDeck().getCardsList().size() + ", " + (deck.isDeckValid() ? "" : "in") + "valid";
    }

    public void showAllDecks() {
        StringBuilder result = new StringBuilder();
        result.append("Decks:\n");
        result.append("Active deck:\n");
        if (user.getActiveDeckName() != null)
            result.append(getAllDecksFormat(user.getGameDeckByName(user.getActiveDeckName())) + "\n");
        result.append("Other decks:\n");
        for (GameDeck deck : user.getDecks())
            if (user.getActiveDeckName() == null || (!user.getActiveDeckName().equals(deck.getName())))
                result.append(getAllDecksFormat(deck) + "\n");
        Printer.prompt(result.toString());
    }

    public void showDeck(Matcher matcher) {
        System.out.println(matcher.group(1));
        HashMap<String, String> map = Utility.getCommand(matcher.group(1));
        for(String x : map.keySet())
            System.out.println(x + " : " + map.get(x));
        if (!Utility.isCommandValid(map, new String[]{"deck-name"}, new String[]{"side"})) {
            Printer.prompt(Menu.INVALID_COMMAND);
            return;
        }
        boolean isSideDeck = matcher.group(1).contains("--side");
        String deckName = map.get("deck-name");
        if (user.getGameDeckByName(deckName) == null) {
            Printer.prompt("no deck with name " + deckName + " exists");
            return;
        }
        Printer.showDeck(user.getGameDeckByName(deckName), isSideDeck);
    }

    public void showDeckCards() {
        StringBuilder result = new StringBuilder();
        for (Card card : Card.getAllCards())
            if (user.getCardBalance(card.getCardName()) > 0)
                result.append(card.getCardName() + ":" + card.getCardDescription() + "\n");
        Printer.prompt(result.toString());
    }

    @Override
    public void runMenu() {
        String regexMenuExit = "menu\\sexit";
        String regexShowCurrentMenu = "menu\\sshow\\-current";
        String regexShowCard = "card\\sshow\\s(\\w+)";
        String regexCreateDeck = "deck\\screate\\s(\\w+)";
        String regexDeleteDeck = "deck\\sdelete\\s(\\w+)";
        String regexSetActiveDeck = "deck\\sset\\-activate\\s(\\w+)";
        String regexAddCardToDeck = "deck\\sadd-card\\s(.+)";
        String regexRemoveCardFromDeck = "deck\\srm-card\\s(.+)";
        String regexDeckShowAll = "deck\\sshow\\s--all";
        String regexShowDeckCards = "deck\\sshow\\s--cards";
        String regexShowDeck = "deck\\sshow\\s(.+)";

        Matcher matcher;
        while (true) {
            String input = Utility.getNextLine();
            if ((Utility.getCommandMatcher(input, regexMenuExit)).matches()) {
                return;
            } else if ((Utility.getCommandMatcher(input, regexShowCurrentMenu)).matches()) {
                Printer.prompt("Deck Menu");
            } else if ((matcher = Utility.getCommandMatcher(input, regexShowCard)).matches()) {
                showCard(matcher);
            } else if ((matcher = Utility.getCommandMatcher(input, regexCreateDeck)).matches()) {
                createDeck(matcher);
            } else if ((matcher = Utility.getCommandMatcher(input, regexDeleteDeck)).matches()) {
                deleteDeck(matcher);
            } else if ((matcher = Utility.getCommandMatcher(input, regexSetActiveDeck)).matches()) {
                setActiveDeck(matcher);
            } else if ((matcher = Utility.getCommandMatcher(input, regexAddCardToDeck)).matches()) {
                addCardToDeck(matcher);
            } else if ((matcher = Utility.getCommandMatcher(input, regexRemoveCardFromDeck)).matches()) {
                removeCardFromDeck(matcher);
            } else if ((Utility.getCommandMatcher(input, regexDeckShowAll)).matches()) {
                showAllDecks();
            } else if ((Utility.getCommandMatcher(input, regexShowDeckCards)).matches()) {
                showDeckCards();
            } else if ((matcher = Utility.getCommandMatcher(input, regexShowDeck)).matches()) {
                showDeck(matcher);
            } else {
                Printer.prompt(Menu.INVALID_COMMAND);
            }
        }
    }
}
