package view.menu.loginmenu;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import view.View;

import java.io.IOException;

public class WelcomeView extends View {

    public void register(ActionEvent actionEvent) throws IOException {
        loadView("register");
    }

    public void login(ActionEvent actionEvent) throws IOException {
        loadView("login");
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
