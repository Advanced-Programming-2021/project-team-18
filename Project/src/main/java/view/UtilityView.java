package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UtilityView {
    static String answer;
    public static void displayMessage(String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(200);
        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("ok");
        closeButton.setOnAction(e -> stage.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label , closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public static String obtainInformation(String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(200);
        Label label = new Label();
        label.setText(message);
        TextField textField = new TextField();
        Button closeButton = new Button("submit");
        closeButton.setOnAction(e -> { answer = textField.getText(); stage.close(); });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label , textField , closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();
        return answer;
    }

    public static void showError(String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
