package view;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.SneakyThrows;

public class DebugStarter extends Application {
    @Getter
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @SneakyThrows
    @Override
    public void start(Stage stage) {
        DebugStarter.stage = stage;
        (new DebugApp()).start(stage);
    }
}
