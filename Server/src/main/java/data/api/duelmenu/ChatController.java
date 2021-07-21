package data.api.duelmenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.User;
import org.springframework.web.bind.annotation.*;
import utility.Utility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class ChatController {
    private ArrayList<ChatMessage> chatMessages = new ArrayList<>();

    @RequestMapping(path = "api/duelmenu/getChat", method = RequestMethod.GET)
    @GetMapping
    public String getNewMessages(@RequestHeader(value = "token") String token, @RequestHeader(value = "index") String indexString) {
        User user = User.getUserByToken(token);
        HashMap<String, String> result = new HashMap<>();
        if (user == null) {
            result.put("verdict", "invalid token");
            return Utility.getJson(result);
        }
        int index = Integer.parseInt(indexString);
        result.put("verdict", "success");
        result.put("messages", Utility.getJson(newMessages(index)));
        result.put("onlineCount",String.valueOf(User.getOnlineCount()));
        return Utility.getJson(result);
    }

    private ArrayList<ChatMessage> newMessages(int index) {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        int counter = 0;
        for (ChatMessage message : chatMessages) {
            if (counter >= index) messages.add(message);
            counter++;
        }
        return messages;
    }

    @RequestMapping(path = "api/duelmenu/addMessage", method = RequestMethod.GET)
    @GetMapping
    public synchronized String addMessage(@RequestHeader(value = "token") String token, @RequestHeader(value = "message") String message) {
        User user = User.getUserByToken(token);
        HashMap<String, String> result = new HashMap<>();
        if (user == null) {
            result.put("verdict", "invalid token");
            return Utility.getJson(result);
        }
        ChatMessage chatMessage = new ChatMessage(message, user.getNickname());
        chatMessages.add(chatMessage);
        result.put("verdict", "success");
        return Utility.getJson(result);
    }
}
