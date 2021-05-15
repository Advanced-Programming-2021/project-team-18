package menus;

import card.Card;
import data.Printer;
import game.Game;
import game.User;
import utility.Utility;

import java.util.HashMap;
import java.util.regex.Matcher;

// by Pasha
public class DuelMenu extends Menu {
    User user;

    public DuelMenu(User user) {
        this.user = user;
    }

    public void showCard(Matcher matcher) {
        Printer.showCard(Card.getCardByName(matcher.group(1)));
    }

    public void duelOtherPlayer(Matcher matcher) {
        HashMap<String, String> map = Utility.getCommand(matcher.group(1));
        String[] mustAttributes = {"second-player", "rounds"};
        if (!Utility.isCommandValid(map, mustAttributes, null)) {
            Printer.prompt(INVALID_COMMAND);
            return;
        }
        String secondPlayerName = map.get("second-player");
        int roundsCount = Integer.parseInt(map.get("rounds"));
        if (User.getUserByUsername(secondPlayerName) == null) {
            Printer.prompt("there is no player with this username");
            return;
        }
        if (user.getActiveDeckName() == null) {
            Printer.prompt(user.getUsername() + "  has no active deck");
            return;
        }
        if (User.getUserByUsername(secondPlayerName).getActiveDeckName() == null) {
            Printer.prompt(User.getUserByUsername(secondPlayerName).getUsername() + "  has no active deck");
            return;
        }
        if (!user.getGameDeckByName(user.getActiveDeckName()).isDeckValid()) {
            Printer.prompt(user.getUsername() + "'s deck is invalid");
            return;
        }
        if (!User.getUserByUsername(secondPlayerName).getGameDeckByName(User.getUserByUsername(secondPlayerName).getActiveDeckName()).isDeckValid()) {
            Printer.prompt(User.getUserByUsername(secondPlayerName).getUsername() + "'s deck is invalid");
            return;
        }
        if (roundsCount != 1 && roundsCount != 3) {
            Printer.prompt("number of rounds is not supported");
            return;
        }
        (new Game(user, User.getUserByUsername(secondPlayerName), roundsCount)).runGame();
    }

    public void duelAI(Matcher matcher) {
        HashMap<String, String> map = Utility.getCommand(matcher.group(1));
        String[] mustAttributes = {"rounds"};
        if (!Utility.isCommandValid(map, mustAttributes, null)) {
            Printer.prompt("Invalid command!");
            return;
        }
        int roundsCount = Integer.parseInt(map.get("rounds"));
        if (user.getActiveDeckName() == null) {
            Printer.prompt(user.getUsername() + "  has no active deck");
            return;
        }
        if (!user.getGameDeckByName(user.getActiveDeckName()).isDeckValid()) {
            Printer.prompt(user.getUsername() + "'s deck is invalid");
            return;
        }
        if (roundsCount != 1 && roundsCount != 3) {
            Printer.prompt("number of rounds is not supported");
            return;
        }
        (new Game(user, null, roundsCount)).runGame();
    }

    @Override
    public void runMenu() {
        String regexShowCard = "card\\sshow\\s(.+)";
        String regexDuelOtherPlayer = "duel\\s\\-\\-new\\s(.+)";
        String regexDuelAI = "duel\\s\\-\\-new\\s\\-\\-ai\\s(.+)";
        Matcher matcher;
        while (true) {
            String input = Utility.getNextLine();
            if (Utility.getCommandMatcher(input, MENU_EXIT).matches()) {
                return;
            } else if (Utility.getCommandMatcher(input, SHOW_MENU).matches()) {
                Printer.prompt("Duel Menu");
            } else if ((matcher = Utility.getCommandMatcher(input, regexShowCard)).matches()) {
                showCard(matcher);
            } else if ((matcher = Utility.getCommandMatcher(input, regexDuelOtherPlayer)).matches()) {
                duelOtherPlayer(matcher);
            } else if ((matcher = Utility.getCommandMatcher(input, regexDuelAI)).matches()) {
                duelAI(matcher);
            } else {
                Printer.prompt(INVALID_COMMAND);
            }
        }
    }
}
