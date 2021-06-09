package menus;

import game.User;

public class MenuController {
    private static MenuController instance = new MenuController();

    public static MenuController getInstance() {
        return instance;
    }

    public ProfileResult createNewUser(String username, String password, String nickname) {
        if (username.isBlank()) return ProfileResult.BLANK_USERNAME;
        if (User.getUserByUsername(username) != null)
            return ProfileResult.USERNAME_TAKEN;
        if (User.isNicknameTaken(nickname))
            return ProfileResult.NICKNAME_TAKEN;
        new User(username, password, nickname);
        return ProfileResult.SUCCESSFUL_OPERATION;
    }

    public boolean isLoginValid(String username, String password) {
        User user = User.getUserByUsername(username);
        if (user == null) return false;
        return user.isPasswordCorrect(password);
    }
}
