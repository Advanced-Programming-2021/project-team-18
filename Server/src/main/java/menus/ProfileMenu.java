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

    public ProfileMenu(User user) {
        this.user = user;
        MenuController.getInstance().setUser(user);
    }


    private void changePassword(HashMap<String, String> commandMap) {
        switch (MenuController.getInstance()
                .changePassword(commandMap.get("current"), commandMap.get("new"))) {
            case INVALID_PASSWORD:
                Printer.prompt("invalid password!");
                return;
            case PASSWORD_THE_SAME:
                Printer.prompt("please enter a NEW  password :)");
                return;
            case SUCCESSFUL_OPERATION:
                Printer.prompt("password changed successfully!");
        }
    }

    private void changeNickname(HashMap<String, String> commandMap) {
        switch (MenuController.getInstance()
                .changeNickname(commandMap.get("nickname"))) {
            case NICKNAME_TAKEN:
                Printer.prompt("nickname is taken!");
                return;
            case SUCCESSFUL_OPERATION:
                Printer.prompt("nickname changed successfully!");
        }
    }

    // Note: This function WILL NOT WORK until the function "getCommand" is re-implemented
    // Command processing should be improved. Also the function "isCommandValid" should accept null HashMaps
    public void runMenu() {
        String newLine = Utility.getNextLine();
        HashMap<String, String> commandMap = Utility.getCommand(newLine);
        while (!newLine.equals(MENU_EXIT)) {
            if (newLine.equals(SHOW_MENU)) {
                Printer.prompt(NAME);
            } else if (newLine.matches("menu enter .*")) {
                Printer.prompt(NAVIGATION_DENIED);
            } else if (newLine.matches("profile change --password .*")) {
                if (Utility.isCommandValid(commandMap, mustInputs.get("changePassword"), mayInputs.get("changePassword"))) {
                    assert commandMap != null;
                    changePassword(commandMap);
                } else Printer.prompt(INVALID_COMMAND);
            } else if (newLine.matches("profile change --nickname .*")) {
                if (Utility.isCommandValid(commandMap, mustInputs.get("changeNickname"), mayInputs.get("changeNickname"))) {
                    assert commandMap != null;
                    changeNickname(commandMap);
                } else Printer.prompt(INVALID_COMMAND);
            } else Printer.prompt(INVALID_COMMAND);
            newLine = Utility.getNextLine();
            commandMap = Utility.getCommand(newLine);
        }
    }
}
