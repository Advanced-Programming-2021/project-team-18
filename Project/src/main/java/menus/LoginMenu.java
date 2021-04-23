package menus;

import data.Printer;
import game.User;
import utility.Utility;

import java.util.HashMap;

// TODO : Kamyar
public class LoginMenu extends Menu {

    public LoginMenu() {

    }

    public void create(HashMap<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String nickname = map.get("nickname");
        if (User.getUserByUsername(username) != null) {
            Printer.prompt("user with username " + username + " already exists");
            return;
        }
        if (User.isNicknameTaken(nickname)) {
            Printer.prompt("user with nickname " + nickname + " already exists");
            return;
        }
        Printer.prompt("user created successfully!");
        User user = new User(username, password, nickname);
    }

    public User login(HashMap<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        User user = User.getUserByUsername(username);
        if (user == null) {
            Printer.prompt("Username and password didn’t match!");
            return null;
        }
        if (!user.isPasswordCorrect(password)) {
            Printer.prompt("Username and password didn’t match!");
            return null;
        }
        Printer.prompt("user logged in successfully!");
        return user;
    }

    public void showMenu() {
        Printer.prompt("Login Menu");
    }

    public void exit() {
        System.exit(0);
    }

    public void runMenu() {
        String loginPattern = "user\\slogin(\\s--\\w+\\s\\w+){2}";
        String registerPattern = "user\\screate(\\s--\\w+\\s\\w+){3}";
        String showMenuPattern = "menu show-current";
        String menuNavigation = "menu\\senter\\s(\\w+)";
        while (true) {
            String input = Utility.getNextLine();
            if (input.matches(loginPattern)) {
                HashMap<String, String> map = Utility.getCommand(input);
                String[] mustAttributes = {"username", "password"};
                if (!Utility.isCommandValid(map, mustAttributes, null)) {
                    Printer.prompt("Invalid command!");
                    return;
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
                    return;
                }
                create(map);
            } else if (input.matches(showMenuPattern)) {
                showMenu();
            } else if (input.matches(menuNavigation)) {
                Printer.prompt("please login first");
                return;
            } else {
                Printer.prompt("Invalid command!");
            }

        }
    }
}
