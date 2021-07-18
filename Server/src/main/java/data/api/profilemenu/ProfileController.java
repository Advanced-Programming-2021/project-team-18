package data.api.profilemenu;

import game.User;
import org.springframework.web.bind.annotation.*;
import utility.Utility;

import java.util.HashMap;

@RestController
public class ProfileController {
    @RequestMapping(path = "api/profilemenu/change_password" , method = RequestMethod.GET)
    @GetMapping
    public String changePassword(@RequestHeader (value = "token") String token,
                                 @RequestHeader (value = "current_password") String currentPass,
                                 @RequestHeader (value = "new_password") String newPass) {
        User user = User.getUserByToken(token);
        if (user == null) return null;
        if (user.getPassword() != currentPass) return "INVALID_PASSWORD";
        else if (newPass.isBlank()) return "BLANK_PASSWORD";
        else if (newPass.equals(user.getPassword())) return "PASSWORD_THE_SAME";
        user.setPassword(newPass);
        return "SUCCESSFUL_OPERATION";
    }
    @RequestMapping(path = "api/profilemenu/change_nickname" , method = RequestMethod.GET)
    @GetMapping
    public String changeNickname(@RequestHeader (value = "token") String token,
                                 @RequestHeader (value = "new_nickname") String newNickname) {
        User userByToken = User.getUserByToken(token);
        if (userByToken == null) return null;
        for (User user : User.getAllUsers()) {
            if (user.equals(userByToken)) continue;
            if (user.getNickname().equals(newNickname)) return "NICKNAME_TAKEN";
        }
        userByToken.setNickname(newNickname);
        return "SUCCESSFUL_OPERATION";
    }
}
