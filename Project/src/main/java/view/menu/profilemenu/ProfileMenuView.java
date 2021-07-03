package view.menu.profilemenu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import menus.MenuController;
import menus.ProfileResult;
import view.View;

import java.io.IOException;

public class ProfileMenuView extends View {

    public PasswordField oldPassword;
    public PasswordField newPassword;
    public Label response;
    public Label username;
    public Label nickname;
    public TextField nicknameInput;

    public void loadChangePasswordPage() throws IOException {
        loadView("change_password");
    }

    public void loadChangeNicknamePage() throws IOException {
        loadView("change_nickname");
    }
    
    @FXML
    public void initialize() {
        MenuController controller = MenuController.getInstance();
        try {
            username.setText(controller.getUser().getUsername());
            nickname.setText(controller.getUser().getNickname());
        } catch (Exception ignored) {
            System.out.println("Sorry, some problem occurred in initialization :(");
        }
    }

    public void changePassword() {
        response.setTextFill(Color.DARKRED);
        MenuController controller = MenuController.getInstance();
        ProfileResult result = controller.changePassword(oldPassword.getText(), newPassword.getText());
        switch (result) {
            case INVALID_PASSWORD:
                response.setText("Password is invalid!"); break;
            case PASSWORD_THE_SAME:
                response.setText("Please input a DIFFERENT password!"); break;
            case SUCCESSFUL_OPERATION:
                promptSuccessAndBack();
        }
    }

    public void promptSuccessAndBack() {
        response.setTextFill(Color.DARKGREEN);
        response.setText("Password changed successfully");
        System.out.println("Password changed!");
        Timeline timeline = new Timeline();
        Duration instant = Duration.ZERO;
        instant = instant.add(Duration.seconds(1));
        timeline.getKeyFrames().add(new KeyFrame(instant, actionEvent -> {
            try {
                loadView("profile_menu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        timeline.play();
    }

    public void changeNickname() throws IOException {
        MenuController.getInstance().getUser().setNickname(nicknameInput.getText());
        loadView("profile_menu");
    }
}
