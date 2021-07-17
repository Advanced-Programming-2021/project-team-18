package menus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.User;
import lombok.Getter;
import lombok.Setter;
import utility.Utility;
import view.UtilityView;

import java.util.HashMap;

public class MenuController {
    private static final String GET_USER_LOCATION = "/api/loginmenu/login";
    private static final String REGISTER_LOCATION = "/api/loginmenu/register";
    private static MenuController instance = new MenuController();
    @Setter
    @Getter
    private String token;

    public static MenuController getInstance() {
        return instance;
    }

    public String createNewUser(String username, String password, String nickname) {
        if (username.isBlank()) return null;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("username", username);
        headers.put("nickname", nickname);
        headers.put("password", password);
        HashMap<String, String> result = new Gson().fromJson(Utility.getRequest(Utility.getSERVER_LOCATION() + REGISTER_LOCATION, null, headers), new TypeToken<HashMap<String, String>>() {
        }.getType());
        //System.out.println(result.toString());
        if (result.get("verdict").contentEquals("Success")) return result.get("token");
        UtilityView.showError(result.get("verdict"));
        return null;
    }

    public String getLoginToken(String username, String password) { // returns null if username doesn't match with password
        HashMap<String, String> headers = new HashMap<>();
        headers.put("username", username);
        headers.put("password", password);
        HashMap<String, String> response = new Gson().fromJson(Utility.getRequest(Utility.getSERVER_LOCATION() + GET_USER_LOCATION, null, headers), new TypeToken<HashMap<String, String>>() {
        }.getType());
        System.out.println(response);
        if (response.get("verdict").equals("incorrect password") || response.get("verdict").equals("username not found"))
            return null;
        return response.get("token");
    }

    public ProfileResult changePassword(String currentPassword, String newPassword) {
        // todo server
//        if (!user.isPasswordCorrect(currentPassword))
//            return ProfileResult.INVALID_PASSWORD;
//        if (currentPassword.equals(newPassword))
//            return ProfileResult.PASSWORD_THE_SAME;
//        user.setPassword(newPassword);
//        return ProfileResult.SUCCESSFUL_OPERATION;
        return null;
    }

    public ProfileResult changeNickname(String newNickname) {
        // todo server
//        if (User.isNicknameTaken(newNickname)) return ProfileResult.NICKNAME_TAKEN;
//        user.setNickname(newNickname);
//        return ProfileResult.SUCCESSFUL_OPERATION;
        return null;
    }
}
