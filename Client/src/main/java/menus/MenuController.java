package menus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import utility.Utility;
import view.UtilityView;

import java.lang.reflect.Type;
import java.util.HashMap;

public class MenuController {
    private static final MenuController instance = new MenuController();

    private static final String CHANGE_NICKNAME_LOC;
    private static final String GET_USER_LOCATION;
    private static final String REGISTER_LOCATION;
    private static final String CHANGE_PASSWORD_LOC;
    private static final Type resultType;

    @Setter
    @Getter
    private String token;

    static {
        CHANGE_NICKNAME_LOC = "/api/profilemenu/change_nickname";
        GET_USER_LOCATION = "/api/loginmenu/login";
        REGISTER_LOCATION = "/api/loginmenu/register";
        CHANGE_PASSWORD_LOC = "/api/profilemenu/change_password";

        resultType = new TypeToken<HashMap<String, String>>() {
        }.getType();
    }

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

    // returns null if username doesn't match with password
    public String getLoginToken(String username, String password) {
        HashMap<String, String> headers = Utility.makeHashMap("username", username, "password", password);
        HashMap<String, String> response = new Gson().fromJson(Utility.getRequest(
                Utility.getSERVER_LOCATION() + GET_USER_LOCATION, null, headers), resultType);
        System.out.println(response);
        if (response.get("verdict").equals("incorrect password") || response.get("verdict").equals("username not found"))
            return null;
        return response.get("token");
    }

    public ProfileResult changePassword(String currentPassword, String newPassword) {
        HashMap<String, String> headers = Utility.makeHashMap("token", token,
                "current_password", currentPassword,
                "new_password", newPassword);
        String result = Utility.getRequest(Utility.getSERVER_LOCATION()
                + CHANGE_PASSWORD_LOC, null, headers);
        return ProfileResult.valueOf(result);
    }

    public ProfileResult changeNickname(String newNickname) {
        HashMap<String, String> headers = Utility.makeHashMap("token", token,
                "new_nickname", newNickname);
        String result = Utility.getRequest(Utility.getSERVER_LOCATION()
        + CHANGE_NICKNAME_LOC, null, headers);
        return ProfileResult.valueOf(result);
    }
}
