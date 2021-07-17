package data.api.loginmenu;

import data.DataManager;
import game.User;
import org.springframework.web.bind.annotation.*;
import utility.Utility;

import java.util.HashMap;

@RestController
public class RegisterController {
    @RequestMapping(path = "api/loginmenu/register", method = RequestMethod.GET)
    @GetMapping
    public String getRegisterResult(@RequestHeader(value = "username") String username, @RequestHeader(value = "password") String password, @RequestHeader(value = "nickname") String nickname) {
        User user = User.getUserByUsername(username);
        //System.out.println("i was invoked " + username + " " + password + " " + nickname);
        HashMap<String, String> result = new HashMap<>();
        if (username.isBlank() || password.isBlank() || nickname.isBlank()){
            result.put("verdict", "none of the fields can be blank");
            return Utility.getJson(result);
        }
        if (user != null) {
            result.put("verdict", "a user with this username already exists");
            return Utility.getJson(result);
        }
        if (User.isNicknameTaken(nickname)){
            result.put("verdict", "a user with this nickname already exists");
            return Utility.getJson(result);
        }
        user = new User(username, password, nickname);
        result = new HashMap<>();
        result.put("verdict", "Success");
        user.setToken(Utility.generateNewToken());
        result.put("token", user.getToken());
        DataManager.saveUsersData();
        return Utility.getJson(result);
    }
}
