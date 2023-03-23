import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class InvadersApplication extends JFrame implements Runnable, KeyListener{
    
    private static final Dimension WindowSize = new Dimension(800, 600);
    private static final int NumEnemies = 30;
    private Alien[] enemies = new Alien[NumEnemies];
    private Sprite2D playerShip = new Spaceship(null, WindowSize);
    private BufferStrategy strategy;
    
    public InvadersApplication() {
        setTitle("Space Invaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true); // Make the Window visible
        setResizable(false); // Makes it not possible to resize the window
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        addKeyListener(this); // Key Listener 


        // Add the Aliens to the array as game objects and Position them in a 5x6 format
        for (int i = 0; i < NumEnemies; i++) {
            Alien obj = new Alien(null);
            obj.x = (i % 5) * (obj.alienWidth + 10) + 50; 
            obj.y = (i / 5) * (obj.alienHeight + 10) + 50;
            enemies[i] = obj;
        }
        
        // Thread 
        Thread t = new Thread(this);
        t.start();
        }

    private int direction = 1; // 1 = right, -1 = left

    public void run() {
        while (true) {
            paint(getGraphics()); // Repaint the Screen after every time  
            boolean edge = false; // Edge is used to check if the aliens hit the edge
            for (Alien alien : enemies) {
                try {
                    alien.paintEnemy(getGraphics());
                    alien.x += direction;
    
                    if (alien.x + 50 > 800) { // If the alien hits the right window edge which is 800 for this assignment 
                        direction = -1; // Move in a negative direction
                        alien.reverseDirection(); // Reverse Direction Function called
                        edge = true; // They have hit the edge
                        
                    } else if (alien.x <= 0) { // If the alien hits the left window edge
                        direction = 1; // Move in a positive direction
                        alien.reverseDirection(); // Reverse Direction Function called
                        edge = true; // They have hit the edge
                    }
                    if (edge) { // When they hit the edge
                        for (Alien aliens : enemies) {
                            aliens.y += 10; // Move their y position down by 10 pixels
                        }
                    edge = false; // Reset edge to False

                }} catch (Exception e) {}
            }
            try {
                playerShip.paintPlayer(getGraphics());
            } catch (Exception e) {}
            try { // Thread 
                Thread.sleep(50);
            } catch (InterruptedException e ) {
                e.printStackTrace();
            } 
        }
    }
    
    // Key Events

    // When key is held, move the player until released
    public void keyPressed(KeyEvent e) {  
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            playerShip.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT){
            playerShip.moveLeft(); 
      }
    }

    // When key is released, Stop moving the player
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            playerShip.xSpeed = 0; 
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT){
            playerShip.xSpeed = 0; 
      }
    }

    public void keyTyped(KeyEvent e) {}

    // Paint Function
    public void paint (Graphics g) {
        g = strategy.getDrawGraphics();
        // Set the Background 
        g.setColor(Color.BLACK);  
        g.fillRect(0, 0, 800, 600); 

        //Paint the enemies and moves them 
        for (Sprite2D obj : enemies) {
            obj.move(); // Calls the move function in the object class
        }  
        strategy.show();
        g.dispose();
}

    public static void main (String[] args) {
        InvadersApplication window = new InvadersApplication();
    }
    // Buffering code was implemented as per lecture slides. it just does not work
}