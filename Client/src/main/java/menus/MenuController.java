package menus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.User;
import utility.Utility;

import java.util.HashMap;

public class MenuController {
    private static final String GET_USER_LOCATION = "/api/loginmenu/login";
    private static MenuController instance = new MenuController();

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

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
        HashMap<String , String> params = new HashMap<>();
        params.put("username" , username);
        params.put("password" , password);
        HashMap<String , String> response = new Gson().fromJson(Utility.getRequest(params, Utility.getSERVER_LOCATION() + GET_USER_LOCATION) , new TypeToken<HashMap<String , String>>() {}.getType());
        System.out.println(response.get("verdict"));
        if(response.get("verdict").equals("incorrect password") || response.get("verdict").equals("username not found"))
            return false;
        return true;
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
}
