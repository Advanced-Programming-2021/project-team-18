package view.menu.loginmenu;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import menus.MenuController;
import view.UtilityView;
import view.View;

import java.io.IOException;

public class LoginView extends View {
    public TextField usernameField;
    public TextField passwordField;

    public void login(MouseEvent mouseEvent) throws IOException {
        boolean result = MenuController.getInstance().isLoginValid(usernameField.getText(), passwordField.getText());
        if(!result){
            UtilityView.showError("username and password didn't match!");
            loadView("/view/FXML/loginMenu.fxml");
        }
        else{
            loadView("/view/FXML/mainMenu.fxml");
        }
    }
}