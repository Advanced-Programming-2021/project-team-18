package view;

import game.User;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URI;
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
            File arbitraryFolder = new File(new URI(UtilityView.class.getResource(
                    "/avatars") + "/arbitrary/"));
            //noinspection ResultOfMethodCallIgnored
            arbitraryFolder.mkdirs();
            File avatarFolder = new File(Objects.requireNonNull(UtilityView.class.getResource(
                    "/avatars/")).toURI());
            for (File file : Objects.requireNonNull(avatarFolder.listFiles())) {
                if (file.isFile() && file.getName().matches("[1-9]\\d*.jpg"))
                    avatarNumbers++;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Image getAvatarImage(int avatarID) {
        if (avatarID > avatarNumbers || avatarID < 0) return null;
        try {
            return new Image(Objects.requireNonNull(UtilityView.class.getResource(
                    "/avatars/" + avatarID + ".jpg")).toExternalForm());
        } catch (Exception ignored) {
            System.out.println("Some problems occurred during opening avatar!");
            return new Image(Objects.requireNonNull(UtilityView.class.getResource(
                    "/avatars/" + 1 + ".jpg")).toExternalForm());
        }
    }

    public static int getAvatarNumbers() {
        return avatarNumbers;
    }

    public static void displayMessage(String message, User audience) {
        audience.setDuelMessage(message);
    }

    public static String obtainInformation(String message) {
//        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setMinWidth(200);
//        Label label = new Label();
//        label.setText(message);
//        TextField textField = new TextField();
//        Button closeButton = new Button("submit");
//        closeButton.setOnAction(e -> {
//            answer = textField.getText();
//            stage.close();
//        });
//        VBox layout = new VBox(10);
//        layout.getChildren().addAll(label, textField, closeButton);
//        layout.getStylesheets().add(Objects.requireNonNull(UtilityView.class.getResource(
//                "CSS/styles.css")).toExternalForm());
//        layout.setAlignment(Pos.CENTER);
//        Scene scene = new Scene(layout);
//        stage.setScene(scene);
//        stage.showAndWait();
//        return answer;
        return null;
    }

    public static String obtainInformationInCertainWay(String message, String regex) {
//        String input;
//        while (true) {
//            input = obtainInformation(message);
//            if (!input.matches(regex)) showError("your input didn't match the format try again!");
//            else break;
//        }
//        return input;
        return null;
    }

    @SneakyThrows
    public static String obtainInformationInList(String message, String[] options) {
//        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
//        FXMLLoader loader = new FXMLLoader(UtilityView.class.getResource("/view/FXML/" + "obtain_information_list" + ".fxml"));
//        Parent root = loader.load();
//        ObtainInformationListController controller = loader.getController();
//        for (String option : options)
//            controller.getListView().getItems().add(option);
//        controller.setResult(controller.getListView().getSelectionModel().getSelectedItems().get(0).toString());
//        controller.getLabel().setText(message);
//        controller.getListView().getSelectionModel().select(0);
//        controller.getSelectButton().setOnMouseClicked(event -> {
//            String text = controller.getListView().getSelectionModel().getSelectedItems().get(0).toString();
//            controller.setResult(text);
//            stage.close();
//        });
//        stage.setScene(new Scene(root));
//        stage.showAndWait();
//        return controller.getResult();
        return null;
    }

    public static void showError(User audience, String message) {
        audience.setDuelMessage(message);
    }

    public static void playSound(String songName) {
//        System.out.println("Playing " + songName + "...");
//        Media media = new Media(Objects.requireNonNull(UtilityView.class.getResource(
//                "/sounds/" + songName)).toString());
//        if (player != null) player.stop();
//        player = new MediaPlayer(media);
//        player.setCycleCount(-1);
//        player.play();
    }

    public static void playSoundWithoutCycle(String songName) {
//        System.out.println("Playing " + songName + "...");
//        Media media = new Media(Objects.requireNonNull(UtilityView.class.getResource(
//                "/sounds/" + songName)).toString());
//        MediaPlayer currentPlayer = new MediaPlayer(media);
//        currentPlayer.setCycleCount(1);
//        currentPlayer.play();
    }

    public static void stopPlayer() {
        player.stop();
    }
}
