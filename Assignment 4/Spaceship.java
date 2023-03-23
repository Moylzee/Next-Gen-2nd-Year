import javax.swing.ImageIcon;
import java.awt.*;

public class Spaceship extends Sprite2D{
    
    public Spaceship(ImageIcon image, Dimension WindowSize){
        // Inherits
        super(image);
        this.xSpeed = 0; // controlled by player inputs
    }

    public void move(){
        this.x += this.xSpeed;
    }
}