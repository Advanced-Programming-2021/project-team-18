package data.api.loginmenu;

import game.User;
import org.springframework.web.bind.annotation.*;
import utility.Utility;

import java.util.HashMap;

@RestController
public class LoginController {

    @RequestMapping(path = "api/loginmenu/login" , method = RequestMethod.GET)
    @GetMapping
    public String getUserToken(@RequestHeader(value = "username") String username , @RequestHeader(value = "password") String password) {
        User user = User.getUserByUsername(username);
        HashMap<String ,String > response = new HashMap<>();
        if(user == null) {
            response.put("verdict" , "username not found");
            return Utility.getJson(response);
        }
        if(!user.getPassword().equals(password)) {
            response.put("verdict" , "incorrect password");
            return Utility.getJson(response);
        }
        response.put("verdict" , "success");
        user.setToken(Utility.generateNewToken());
        response.put("token" , user.getToken());
        return Utility.getJson(response);
    }


}
