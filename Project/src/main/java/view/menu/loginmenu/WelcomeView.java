package view.menu.loginmenu;

import javafx.scene.input.MouseEvent;
import view.View;

import java.io.IOException;

public class WelcomeView extends View {

    public void register(MouseEvent mouseEvent) throws IOException {
        loadView("register");
    }

    public void login(MouseEvent mouseEvent) throws IOException {
        loadView("login");
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
