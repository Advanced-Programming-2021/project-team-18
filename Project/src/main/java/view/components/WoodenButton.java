package view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

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

    @SneakyThrows
    public WoodenButton() {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML/wooden_button.fxml"));
        loader.setRoot(this);
        loader.setController(this);
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
    @SneakyThrows
    private void onMouseEntered() {
        File file = new File(getClass().getResource("/background/button_hovered.png").toURI());
        imageView.setImage(new Image(file.toURI().toString()));
    }
    @SneakyThrows
    private void onMouseExited() {
        File file = new File(getClass().getResource("/background/button.png").toURI());
        imageView.setImage(new Image(file.toURI().toString()));
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        this.text.setFont(new Font("Times Roman", fontSize));
    }
}
