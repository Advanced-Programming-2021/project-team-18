package menus;

import card.Card;
import data.Printer;
import game.Game;
import game.User;
import utility.Utility;

import java.util.HashMap;
import java.util.regex.Matcher;

// TODO : PASHA : ِDONE
public class DuelMenu extends Menu {
    User user;
    public DuelMenu(User user) {
        this.user = user;
    }
    public void showCard(Matcher matcher) {
        matcher.matches();
        Printer.showCard(Card.getCardByName(matcher.group(1)));
    }
    public void duelOtherPlayer(Matcher matcher) {
        matcher.matches();
        HashMap<String,String> map = Utility.getCommand(matcher.group(1));
        String[] mustAttributes = {"second-player" , "rounds"};
        if(! Utility.isCommandValid(map , mustAttributes , null)) {
            Printer.prompt("Invalid command!"); return ;
        }
        String secondPlayerName = map.get("second-player");
        int roundsCount = Integer.parseInt(map.get("rounds"));
        if(User.getUserByUsername(secondPlayerName) == null) {
            Printer.prompt("there is no player with this username"); return ;
        }
        if(user.getActiveDeck() == null) {
            Printer.prompt(user.getUsername() + "  has no active deck"); return ;
        }
        if(User.getUserByUsername(secondPlayerName).getActiveDeck() == null) {
            Printer.prompt(User.getUserByUsername(secondPlayerName).getUsername() + "  has no active deck"); return ;
        }
        if(! user.getActiveDeck().isDeckValid()) {
            Printer.prompt(user.getUsername() + "'s deck is invalid"); return ;
        }
        if(! User.getUserByUsername(secondPlayerName).getActiveDeck().isDeckValid()) {
            Printer.prompt(User.getUserByUsername(secondPlayerName).getUsername() + "'s deck is invalid"); return ;
        }
        if(roundsCount != 1 && roundsCount != 3) {
            Printer.prompt("number of rounds is not supported"); return ;
        }
        while(roundsCount > 0) {
            Game game = new Game(user, User.getUserByUsername(secondPlayerName));
            game.runGame();
            roundsCount --;
        }
    }
    public void duelAI(Matcher matcher) {
        matcher.matches();
        HashMap<String,String> map = Utility.getCommand(matcher.group(1));
        String[] mustAttributes = {"rounds"};
        if(! Utility.isCommandValid(map , mustAttributes , null)) {
            Printer.prompt("Invalid command!"); return ;
        }
        int roundsCount = Integer.parseInt(map.get("rounds"));
        if(user.getActiveDeck() == null) {
            Printer.prompt(user.getUsername() + "  has no active deck"); return ;
        }
        if(! user.getActiveDeck().isDeckValid()) {
            Printer.prompt(user.getUsername() + "'s deck is invalid"); return ;
        }
        if(roundsCount != 1 && roundsCount != 3) {
            Printer.prompt("number of rounds is not supported"); return ;
        }
        while(roundsCount > 0) {
            Game game = new Game(user, User.getUserByUsername("AI"));
            game.runGame();
            roundsCount --;
        }
    }
    @Override
    public void runMenu() {
        String regexMenuExit = "menu\\sexit";
        String regexShowCurrentMenu = "menu\\sshow\\-current";
        String regexShowCard = "card\\sshow\\s(\\w+)";
        String regexDuelOtherPlayer = "duel\\s\\-\\-new\\s(.+)";
        String regexDuelAI = "duel\\s\\-\\-new\\s\\-\\-ai\\s(.+)";
        while(true) {
            String input = Utility.getNextLine();
            if(Utility.getCommandMatcher(input , regexMenuExit).matches()) {
                return ;
            } else if(Utility.getCommandMatcher(input , regexShowCurrentMenu).matches()) {
                Printer.prompt("Duel Menu");
            } else if(Utility.getCommandMatcher(input , regexShowCard).matches()) {
                showCard(Utility.getCommandMatcher(input , regexShowCard));
            } else if(Utility.getCommandMatcher(input , regexDuelOtherPlayer).matches()) {
                duelOtherPlayer(Utility.getCommandMatcher(input , regexDuelOtherPlayer));
            } else if(Utility.getCommandMatcher(input , regexDuelAI).matches()) {
                duelAI(Utility.getCommandMatcher(input , regexDuelAI));
            } else {
                Printer.prompt("Invalid Command!");
            }
        }
    }
}
