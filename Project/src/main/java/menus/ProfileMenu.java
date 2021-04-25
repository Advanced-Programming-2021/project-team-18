package menus;

import data.Printer;
import game.User;
import utility.Utility;

import java.util.HashMap;

// TODO : SINA
public class ProfileMenu extends Menu {
    User user;

    static {
        name = "Profile Menu";
        mustInputs.put("changePassword", new String[]{"current", "new"});
        mayInputs.put("changePassword", null);
        mustInputs.put("changeNickname", new String[]{"nickname"});
        mayInputs.put("changeNickname", null);
    }

    public ProfileMenu(User user) {
        this.user = user;
    }

    public void changePassword(HashMap<String, String> commandMap) {
        String currentPassword = commandMap.get("current");
        String newPassword = commandMap.get("new");
        if (!user.isPasswordCorrect(currentPassword)) {
            Printer.prompt("current password is invalid");
            return;
        }
        if (currentPassword.equals(newPassword)) {
            Printer.prompt("please enter a new password");
            return;
        }
        user.setPassword(newPassword);
        Printer.prompt("password changed successfully!");
    }

    public void changeNickname(HashMap<String, String> commandMap) {
        String newNickname = commandMap.get("nickname");
        if (User.isNicknameTaken(newNickname)) {
            Printer.prompt("user with nickname " + newNickname + " already exists");
            return;
        }
        user.setNickname(newNickname);
        Printer.prompt("nickname changed successfully! hello world!");
    }

    // Note: This function WILL NOT WORK until the function "getCommand" is re-implemented
    // Command processing should be improved. Also the function "isCommandValid" should accept null HashMaps
    public void runMenu() {
        String newLine = Utility.getNextLine();
        HashMap<String, String> commandMap = Utility.getCommand(newLine);
        while (!newLine.equals(MENU_EXIT)) {
            if (newLine.equals(SHOW_MENU)) {
                Printer.prompt(name);
            } else if (newLine.matches("menu enter .*")) {
                Printer.prompt(NAVIGATION_DENIED);
            } else if (newLine.matches("profile change --password .*")) {
                if (Utility.isCommandValid(commandMap, mustInputs.get("changePassword"), mayInputs.get("changePassword"))) {
                    assert commandMap != null;
                    changePassword(commandMap);
                }
                else Printer.prompt(INVALID_COMMAND);
            } else if (newLine.matches("profile change --nickname .*")) {
                if (Utility.isCommandValid(commandMap, mustInputs.get("changeNickname"), mayInputs.get("changeNickname"))) {
                    assert commandMap != null;
                    changeNickname(commandMap);
                }
                else Printer.prompt(INVALID_COMMAND);
            } else Printer.prompt(INVALID_COMMAND);
            newLine = Utility.getNextLine();
            commandMap = Utility.getCommand(newLine);
        }
    }
}
