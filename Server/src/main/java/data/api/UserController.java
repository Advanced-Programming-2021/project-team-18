package data.api;

import game.User;
import org.springframework.web.bind.annotation.*;
import utility.Utility;

import java.util.List;

@RestController
public class UserController {

    @RequestMapping(path = "api/user" , method = RequestMethod.GET)
    @GetMapping
    public String getUser(@RequestParam(value = "username") String username) {
        User user = User.getUserByUsername(username);
        if(user == null) return "username not found";
        return Utility.getJson(user);
    }

}
