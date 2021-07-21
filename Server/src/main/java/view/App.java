package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        View.setStage(stage);
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("/view/FXML/welcome.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Yu-Gi-Oh: Administrator :|");
        stage.show();
    }
}
