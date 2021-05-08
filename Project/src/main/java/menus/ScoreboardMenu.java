package menus;

import data.Printer;
import game.User;
import utility.Utility;


// by Kamyar
public class ScoreboardMenu extends Menu {

    public ScoreboardMenu(User user) {

    }

    public void showScoreboard() {
        Printer.showScoreBoard();
    }

    public void showCurrentMenu() {
        Printer.prompt("Scoreboard");
    }

    public void runMenu() {
        String showScoreBoardPattern = "scoreboard\\sshow";
        String currentMenuPattern = "menu\\sshow-current";
        String exitMenuPattern = "menu\\sexit";
        while (true) {
            String input = Utility.getNextLine();
            if (input.matches(showScoreBoardPattern)) showScoreboard();
            else if (input.matches(currentMenuPattern)) showCurrentMenu();
            else if (input.matches(exitMenuPattern)) return;
            else Printer.prompt("Invalid Command!");
        }
    }
}
