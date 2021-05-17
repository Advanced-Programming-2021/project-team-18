import game.User;
import menus.LoginMenu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class LoginMenuTests {
    @Test
    public void userSignupTest() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", "sampleUsername");
        map.put("password", "samplePassword");
        map.put("nickname", "sampleNickname");
        boolean finalResult = false;
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.create(map);
        User user = User.getUserByUsername("sampleUsername");
        if (user != null && user.getPassword().equals("samplePassword") && user.getNickname().contentEquals("sampleNickname"))
            finalResult = true;
        User.removeUser("sampleUsername");
        if (User.getUserByUsername("sampleUsername") != null) finalResult = false;
        Assertions.assertEquals(finalResult, true);
    }

    @Test
    public void userLoginTest() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", "sampleUsername");
        map.put("password", "samplePassword");
        map.put("nickname", "sampleNickname");
        boolean finalResult = false;
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.create(map);
        HashMap<String, String> loginMap = new HashMap<String, String>();
        loginMap.put("username", "sampleUsername");
        loginMap.put("password", "samplePassword");
        User user = loginMenu.login(map);
        if(user != null && user.getUsername().contentEquals("sampleUsername") && user.getPassword().contentEquals("samplePassword")) finalResult = true;
        Assertions.assertEquals(finalResult , true);
    }
}
