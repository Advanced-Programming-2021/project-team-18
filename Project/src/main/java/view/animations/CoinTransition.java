package view.animations;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

public class CoinTransition extends Transition {
    private static final String framesPath = "/animation_frames/coin/";
    private static final Image[] images;
    private static final int count;
    private final ImageView imageView;

    private int lastIndex;

    static {
        File frameFolder = null;
        try {
            frameFolder = new File(Objects.requireNonNull(CoinTransition.class.getResource(framesPath)).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert frameFolder != null;
        count = Objects.requireNonNull(frameFolder.listFiles()).length;
        images = new Image[count];
        for (int i = 0; i < count; i++) images[i] = getImage(i);
        System.out.println("Count: " + count);
    }

    public CoinTransition(ImageView imageView, Duration duration) {
        this.imageView = imageView;
        this.lastIndex = -1;
        setCycleDuration(duration);
    }

    public static Image getImage(int index) {
        if (index >= count)
            throw new IndexOutOfBoundsException("Illegal index: " + index);
        return new Image(Objects.requireNonNull(CoinTransition.class.getResource(
                framesPath + index + ".png")).toExternalForm());
    }


    @Override
    protected void interpolate(double v) {
        final int index = Math.min((int) Math.floor(v * count), count - 1);
        if (index == lastIndex) return;
        double previousX = imageView.getLayoutX() + imageView.getLayoutBounds().getCenterX();
        double previousY = imageView.getLayoutY() + imageView.getLayoutBounds().getCenterY();
        lastIndex = index;
        imageView.setImage(images[index]);
        imageView.setLayoutX(previousX - imageView.getLayoutBounds().getCenterX());
        imageView.setLayoutY(previousY - imageView.getLayoutBounds().getCenterY());
    }
}
