package menus;

import card.Card;
import data.Printer;
import game.Deck;
import game.GameDeck;
import game.User;
import utility.Utility;

import java.util.HashMap;
import java.util.regex.Matcher;

// TODO : PASHA
public class DeckMenu extends Menu {
    User user;
    public DeckMenu(User user) {
        this.user = user;
    }

    public void showCard(Matcher matcher) {
        matcher.matches();
        Printer.showCard(Card.getCardByName(matcher.group(1)));
    }

    public void createDeck(Matcher matcher) {
        matcher.matches();
        String deckName = matcher.group(1);
        if(user.getGameDeckByName(deckName) != null) {
            Printer.prompt("deck with name " + deckName + " already exists"); return ;
        }
        Printer.prompt("deck created successfully!");
        GameDeck deck = new GameDeck(deckName , user);
        user.getDecks().add(deck);
    }

    public void deleteDeck(Matcher matcher) {
        matcher.matches();
        String deckName = matcher.group(1);
        GameDeck deck = user.getGameDeckByName(deckName);
        if(deck == null) {
            Printer.prompt("deck with name " + deckName + " does not exist"); return ;
        }
        Printer.prompt("deck deleted successfully");
        user.getDecks().remove(deck);
        if(user.getActiveDeck().getName().equals(deck.getName()))
            user.setActiveDeck(null);
    }

    public void setActiveDeck(Matcher matcher) {
        matcher.matches();
        String deckName = matcher.group(1);
        GameDeck deck = user.getGameDeckByName(deckName);
        if(deck == null) {
            Printer.prompt("deck with name " + deckName + " does not exist"); return ;
        }
        Printer.prompt("deck activated successfully");
        user.setActiveDeck(deck);
    }

    public void addCardToDeck(Matcher matcher) {
        matcher.matches();
        HashMap<String,String> map = Utility.getCommand(matcher.group(1));
        String[] mustAttributes = {"card" , "deck"};
        String[] optionalAttributs = {"side"};
        if(! Utility.isCommandValid(map , mustAttributes , optionalAttributs)) {
            Printer.prompt("Invalid command"); return ;
        }
        String cardName = map.get("card");
        String deckName = map.get("deck");
        boolean isSideDeck = matcher.group(1).contains("--side");
        Card card = Card.getCardByName(cardName);
        GameDeck gameDeck = user.getGameDeckByName(deckName);
        Deck deck = gameDeck.getMainDeck();
        if(isSideDeck) deck = gameDeck.getSideDeck();
        if(gameDeck == null) {
            Printer.prompt("deck with name " + deckName + " does not exist"); return ;
        }
        if(Card.getCardByName(cardName) == null || user.getCardCount().getOrDefault(cardName , 0) == gameDeck.getMainDeck().getCardCount(cardName) + gameDeck.getSideDeck().getCardCount(cardName))  {
            Printer.prompt("card with name " + map.get("card") + " does not exist"); return ;
        }
        if((isSideDeck && deck.getCardsList().size() == 15) || (! isSideDeck && deck.getCardsList().size() == 60)) {
            Printer.prompt((isSideDeck ? "side" : "main") + " deck is full"); return ;
        }
        if(gameDeck.getMainDeck().getCardCount(cardName) + gameDeck.getSideDeck().getCardCount(cardName) == 3) {
            Printer.prompt("there are already three cards with name " + cardName + " in deck " + deckName); return ;
        }
        Printer.prompt("card added to deck successfully");
        deck.getCardsList().add(card);
    }

    public void removeCardFromDeck(Matcher matcher) {
        matcher.matches();
        HashMap<String,String> map = Utility.getCommand(matcher.group(1));
        String[] mustAttributes = {"card" , "deck"};
        String[] optionalAttributs = {"side"};
        if(! Utility.isCommandValid(map , mustAttributes , optionalAttributs)) {
            Printer.prompt("Invalid command"); return ;
        }
        String cardName = map.get("card");
        String deckName = map.get("deck");
        boolean isSideDeck = matcher.group(1).contains("--side");
        Card card = Card.getCardByName(cardName);
        GameDeck gameDeck = user.getGameDeckByName(deckName);
        Deck deck = gameDeck.getMainDeck();
        if(isSideDeck) deck = gameDeck.getSideDeck();
        if(gameDeck == null) {
            Printer.prompt("deck with name " + deckName + " does not exist"); return ;
        }
        if(Card.getCardByName(cardName) == null || deck.getCardCount(cardName) == 0)  {
            Printer.prompt("card with name " + cardName + " does not exist in " + (isSideDeck ? "side" : "main") + " deck"); return ;
        }
        if(gameDeck.getMainDeck().getCardCount(cardName) + gameDeck.getSideDeck().getCardCount(cardName) == 3) {
            Printer.prompt("there are already three cards with name " + cardName + " in deck " + deckName); return ;
        }
        Printer.prompt("card removed from deck successfully");
        deck.getCardsList().remove(card);
    }

