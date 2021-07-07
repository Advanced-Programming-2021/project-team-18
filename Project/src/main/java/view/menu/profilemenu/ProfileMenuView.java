package view.menu.profilemenu;

import game.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import menus.MenuController;
import menus.ProfileResult;
import view.View;

import java.io.File;
import java.io.IOException;

public class ProfileMenuView extends View {

    public PasswordField oldPassword;
    public PasswordField newPassword;
    public Label response;
    public Label username;
    public Label nickname;
    public TextField nicknameInput;
    public ImageView avatarImage;
    public VBox buttons;
    public GridPane userInfo;

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
            avatarImage.setImage(controller.getUser().getAvatar());
            avatarImage.fitWidthProperty().bind(stage.heightProperty().multiply(.4));
            avatarImage.setPreserveRatio(true);
        } catch (Exception ignored) {
        }
    }

    public void changePassword() {
        response.setTextFill(Color.DARKRED);
        MenuController controller = MenuController.getInstance();
        ProfileResult result = controller.changePassword(oldPassword.getText(), newPassword.getText());
        switch (result) {
            case INVALID_PASSWORD:
                response.setText("Password is invalid!");
                break;
            case PASSWORD_THE_SAME:
                response.setText("Please input a DIFFERENT password!");
                break;
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

    public void backToMain() throws IOException {
        loadView("main_menu");
    }

    public void changeNickname() throws IOException {
        MenuController.getInstance().getUser().setNickname(nicknameInput.getText());
        backToProfileMenu();
    }


    public void backToProfileMenu() throws IOException {
        loadView("profile_menu");
    }

    public void chooseAFile() {
        Stage tempStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select You Avatar...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(tempStage);
        if (file != null) {
            User user = MenuController.getInstance().getUser();
            try {
                user.updateAvatar(file);
                user.setAvatarID(-1);
                avatarImage.setImage(user.getAvatar());
            } catch (Exception e) {
                System.out.println("Problem occurred in process ...");
                e.printStackTrace();
            }
        }
        else {
            System.out.println("FILE IS NULL!");
        }
        tempStage.close();
    }
}
