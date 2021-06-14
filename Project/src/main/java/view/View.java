package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class View {
    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public View loadView(String address) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(address));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        View controllerView = loader.getController();
        controllerView.setStage(this.stage);
        return controllerView;
    }

}
