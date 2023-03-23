import java.awt.Image;

public class Alien extends Sprite2D {
        protected static double xSpeed=0;
        int Width = 50;
        int Height = 50;

        public Alien(Image i) {
        super(i); // invoke constructor on superclass Sprite2D
        myImage2 = i;
    }

    // public interface
    public boolean move() {
        x+=xSpeed;
        // Only check if reversal needed for the images that are visible
        if (isVisible) {
            if (x<=0 || x>=winWidth-myImage.getWidth(null)) {
                return true;
            }
        }
        return false;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public static void setFleetXSpeed(double dx) {
        xSpeed=dx;
    }

    public static double getFleetXSpeed() {
        return xSpeed;
    }

    public static void reverseDirection() {
        xSpeed=-xSpeed;
    }

    public void jumpDownwards() {
        y+=20;
    }
}