import javax.swing.*;

public class Alien extends Sprite2D{
    private static int dir = 1; // Direction 
    private static int xSpeed = 0; // So they move 

    public Alien(ImageIcon image) {
        // inherit
        super(image);
        Alien.xSpeed = 5 * dir;
    }

    public void move() {
        this.x += (xSpeed * dir); 
    }

    public void reverseDirection() {
        dir *= -1;
    }

}
