import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.util.Random;

public class Main extends JFrame implements MouseInputListener, Runnable {

    private static final Dimension WindowSize = new Dimension(800, 800);
    // Variables to create the cells in the window
    private static final int cell = 20;
    private static final int grid = 40;
    private boolean gameState[][] = new boolean[grid][grid]; // Boolean 2d Array for the cells
    public boolean playing = false; // Game State

    public Main() {
        setTitle("Assignment 7");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width / 2 - WindowSize.width / 2;
        int y = screensize.height / 2 - WindowSize.height / 2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true); // Make the Window visible
        setResizable(false); // Makes it not possible to resize the window
        addMouseListener(this); // Mouse Listener

        // Thread
        Thread t = new Thread(this);
        t.start();
    }

    // Run Function will run the function to check the cells on the interval
    @Override
    public void run() {
        paint(getGraphics());
        while (true) {
            try {
                Thread.sleep(300);
                if (playing) {
                    rules();
                    repaint();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        // Paint the window in the cells
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                // Check each grid state and paint it white if true and black if false
                if (gameState[i][j] == true) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                // Fill the window with the grid
                g.fillRect(i * cell, j * cell, cell, cell);
                // An outline around each cell for aesthetic
                g.setColor(Color.GRAY);
                g.drawRect(i * cell, j * cell, cell, cell);
            }
        }
        if (!playing) {
            // Start Button
            g.setColor(Color.green);
            g.fillRect(10, 40, 60, 20);
            g.setColor(Color.BLACK);
            g.drawString("START", 20, 55);
            // Random Button
            g.setColor(Color.green);
            g.fillRect(80, 40, 60, 20);
            g.setColor(Color.BLACK);
            g.drawString("RANDOM", 82, 55);
        }
    }

    // Function to randomise the Window 
    public void randomise() {
        repaint();
        Random rd = new Random(); // creating Random object
        // Iterate Through the Window Cell by Cell
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                if (rd.nextInt(5) == 0) { // With a probability of 1/5
                    boolean rand = rd.nextBoolean(); // generate a random boolean
                    gameState[i][j] = rand; // update the corresponding game state
                }
            }
        }
        repaint(); // repaint the screen to reflect the changes
    }

    // Function to Implement the rules of the game 
    public void rules() {
        // Temp variable for the Function
        boolean[][] temp = new boolean[grid][grid];

        // Iterate Through the Window Cell by Cell
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                if (gameState[i][j] == false && countSurroundingCells(i, j) == 3) { // Any dead cell with exactly three live neighbours becomes a live cell
                    temp[i][j] = true;
                }else if (gameState[i][j] == true && (countSurroundingCells(i, j) < 2 || countSurroundingCells(i, j) > 3)) { // Any Live cell with too Many neighbours or too few neighbours dies
                    temp[i][j] = false;
                }else { // Any Live Cell with 2 or 3 alive neighbours lives on to the next generation
                    temp[i][j] = gameState[i][j];
                }
            }
        }
        gameState = temp; // Update the Cells 
    }

    // Function to check the states of the cells neighbours
    // We will iterate through every cell and check its surrounding cells states
    // By returning count, we can use it in the 'rules' function to check for conditions 
    public int countSurroundingCells(int x, int y) {
        // Variable to count Live neighbours
        int count = 0;

        // Iterate Through the Window Cell by Cell
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {

                if (i >= 0 && i < grid && j >= 0 && j < grid) {
                    if (i != x || j != y) {
                        if (gameState[i][j] == true) {
                            count++;
                        }
                    }
                }
            }
        }
        return count; // Return it to be used in another Function when called
    }

    public void mouseClicked(MouseEvent e) {
        // Get the position of the cell just clicked
        int x = e.getX() / cell;
        int y = e.getY() / cell;

        // Check if the Start Button is Pressed
        if ((e.getX() >= 10 && e.getX() <= 70) && (e.getY() >= 40 && e.getY() <= 60)) {
            playing = true;
            repaint();
        }
        // Check if the Randomise Button is Pressed
        if ((e.getX() >= 80 && e.getX() <= 140) && (e.getY() >= 40 && e.getY() <= 60)) {
            randomise();
        }

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

    public static void main(String[] args) {
        Main main = new Main();
    }
}