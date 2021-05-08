package menus;

import data.Printer;
import game.User;
import utility.Utility;

import java.util.regex.Matcher;

// by Pasha
public class MainMenu extends Menu {
    private final User user;

    public MainMenu(User user) {
        this.user = user;
    }

    public void menuEnter(Matcher matcher) {
        String menuName = matcher.group(1).toLowerCase();
        if (menuName.equals("duel menu") || menuName.equals("duel")) {
            new DuelMenu(user).runMenu();
        } else if (menuName.equals("deck menu") || menuName.equals("deck")) {
            new DeckMenu(user).runMenu();
        } else if (menuName.equals("scoreboard menu") || menuName.equals("scoreboard")) {
            new ScoreboardMenu(user).runMenu();
        } else if (menuName.equals("profile menu") || menuName.equals("profile")) {
            new ProfileMenu(user).runMenu();
        } else if (menuName.equals("shop menu") || menuName.equals("shop")) {
            new ShopMenu(user).runMenu();
        } else if (menuName.equals("import/export menu") || menuName.equals("import/export")) {
            new ImportExportMenu(user).runMenu();
        } else {
            Printer.prompt(NAVIGATION_DENIED);
        }
    }

    @Override
    public void runMenu() {
        String regexMenuEnter = "menu\\senter\\s(\\w+)";
        String regexUserLogout = "user\\slogout";
        Matcher matcher;
        while (true) {
            String input = Utility.getNextLine();
            if ((matcher = Utility.getCommandMatcher(input, regexMenuEnter)).matches()) {
                menuEnter(matcher);
            } else if (Utility.getCommandMatcher(input, MENU_EXIT).matches()) {
                return;
            } else if (Utility.getCommandMatcher(input, SHOW_MENU).matches()) {
                Printer.prompt("Main Menu");
            } else if (Utility.getCommandMatcher(input, regexUserLogout).matches()) {
                Printer.prompt("user logged out successfully!");
                return;
            } else {
                Printer.prompt(INVALID_COMMAND);
            }
        }
    }
}
