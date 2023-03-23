import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

private static String workingDirectory;
private static boolean isInitialised = false;
private static final Dimension WindowSize = new Dimension(800,600);
private BufferStrategy strategy;
private Graphics offscreenGraphics;
private static final int NUMALIENS = 30;
public Alien[] AliensArray = new Alien[NUMALIENS];
private Spaceship PlayerShip;
private Image bullet; // Bullet Image 
public Image endScreen; // Game Over Image 
private ArrayList<PlayerBullet> bulletslist = new ArrayList<>();
public boolean gameover = false; // Game State
private int currentWave = 1; // Wave Counter
private int score = 0; // Score Counter 
public boolean allAliensInvisible = true; // Check That all aliens are dead Variable 

public InvadersApplication() {
    //Display the window, centred on the screen
    Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    int x = screensize.width/2 - WindowSize.width/2;
    int y = screensize.height/2 - WindowSize.height/2;
    setBounds(x, y, WindowSize.width, WindowSize.height);
    setResizable(false);
    setVisible(true);
    this.setTitle("Space Invaders");
    // load image from disk
    ImageIcon icon = new ImageIcon(workingDirectory + "\\alien_ship_1.png");
    Image alienImage = icon.getImage();
    icon = new ImageIcon(workingDirectory + "\\alien_ship_2.png");
    Image alienImage2 = icon.getImage();

    // create and initialise some aliens, passing them each the image we have
    for (int i=0; i<NUMALIENS; i++) {
        AliensArray[i] = new Alien(alienImage);
        AliensArray[i].myImage2 = alienImage2;
        double xx = (i%5)*80 + 70;
        double yy = (i/5)*40 + 50;
        AliensArray[i].setPosition(xx, yy);
    }

    Alien.setFleetXSpeed(2);
    // create and initialise the player's spaceship
    icon = new ImageIcon(workingDirectory + "\\player_ship.png");
    Image shipImage = icon.getImage();
    PlayerShip = new Spaceship(shipImage);
    PlayerShip.setPosition(300,530);

    // tell all sprites the window width
    Sprite2D.setWinWidth(WindowSize.width);

    // Bullet Image 
    icon = new ImageIcon(workingDirectory + "\\bullet.png");
    bullet = icon.getImage();

    // I am using an image to display the game over Screen
    icon = new ImageIcon(workingDirectory + "\\game_over.png");
    endScreen = icon.getImage();

    // create and start our animation thread
    Thread t = new Thread(this);
    t.start();

    // send keyboard events arriving into this JFrame back to its own event
    addKeyListener(this);

    // initialise double-buffering
    createBufferStrategy(2);
    strategy = getBufferStrategy();
    offscreenGraphics = strategy.getDrawGraphics();
    isInitialised = true;
}

// thread's entry point
public void run() {
    while ( true ) {
    // 1: sleep for 1/50 sec
    try {
        Thread.sleep(5);
    } catch (InterruptedException e) { }
    // 2: animate game objects
    boolean alienDirectionReversalNeeded = false;
    for (int i=0;i<NUMALIENS; i++) {
    if (AliensArray[i].move())
    alienDirectionReversalNeeded=true;
    }
    if (alienDirectionReversalNeeded) {
    Alien.reverseDirection();
    for (int i=0;i<NUMALIENS; i++)
    AliensArray[i].jumpDownwards();
    }
    PlayerShip.move();
    if (bulletslist != null) {
        bulletslist.forEach(bullet->{ 
            bullet.move();
        });
    }
    // Always Check for Collisions for Bullet and Aliens
    checkCollisions();
    // Always Check for a new wave 
    newWave();
    // Always Check for Alien and spaceship collisions
    endGame();
    // 3: force an application repaint
    this.repaint();
    }
}

// Function to Create a new wave when all the aliens are dead
public void newWave() {
    boolean allAliensInvisible = true;
    for (Alien alien : AliensArray) {
        if (alien.isVisible) {
            allAliensInvisible = false;
            break;
        }
    }
    // To reset the aliens and start a new wave
    if (allAliensInvisible) {
        Alien.setFleetXSpeed(Alien.getFleetXSpeed() + 1);
        for (Alien alien : AliensArray) {
            alien.isVisible = true;
            allAliensInvisible = false;
            resetAliens();
        }
        currentWave++; // Increment the wave counter
    }
}

