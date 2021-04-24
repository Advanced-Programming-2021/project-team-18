package menus;

import data.Printer;
import game.User;
import utility.Utility;

import java.util.regex.Matcher;

// TODO : PASHA : DONE
public class MainMenu extends Menu {
    private User user;

    public MainMenu(User user) {
        this.user = user;
    }

    public void menuEnter(Matcher matcher) {
        matcher.matches();
        String menuName = matcher.group(1).toLowerCase();
        if(menuName.equals("duel menu") || menuName.equals("duel")) {
            new DuelMenu(user).runMenu();
        } else if(menuName.equals("deck menu") || menuName.equals("deck")) {
            new DeckMenu(user).runMenu();
        } else if(menuName.equals("scoreboard menu") || menuName.equals("scoreboard")) {
            new ScoreboardMenu(user).runMenu();
        } else if(menuName.equals("profile menu") || menuName.equals("profile")) {
            new ProfileMenu(user).runMenu();
        } else if(menuName.equals("shop menu") || menuName.equals("shop")) {
            new ShopMenu(user).runMenu();
        } else if(menuName.equals("import/export menu") || menuName.equals("import/export")) {
            new ImportExportMenu(user).runMenu();
        } else {
            Printer.prompt("menu navigation not possible");
        }
    }

    @Override
    public void runMenu() {
        String regexMenuEnter = "menu\\senter\\s(\\w+)";
        String regexMenuExit = "menu\\sexit";
        String regexShowCurrentMenu = "menu\\sshow-current";
        String regexUserLogout = "user\\slogout";
        while(true) {
            String input = Utility.getNextLine();
            if(Utility.getCommandMatcher(input , regexMenuEnter).matches()) {
                menuEnter(Utility.getCommandMatcher(input , regexMenuEnter));
            } else if(Utility.getCommandMatcher(input , regexMenuExit).matches()) {
                return ;
            } else if(Utility.getCommandMatcher(input , regexShowCurrentMenu).matches()) {
                Printer.prompt("Main Menu");
            } else if(Utility.getCommandMatcher(input , regexUserLogout).matches()) {
                Printer.prompt("user logged out successfully!");
                return ;
            } else {
                Printer.prompt("Invalid command!");
            }
        }
    }
}
