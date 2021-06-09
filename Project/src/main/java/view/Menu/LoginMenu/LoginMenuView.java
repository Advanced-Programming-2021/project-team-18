package view.Menu.LoginMenu;

import javafx.event.ActionEvent;
import view.View;

import java.io.IOException;

public class LoginMenuView extends View {

    public void register(ActionEvent actionEvent) throws IOException {
        loadView("/view/FXML/register.fxml");
    }

    public void login(ActionEvent actionEvent) {
    }
}
