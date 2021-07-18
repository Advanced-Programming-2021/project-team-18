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
        String username = map.get("username");
        String password = map.get("password");
        if (!MenuController.getInstance().isLoginValid(username, password)) {
//            Printer.prompt("Username and password didn't match!");
            return null;
        }
//        Printer.prompt("user logged in successfully!");
        return User.getUserByUsername(username);
    }


    public void runMenu() {

    }
}
