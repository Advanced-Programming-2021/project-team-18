package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DebugApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        View.setStage(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML/main_menu.fxml"));
        ((View) (loader.getController())).setStage(stage);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("The Yu-Gi-oh! :)");
        stage.show();
    }
}
