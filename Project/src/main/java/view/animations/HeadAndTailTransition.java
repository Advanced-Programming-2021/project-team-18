package view.animations;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lombok.Getter;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

public class HeadAndTailTransition extends Transition {
    private static final String framesPath = "/animation_frames/silver_coin/";
    private static final Image[] images;
    private static final int count;
    private final ImageView imageView;
    private int currentCycleCount;
    private int totalCycleCount;
    private int starterIndex;
    private boolean isHead;
    @Getter
    private boolean hasStopped;
    private int index;
    static {
        File frameFolder = null;
        try {
            frameFolder = new File(Objects.requireNonNull(CoinTransition.class.getResource(framesPath)).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert frameFolder != null;
        count = Objects.requireNonNull(frameFolder.listFiles()).length;
        images = new Image[count + 1];
        for (int i = 1; i <= count; i++) images[i] = getImage(i);
    }
    public static Image getImage(int index) {
        if (index > count)
            throw new IndexOutOfBoundsException("Illegal index: " + index);
        return new Image(Objects.requireNonNull(CoinTransition.class.getResource(
                framesPath + index + ".png")).toExternalForm());
    }

    public HeadAndTailTransition(ImageView imageView, Duration duration , int totalCycleCount , boolean isHead){
        this.isHead = isHead;
        this.imageView = imageView;
        this.totalCycleCount = totalCycleCount;
        if(isHead) starterIndex = 5;
        else starterIndex = 15;
        index = starterIndex;
        currentCycleCount = 0;
        setCycleDuration(duration);
    }

    @Override
    protected void interpolate(double v) {
        if(hasStopped) return ;
        int indexNow = Math.min((int) Math.floor(v * count), count - 1) + starterIndex;
        if(indexNow > count) indexNow -= count;
        if (index == indexNow) return;
        ++ currentCycleCount;
        if(currentCycleCount >= totalCycleCount * count && (index == 5 || index == 15)) {
            hasStopped = true;
            stop();
            return;
        }
        setRate(getRate()*0.96);
        double previousX = imageView.getLayoutX() + imageView.getLayoutBounds().getCenterX();
        double previousY = imageView.getLayoutY() + imageView.getLayoutBounds().getCenterY();
        index = indexNow;
        imageView.setImage(images[index]);
        imageView.setLayoutX(previousX - imageView.getLayoutBounds().getCenterX());
        imageView.setLayoutY(previousY - imageView.getLayoutBounds().getCenterY());
    }
}
