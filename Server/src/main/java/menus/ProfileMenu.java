package menus;

import data.Printer;
import game.User;
import utility.Utility;

import java.util.HashMap;

// By Sina
public class ProfileMenu extends Menu {
    User user;

    static {
        NAME = "Profile Menu";
        mustInputs.put("changePassword", new String[]{"current", "new"});
        mayInputs.put("changePassword", new String[]{"password"});
        mustInputs.put("changeNickname", new String[]{"nickname"});
        mayInputs.put("changeNickname", null);
    }
//
//    public ProfileMenu(User user) {
//        this.user = user;
//        MenuController.getInstance().setUser(user);
//    }
//
//
//    private void changePassword(HashMap<String, String> commandMap) {
//        switch (MenuController.getInstance()
//                .changePassword(commandMap.get("current"), commandMap.get("new"))) {
//            case INVALID_PASSWORD:
//                Printer.prompt("invalid password!");
//                return;
//            case PASSWORD_THE_SAME:
//                Printer.prompt("please enter a NEW  password :)");
//                return;
//            case SUCCESSFUL_OPERATION:
//                Printer.prompt("password changed successfully!");
//        }
//    }
//
//    private void changeNickname(HashMap<String, String> commandMap) {
//        switch (MenuController.getInstance()
//                .changeNickname(commandMap.get("nickname"))) {
//            case NICKNAME_TAKEN:
//                Printer.prompt("nickname is taken!");
//                return;
//            case SUCCESSFUL_OPERATION:
//                Printer.prompt("nickname changed successfully!");
//        }
//    }
//
//
    public void runMenu() {


    }
}
