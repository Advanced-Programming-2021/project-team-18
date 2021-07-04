package view.menu.loginmenu;

import game.User;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
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
        ProfileResult result = MenuController.getInstance().createNewUser(usernameInput.getText(),
                passwordInput.getText(), nicknameInput.getText());
        response.setTextFill(Color.INDIANRED);
        switch (result) {
            case BLANK_USERNAME:
                response.setText("Username is blank!");
                return;
            case USERNAME_TAKEN:
                response.setText("Username is taken!");
                return;
            case NICKNAME_TAKEN:
                response.setText("Nickname is taken!");
                return;
            case SUCCESSFUL_OPERATION:
                response.setTextFill(Color.DARKGREEN);
                response.setText("Registered successfully!");
                MenuController.getInstance().setUser(User.getUserByUsername(usernameInput.getText()));
                // TODO: CHOOSE PROFILE PHOTO
        }
        if (result == ProfileResult.SUCCESSFUL_OPERATION)
            MainMenuView.setCurrentUser(MenuController.getInstance().getUser());
        loadView("main_menu");
    }
}
