package view;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.SneakyThrows;

public class Starter extends Application {
    @Getter
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @SneakyThrows
    @Override
    public void start(Stage stage) {
        Starter.stage = stage;
        (new App()).start(stage);
    }
}
