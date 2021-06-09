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
    }

    public ProfileResult changePassword(String currentPassword, String newPassword) {
        if (!user.isPasswordCorrect(currentPassword))
            return ProfileResult.INVALID_PASSWORD;
        if (currentPassword.equals(newPassword))
            return ProfileResult.PASSWORD_THE_SAME;
        user.setPassword(newPassword);
        return ProfileResult.SUCCESSFUL_OPERATION;
    }

    public ProfileResult changeNickname(String newNickname) {
        if (User.isNicknameTaken(newNickname)) return ProfileResult.NICKNAME_TAKEN;
        user.setNickname(newNickname);
        return ProfileResult.SUCCESSFUL_OPERATION;
    }

    // Note: This function WILL NOT WORK until the function "getCommand" is re-implemented
    // Command processing should be improved. Also the function "isCommandValid" should accept null HashMaps

    /*
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

     */
    // TODO: Update runMenu
    public void runMenu() {

    }
}
