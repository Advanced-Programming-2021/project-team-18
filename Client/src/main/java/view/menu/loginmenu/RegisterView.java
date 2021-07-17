package view.menu.loginmenu;

import game.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import menus.MenuController;
import menus.ProfileResult;
import view.View;
import view.menu.mainmenu.MainMenuView;

import java.io.IOException;

public class RegisterView extends View {

    public TextField usernameInput;
    public TextField nicknameInput;
    public PasswordField passwordInput;
    public Label response;

    public void register() throws IOException {
        String result = MenuController.getInstance().createNewUser(usernameInput.getText(),
                passwordInput.getText(), nicknameInput.getText());
        //response.setTextFill(Color.INDIANRED);
        if (result != null) {
                response.setTextFill(Color.DARKGREEN);
                response.setText("Registered successfully!");
            MainMenuView.setCurrentToken(result);
            MenuController.getInstance().setToken(result);
            loadView("main_menu");
        }
    }

    public void backToWelcome() throws IOException {
        loadView("welcome");
    }
}
