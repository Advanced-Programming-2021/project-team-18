package view.menu.duelmenu;

import game.ChatMessage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import menus.MenuController;
import view.View;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChatMenu extends View {
    public Label onlinePeopleCount;
    public VBox chatBox;
    public TextField messageField;
    private static Timeline timeline;
    private Stage stage;
    @Setter
    @Getter
    private static int messageCount = 0;


    @FXML
    public void initialize() {
        refresh();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2),actionEvent -> {
            refresh();
            actionEvent.consume();
        });
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void stopRefresh(){
        timeline.stop();
    }

    public void sendMessage(MouseEvent mouseEvent) {
        MenuController.getInstance().sendMessage(messageField.getText());
    }

    private void refresh() {
        ArrayList<ChatMessage> newMessages = MenuController.getInstance().getNewMessages(messageCount);
        for (ChatMessage message : newMessages) {
            messageCount++;
            chatBox.getChildren().add(messageBox(message.getSender(), message.getMessage(), chatBox.getChildren().size() % 2 == 1));
        }
    }

    private HBox messageBox(String sender, String message, boolean isOdd) {
        HBox hBox = new HBox();
        hBox.setMinHeight(44);
        hBox.setMaxHeight(44);
        hBox.setMinWidth(380);
        hBox.setMaxWidth(380);
        hBox.setAlignment(Pos.CENTER);
        if (isOdd) hBox.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY,Insets.EMPTY)));
        else hBox.setBackground(new Background(new BackgroundFill(Color.color(0.1,0.1,0.1), CornerRadii.EMPTY,Insets.EMPTY)));
        hBox.getChildren().addAll(senderLabel(sender),messageText(message));
        return hBox;
    }
    private Label senderLabel(String nickname){
        Label label = new Label(nickname);
        label.setPadding(new Insets(0,10,0,10));
        label.setFont(Font.font(15));
        label.setMinWidth(125);
        label.setMinWidth(125);
        label.setPrefHeight(8);
        label.setTextAlignment(TextAlignment.LEFT);
        return label;
    }

    private Text messageText(String message){
        Text text = new Text(message);
        text.setFont(Font.font(20));
        text.setWrappingWidth(212);
        return text;
    }
}
