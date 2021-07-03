package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
    private static boolean loginFirst;

    public static void main(String[] args) {
        loginFirst = false;
        if (args.length > 0)
            if (args[0].matches(".*log.*")) loginFirst = true;
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader;
        if (loginFirst)
            loader = new FXMLLoader(getClass().getResource("/view/FXML/login.fxml"));
        else
            loader = new FXMLLoader(getClass().getResource("/view/FXML/register.fxml"));
        Parent root = loader.load();
        View.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        // stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("The Yu-Gi-Oh! :)");
        stage.show();
    }
}
