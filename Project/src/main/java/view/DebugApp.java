package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.menu.mainmenu.MainMenuView;

public class DebugApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        View.setStage(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML/main_menu.fxml"));
        Parent root = loader.load();
        MainMenuView view = loader.getController();
        stage.setScene(new Scene(root));
        view.adjustScene();
        UtilityView.playSound("menu_song.wav");
        stage.setTitle("The Yu-Gi-oh! :)");
        stage.show();
    }
}
