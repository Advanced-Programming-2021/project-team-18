package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DebugApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML/main_menu.fxml"));
        Parent root = loader.load();
        ((View) (loader.getController())).setStage(stage);
        stage.setScene(new Scene(root));
        stage.setTitle("The Yu-Gi-oh! :)");
        stage.show();
    }
}
