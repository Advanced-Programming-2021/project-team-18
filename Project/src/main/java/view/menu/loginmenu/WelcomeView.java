package view.menu.loginmenu;

import javafx.scene.input.MouseEvent;
import view.View;

import java.io.IOException;

public class WelcomeView extends View {

    public void register() throws IOException {
        loadView("register");
    }

    public void login() throws IOException {
        loadView("login");
    }

    public void exit() {
        stage.close();
    }
}
