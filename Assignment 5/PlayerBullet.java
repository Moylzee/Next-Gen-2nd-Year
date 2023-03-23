import java.awt.Image;

public class PlayerBullet extends Sprite2D {

    private static double ySpeed = 15;
    int Width = 50;
    int Height = 50;
    
    public PlayerBullet(Image i) {
        super(i); // invoke constructor on superclass Sprite2D
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    // public interface
    public boolean move() {
        y -= ySpeed;
        // direction reversal needed?
        if (y <= 0 || y >= winWidth - myImage.getWidth(null)) {
            return true;
        } else {
            return false;
        }
    }
}
