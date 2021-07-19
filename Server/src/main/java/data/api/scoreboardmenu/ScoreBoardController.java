package data.api.scoreboardmenu;

import game.User;
import org.springframework.web.bind.annotation.*;
import utility.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
@RestController
public class ScoreBoardController {
    @RequestMapping(value = "api/scoreboardmenu/scoreboard", method = RequestMethod.GET)
    @GetMapping
    public String getScoreBoard(@RequestHeader(value = "token") String token){
        User user = User.getUserByToken(token);
        HashMap<String,String> result = new HashMap<>();
        if (user == null){
            result.put("verdict","invalid token");
            return Utility.getJson(result);
        }
        ArrayList<User> users = new ArrayList<>(User.getAllUsers());
        Collections.sort(users);
        int size = Math.min(users.size(),20);
        ArrayList<SimplifiedUser> scoreboard = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            scoreboard.add(SimplifiedUser.constructFromUser(users.get(i)));
        }
        result.put("verdict","success");
        result.put("scoreboard",Utility.getJson(scoreboard));
        return Utility.getJson(result);
    }
}
