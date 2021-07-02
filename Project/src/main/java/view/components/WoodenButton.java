package view.components;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WoodenButton extends StackPane implements Initializable {

    private double height, width;
    private int fontSize = 12;
    @FXML
    private ImageView imageView;
    @FXML
    private Text text;

    public WoodenButton() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML/woodenButton.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.setClassLoader(getClass().getClassLoader());
        loader.load();

    }

    public double getInitialHeight() {
        return height;
    }

    public void setInitialHeight(double initialHeight) {
        imageView.setFitHeight(initialHeight);
        super.setMaxHeight(width);
        this.height = initialHeight;
    }

    public double getInitialWidth() {
        return width;
    }

    public void setInitialWidth(double initialWidth) {
        imageView.setFitWidth(initialWidth);
        super.setMaxWidth(initialWidth);
        this.width = initialWidth;
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public void setImage(Image image) {
        this.imageView.setImage(image);
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOnMouseEntered(x -> onMouseEntered());
        setOnMouseExited(x -> onMouseExited());
        text.setFont(new Font("Times Roman", fontSize));
        text.setFill(Color.valueOf("#151b37"));
    }

    private void onMouseEntered() {
        File file = new File("src/main/resources/background/button_hovered.png");
        imageView.setImage(new Image(file.toURI().toString()));
    }

    private void onMouseExited() {
        File file = new File("src/main/resources/background/button.png");
        imageView.setImage(new Image(file.toURI().toString()));
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        this.text.setFont(new Font("", fontSize));
    }
}
