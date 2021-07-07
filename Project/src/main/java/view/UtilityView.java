package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

public class UtilityView {
    static String answer;
    static int avatarNumbers;
    private static MediaPlayer player;

    static {
        avatarNumbers = 0;
        player = null;
        try {
            File avatarFolder = new File(Objects.requireNonNull(UtilityView.class.getResource(
                    "/avatars/")).toURI());
            avatarNumbers = Objects.requireNonNull(avatarFolder.listFiles()).length;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Image getAvatarImage(int avatarID) {
        try {
            return new Image(Objects.requireNonNull(UtilityView.class.getResource(
                    "/avatars/" + avatarID + ".jpg")).toExternalForm());
        } catch (Exception ignored) {
            return new Image(Objects.requireNonNull(UtilityView.class.getResource(
                    "/avatars/" + 1 + ".jpg")).toExternalForm());
        }
    }

    public static int getAvatarNumbers() {
        return avatarNumbers;
    }

    public static void displayMessage(String message) {
        Popup popup = new Popup();
        Label label = new Label();
        label.setMinWidth(200);
        label.setText(message);
        Button closeButton = new Button("Okay");
        closeButton.setOnAction(e -> popup.hide());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.getStylesheets().add(Objects.requireNonNull(UtilityView.class.getResource(
                "CSS/styles.css")).toExternalForm());
        closeButton.getStyleClass().add("normal-button");
        layout.getStyleClass().add("popup-container");
        popup.getContent().add(layout);
        popup.show(View.stage);
    }

//    public static String obtainInformation(String message) {
//        Popup popup = new Popup();
//        Label label = new Label();
//        label.setMinWidth(200);
//        label.setText(message);
//        TextField textField = new TextField();
//        Button closeButton = new Button("submit");
//        closeButton.setOnAction(e -> {
//            answer = textField.getText();
//            popup.hide();
//        });
//        VBox layout = new VBox(10);
//        layout.getChildren().addAll(label, textField, closeButton);
//        layout.setAlignment(Pos.CENTER);
//        layout.getStylesheets().add(Objects.requireNonNull(UtilityView.class.getResource(
//                "CSS/styles.css")).toExternalForm());
//        layout.getStyleClass().add("popup-container");
//        closeButton.getStyleClass().add("normal-button");
//        popup.getContent().add(layout);
//        popup.show(View.stage);
//
//        return answer;
//    }

    public static String obtainInformation(String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(200);
        Label label = new Label();
        label.setText(message);
        TextField textField = new TextField();
        Button closeButton = new Button("submit");
        closeButton.setOnAction(e -> {
            answer = textField.getText();
            stage.close();
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, textField, closeButton);
        layout.getStylesheets().add(Objects.requireNonNull(UtilityView.class.getResource(
                "CSS/styles.css")).toExternalForm());
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
        return answer;
    }

    public static void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    public static void playSound(String songName) {
        System.out.println("Playing " + songName + "...");
        Media media = new Media(Objects.requireNonNull(UtilityView.class.getResource(
                "/sounds/" + songName)).toString());
        if (player != null) player.stop();
        player = new MediaPlayer(media);
        // player.setCycleCount(-1);
        player.play();
    }

    public static void stopPlayer() {
        player.stop();
    }
}
