import java.awt.*;
import javax.swing.*;

public class Sprite2D {
    
    private static String workingDirectory  = System.getProperty("user.dir");
    public int x,y;
    private Image enemies, player;
    public int xSpeed = 0;
    public int alienWidth = 50;
    public int alienHeight = 50;
    public Alien alien;


    public Sprite2D(ImageIcon image) {
        // Set the images
        ImageIcon enemy = new ImageIcon(workingDirectory + "\\Images\\alien_ship_1.png");
        ImageIcon player = new ImageIcon(workingDirectory + "\\Images\\player_ship.png");
        enemies = enemy.getImage();
        this.player = player.getImage();
        // Used to set the initial position of the player
        this.x = 300; 
        this.y = 550;
    }

    // Move to a random place in the window 
    public void move() {

    }

    public void moveRight() {
        this.xSpeed = 15; // positive value to go right 
    }
     
    public void moveLeft() {
        this.xSpeed = -15; // Negative value to go left 
    }

    public void paintEnemy(Graphics g) {
        g.drawImage(enemies, x, y, alienWidth, alienHeight, null); // Draw the Enemies 
    }  

    public void paintPlayer(Graphics g) {
        this.x += this.xSpeed;
        g.drawImage(player, x, y, null); // Draw the player and update it with movement 
    }  
}