package menus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.ChatMessage;
import lombok.Getter;
import lombok.Setter;
import utility.Utility;
import view.UtilityView;
import view.menu.duelmenu.ChatMenu;
import view.menu.scoreboard.SimplifiedUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuController {
    private static final MenuController instance = new MenuController();

    private static final String CHANGE_NICKNAME_LOC;
    private static final String GET_USER_LOCATION;
    private static final String REGISTER_LOCATION;
    private static final String CARD_BALANCE_LOC;
    private static final String CARD_FORBID_LOC = "/api/shopmenu/get_card_forbid";
    private static final String CHANGE_PASSWORD_LOC;
    private static final String SCOREBOARD_LOC;
    private static final String GETCHAT_LOC;
    private static final String ADD_MESSAGE_LOC;
    private static final Type resultType;


    @Setter
    @Getter
    private String token;

    static {
        CHANGE_NICKNAME_LOC = "/api/profilemenu/change_nickname";
        GET_USER_LOCATION = "/api/loginmenu/login";
        REGISTER_LOCATION = "/api/loginmenu/register";
        CARD_BALANCE_LOC = "/api/shopmenu/get_card_balance";
        CHANGE_PASSWORD_LOC = "/api/profilemenu/change_password";
        SCOREBOARD_LOC = "/api/scoreboardmenu/scoreboard";
        GETCHAT_LOC = "/api/duelmenu/getChat";
        ADD_MESSAGE_LOC = "/api/duelmenu/addMessage";

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
        if (result.get("verdict").contentEquals("success")) return result.get("token");
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

    public int getCardBalance(String cardName) {
        HashMap<String, String> headers = Utility.makeHashMap("token", token, "card_name", cardName);
        String result = Utility.getRequest(Utility.getSERVER_LOCATION() + CARD_BALANCE_LOC,
                null, headers);
        return Integer.parseInt(result);
    }

    public ArrayList<SimplifiedUser> getScoreboard() {
        String result = Utility.send(SCOREBOARD_LOC, "token", token);
        HashMap<String, String> response = new Gson().fromJson(result, resultType);
        //System.out.println(response.toString());
        assert response != null;
        if (response.get("verdict").contentEquals("success")) {
            return new Gson().fromJson(response.get("scoreboard"), new TypeToken<ArrayList<SimplifiedUser>>() {
            }.getType());
        }
        return null;
    }

    public ArrayList<ChatMessage> getNewMessages(int lastIndex) {
        String result = "vrtver";
        try {
             result = Utility.send(GETCHAT_LOC, "token", token, "index", String.valueOf(lastIndex));
        }catch (Exception e){
            e.printStackTrace();
        }
        HashMap<String, String> response = new Gson().fromJson(result, new TypeToken<HashMap<String, String>>() {
        }.getType());
        assert response != null;
        if (response.get("verdict").contentEquals("success")) {
            ChatMenu.setOnlineCount(Integer.parseInt(response.get("onlineCount")));
            return new Gson().fromJson(response.get("messages"), new TypeToken<ArrayList<ChatMessage>>() {
            }.getType());
        }
        return null;
    }

    public void sendMessage(String message) {
        String result = Utility.send(ADD_MESSAGE_LOC, "token", token, "message", message);
        HashMap<String, String> response = new Gson().fromJson(result, new TypeToken<HashMap<String, String>>() {
        }.getType());
        assert response != null;
        if (!response.get("verdict").contentEquals("success")) UtilityView.showError(response.get("verdict"));
    }

    public boolean getCardForbid(String cardName) {
        String result = Utility.send(CARD_FORBID_LOC, "token", token, "card_name", cardName);
        assert result != null;
        return result.equals("forbidden");
    }
}
