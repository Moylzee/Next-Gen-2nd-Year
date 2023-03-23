import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;
import java.awt.*;

public class Main extends JFrame implements MouseInputListener {

    private static final Dimension WindowSize = new Dimension(800, 800);
    // Variables to create the cells in the window 
    private static final int cell = 20;
    private static final int grid = 40;
    // Boolean 2d Array for the cells 
    private boolean gameState[][] = new boolean[grid][grid]; 

    public Main() {
        setTitle("Assignment 6");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width / 2 - WindowSize.width / 2;
        int y = screensize.height / 2 - WindowSize.height / 2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true); // Make the Window visible
        setResizable(false); // Makes it not possible to resize the window
        addMouseListener(this); // Mouse Listener 

        // Thread
        Thread t = new Thread();
        t.start();
    }

    public void run() {
        paint(getGraphics()); // Repaint the Screen after every time
        try { // Thread
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        // Paint the window in the cells
        for ( int i = 0; i<grid; i++) {
            for ( int j = 0; j<grid; j++){
                // Check each grid state and paint it white if true and black if false
                if (gameState[i][j] == true) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(i * cell, j * cell, cell, cell);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        // Get the position of the cell just clicked 
        int x = e.getX() / cell;
        int y = e.getY() / cell;
        // Change that cell boolean value to the opposite and repaint
        // This will change the color of the cell when it is repainted 
        if (gameState[x][y] == true) {
            gameState[x][y] = false;
        } else if (gameState[x][y] == false) {
            gameState[x][y] = true;
        }
        repaint();
    }

    // Other Mouse Events not used 
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public static void main (String[] args) {
        Main main = new Main();
    }
}