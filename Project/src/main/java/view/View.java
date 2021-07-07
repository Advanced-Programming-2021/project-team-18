package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class View {
    protected static Stage stage;

    public static void setStage(Stage stage) {
        View.stage = stage;
    }

    public View loadView(String fileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML/" + fileName + ".fxml"));
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        ((View) loader.getController()).adjustScene();
        return loader.getController();
    }

    public void adjustScene() {

    }
}