// Function to Shoot the bullet
public void shootBullet() {
    // add a new bullet to our list
    PlayerBullet b = new PlayerBullet(bullet);
    b.setPosition(PlayerShip.x + 54 / 2, PlayerShip.y);
    bulletslist.add(b);
}

// Function to Check Collisions
public void checkCollisions() {
    for (Alien alien : AliensArray) {
    if (!alien.isVisible){
        continue;
    }
        Rectangle alienBox = new Rectangle((int)alien.x, (int)alien.y, alien.Width, alien.Height);
        
        for (PlayerBullet bullet : bulletslist){
            if (!bullet.isVisible){
                continue; 
            }
            Rectangle bulletBox = new Rectangle((int)bullet.x, (int)bullet.y, bullet.Width,bullet.Height);
        
            // If They Collide 
            if (bulletBox.intersects(alienBox)) {
                score += 10; // Add to the score 
                // Remove The Bullet and Alien That collides
                bullet.setVisible(false);
                alien.setVisible(false);
                repaint();
            }
        }
    }
}

//  Parameters to Check to End the Game 
public void endGame() {
    // Game is Only 5 Rounds Long
    for (Alien alien : AliensArray) {
        // Only Check the visible aliens 
        if (alien.isVisible == true) {
            if ( (currentWave == 6 && allAliensInvisible == true) || alien.y >= 500) {
                gameover = true; // Change the Game State
                resetAliens(); // Reset The Aliens
                // Reset the Wave and Score
                currentWave = 1;
                score = 0;
            }
        }
    }
}

// Function to reset the aliens to play again
public void resetAliens() {
    for (int i = 0; i < NUMALIENS; i++) {
        double xx = (i % 5) * 80 + 70;
        double yy = (i / 5) * 40 + 50;
        AliensArray[i].setPosition(xx, yy);
    }
}

// Three Keyboard Event-Handler functions
public void keyPressed(KeyEvent e) {
    if (e.getKeyCode()==KeyEvent.VK_LEFT) {
        PlayerShip.setXSpeed(-4);
    } else if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
        PlayerShip.setXSpeed(4);
    }

    // For the Bullet
    if (e.getKeyCode()==KeyEvent.VK_SPACE) {     
        // For the Bullet
        shootBullet();
    }

    // If the Game is over and they Press Enter to play again, Reset the Game
    if (gameover == true) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER) {
            gameover = false;
            resetAliens();
            repaint();
        }
    }
}

public void keyReleased(KeyEvent e) {
    if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT) {
        PlayerShip.setXSpeed(0);
    }
}

public void keyTyped(KeyEvent e) { }

// application's paint method
public void paint(Graphics g) {
    // Check the Game State to run the correct sequences
    // If Game State is Game over, Show the Game over Screen
    if (gameover) {
        g.drawImage(endScreen, 0, 0, WindowSize.width, WindowSize.height, null);
    } else if (!gameover) { // Otherwise, Run The Game
        if (!isInitialised)
            return;
        g = offscreenGraphics; // draw to offscreen buffer
        // clear the canvas with a big black rectangle
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height);

        // Round Counter 
        g.setColor(Color.WHITE);
        g.drawString("WAVE: " +currentWave, 10, 50);

        // Score Counter
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + score, 100, 50);

        // redraw all game objects
        for (int i=0;i<NUMALIENS; i++) {
            AliensArray[i].paint(g);
            PlayerShip.paint(g);
        }
        Iterator iterator = bulletslist.iterator();
        while (iterator.hasNext()) {
            PlayerBullet b = (PlayerBullet) iterator.next();
            b.paint(g);
        }
        // flip the buffers offscreen<-->onscreen
        strategy.show();
    }
}
// application entry point
public static void main(String[] args) {
    workingDirectory = System.getProperty("user.dir");
    System.out.println("Working Directory = " + workingDirectory);
    InvadersApplication w = new InvadersApplication();
}
}