package view.menu.loginmenu;

import game.User;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import menus.MenuController;
import view.UtilityView;
import view.View;
import view.menu.mainmenu.MainMenuView;

import java.io.IOException;

public class LoginView extends View {
    public TextField usernameField;
    public PasswordField passwordField;

    public void login() throws IOException {
        boolean result = MenuController.getInstance().isLoginValid(usernameField.getText(), passwordField.getText());
        if (!result) {
            UtilityView.showError("username and password didn't match!");
            loadView("login");
        } else {
            MainMenuView.setCurrentUser(User.getUserByUsername(usernameField.getText()));
            loadView("main_menu");
        }
    }

    public void back() throws IOException {
        loadView("welcome");
    }
}