package menus;

import data.Printer;
import game.User;
import utility.Utility;

import java.util.HashMap;

// by Kamyar
public class LoginMenu extends Menu {

    public void create(HashMap<String, String> map) {
//        String username = map.get("username");
//        String password = map.get("password");
//        String nickname = map.get("nickname");
//        switch (MenuController.getInstance().createNewUser(username, password, nickname)) {
//            case USERNAME_TAKEN:
//                Printer.prompt("user with username " + username + " already exists");
//                return;
//            case NICKNAME_TAKEN:
//                Printer.prompt("user with nickname " + nickname + " already exists");
//                return;
//            case SUCCESSFUL_OPERATION:
//                Printer.prompt("user created successfully!");
//        }
    }

    public User login(HashMap<String, String> map) {
//        String username = map.get("username");
//        String password = map.get("password");
//        if (!MenuController.getInstance().isLoginValid(username, password)) {
//            Printer.prompt("Username and password didn't match!");
//            return null;
//        }
//        Printer.prompt("user logged in successfully!");
//        return User.getUserByUsername(username);
        return null;
    }

    public void showMenu() {
        Printer.prompt("Login Menu");
    }


    public void runMenu() {
        String loginPattern = "user\\slogin(\\s--\\w+\\s\\w+){2}";
        String registerPattern = "user\\screate(\\s--\\w+\\s\\w+){3}";
        String showMenuPattern = "menu show-current";
        String menuNavigation = "menu\\senter\\s(\\w+)";
        String exitMenuPattern = "menu\\sexit";
        while (true) {
            String input = Utility.getNextLine();
            if (input.matches(loginPattern)) {
                HashMap<String, String> map = Utility.getCommand(input);
                String[] mustAttributes = {"username", "password"};
                if (!Utility.isCommandValid(map, mustAttributes, null)) {
                    Printer.prompt("Invalid command!");
                    continue;
                }
                User user = login(map);
                if (user != null) {
                    new MainMenu(user).runMenu();
                }
            } else if (input.matches(registerPattern)) {
                HashMap<String, String> map = Utility.getCommand(input);
                String[] mustAttributes = {"username", "password", "nickname"};
                if (!Utility.isCommandValid(map, mustAttributes, null)) {
                    Printer.prompt("Invalid command!");
                }
                create(map);
            } else if (input.matches(showMenuPattern)) {
                showMenu();
            } else if (input.matches(menuNavigation)) {
                Printer.prompt("please login first");
            } else if (input.matches(exitMenuPattern)) {
                return;
            } else {
                Printer.prompt("Invalid command!");
            }
        }
    }
}