    public String getAllDecksFormat(GameDeck deck) {
        return deck.getName() + ": main deck " + deck.getMainDeck().getCardsList().size() + ", side deck " + deck.getSideDeck().getCardsList().size() + ", " + (deck.isDeckValid()?"":"in") + "valid";
    }
    public void showAllDecks(Matcher matcher) {
        matcher.matches();
        if(! matcher.group(1).contains("-a") ) {
            Printer.prompt("Invalid command"); return ;
        }
        String result = "";
        result += "Decks:\n";
        result += "Active deck:\n";
        if(user.getActiveDeck() != null)
            result += getAllDecksFormat(user.getActiveDeck()) + "\n";
        result += "Other decks:\n";
        for(GameDeck deck : user.getDecks())
            if(user.getActiveDeck() == null || (! user.getActiveDeck().getName().equals(deck.getName())))
                result += getAllDecksFormat(deck) + "\n";
        Printer.prompt(result);
    }

    public void showDeck(Matcher matcher) {
        matcher.matches();
        HashMap<String,String> map = Utility.getCommand(matcher.group(1));
        if(! Utility.isCommandValid(map , new String[] {"deck-name"} , new String[] {"side"})) {
            Printer.prompt("Invalid command!"); return ;
        }
        boolean isSideDeck = matcher.group(1).contains("--side");
        String deckName = map.get("deck-name");
        if(user.getGameDeckByName(deckName) == null) {
            Printer.prompt("no deck with name " + deckName + " exists"); return ;
        }
        Printer.showDeck(user.getGameDeckByName(deckName) , isSideDeck);
    }

    public void showDeckCards(Matcher matcher) {
        matcher.matches();
        if(! matcher.group(1).contains("--cards")) {
            Printer.prompt("Invalid command!"); return ;
        }
        String result = "";
        for(Card card : Card.getAllCards())
            if(user.getCardCount().getOrDefault(card.getCardName() , 0) > 0)
                result += card.getCardName() + ":" + card.getCardDescription() + "\n";
        Printer.prompt(result);
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
        String regexDeckShowAll = "deck\\sshow\\s(.+)";
        String regexShowDeck = "deck\\sshow\\s(.+)";
        String regexShowDeckCards = "deck\\sshow\\s(.+)";
        while(true) {
            String input = Utility.getNextLine();
            if(Utility.getCommandMatcher(input , regexMenuExit).matches()) {
                return ;
            } else if(Utility.getCommandMatcher(input , regexShowCurrentMenu).matches()) {
                Printer.prompt("Deck Menu");
            } else if(Utility.getCommandMatcher(input , regexShowCard).matches()) {
                showCard(Utility.getCommandMatcher(input , regexShowCard));
            } else if(Utility.getCommandMatcher(input , regexCreateDeck).matches()) {
                createDeck(Utility.getCommandMatcher(input , regexCreateDeck));
            } else if(Utility.getCommandMatcher(input , regexDeleteDeck).matches()) {
                deleteDeck(Utility.getCommandMatcher(input , regexDeleteDeck));
            } else if(Utility.getCommandMatcher(input , regexSetActiveDeck).matches()) {
                setActiveDeck(Utility.getCommandMatcher(input , regexSetActiveDeck));
            } else if(Utility.getCommandMatcher(input , regexAddCardToDeck).matches()) {
                addCardToDeck(Utility.getCommandMatcher(input , regexAddCardToDeck));
            } else if(Utility.getCommandMatcher(input , regexRemoveCardFromDeck).matches()) {
                removeCardFromDeck(Utility.getCommandMatcher(input , regexRemoveCardFromDeck));
            } else if(Utility.getCommandMatcher(input , regexDeckShowAll).matches()) {
                showAllDecks(Utility.getCommandMatcher(input , regexDeckShowAll));
            } else if(Utility.getCommandMatcher(input , regexShowDeck).matches()) {
                showDeck(Utility.getCommandMatcher(input , regexShowDeck));
            } else if(Utility.getCommandMatcher(input , regexShowDeckCards).matches()) {
                showDeckCards(Utility.getCommandMatcher(input , regexShowDeckCards));
            } else {
                Printer.prompt("Invalid Command!");
            }
        }
    }
}
