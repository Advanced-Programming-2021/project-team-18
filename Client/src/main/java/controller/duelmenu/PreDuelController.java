package controller.duelmenu;

import utility.Utility;

import java.util.HashMap;

public class PreDuelController {
    private static final String REQUEST_DUEL = "/api/duelmenu/pre_duel/request_duel";
    private static final String CHECK_REQUEST = "/api/duelmenu/pre_duel/check_request";
    private static final String ACCEPT_REQUEST = "/api/duelmenu/pre_duel/accept_request";
    private static final String RECEIVE_LAST_MESSAGE = "/api/duelmenu/pre_duel/receive_last_message";

    public static String requestDuel(String token, String username) {
        HashMap<String, String> headers = new HashMap<>() {{
            put("token", token);
            put("opponent", username);
        }};
        return Utility.postRequest(Utility.getSERVER_LOCATION() + REQUEST_DUEL, null, headers);
    }

    public static String checkRequest(String token) {
        HashMap<String, String> headers = new HashMap<>() {{
            put("token", token);
        }};
        return Utility.getRequest(Utility.getSERVER_LOCATION() + CHECK_REQUEST, null, headers);
    }

    public static String acceptRequest(String token) {
        HashMap<String, String> headers = new HashMap<>() {{
            put("token", token);
        }};
        return Utility.postRequest(Utility.getSERVER_LOCATION() + ACCEPT_REQUEST, null, headers);
    }

    public static String receiveLastMessage(String token) {
        HashMap<String, String> headers = new HashMap<>() {{
            put("token", token);
        }};
        return Utility.getRequest(Utility.getSERVER_LOCATION() + RECEIVE_LAST_MESSAGE, null, headers);
    }
}
