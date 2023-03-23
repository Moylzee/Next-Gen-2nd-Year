import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

public class Main extends JFrame implements MouseInputListener, Runnable {

    private static final Dimension WindowSize = new Dimension(800, 800);
    // Variables to create the cells in the window
    private static final int cell = 20;
    private static final int grid = 40;
    private boolean gameState[][] = new boolean[grid][grid]; // Boolean 2d Array for the cells
    public boolean playing = false; // Game State
    private BufferStrategy bufferStrategy; // Buffer 
    private static String workingDirectory = System.getProperty("user.dir"); // Get the Directory 

    public Main() {
        setTitle("Assignment 8");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width / 2 - WindowSize.width / 2;
        int y = screensize.height / 2 - WindowSize.height / 2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true); // Make the Window visible
        setResizable(false); // Makes it not possible to resize the window
        addMouseListener(this); // Mouse Listener
        addMouseMotionListener(this); // Mouse Motion Listener 

        // Thread
        Thread t = new Thread(this);
        t.start();
    }

    // Method for Buffer
    @Override
    public void addNotify() {
        super.addNotify();
        createBufferStrategy(2); // create a double buffer strategy
        bufferStrategy = getBufferStrategy();
    }

    // Run Function will run the function to check the cells on the interval
    @Override
    public void run() {
        paint(getGraphics());
        while (true) {
            try {
                Thread.sleep(300);
                if (playing) { // If the Game state is true check the rules and repaint the window
                    rules();
                    repaint();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        // Get the graphics context from the buffer strategy
        Graphics graphics = bufferStrategy.getDrawGraphics();

        // Paint the window in the cells
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                // Check each grid state and paint it white if true and black if false
                if (gameState[i][j] == true) {
                    graphics.setColor(Color.WHITE);
                } else {
                    graphics.setColor(Color.BLACK);
                }
                // Fill the window with the grid
                graphics.fillRect(i * cell, j * cell, cell, cell);
                // An outline around each cell for aesthetic
                graphics.setColor(Color.GRAY);
                graphics.drawRect(i * cell, j * cell, cell, cell);
            }
        }
        if (!playing) {
            // Start Button
            graphics.setColor(Color.green);
            graphics.fillRect(10, 40, 60, 20);
            graphics.setColor(Color.BLACK);
            graphics.drawString("START", 20, 55);
            // Random Button
            graphics.setColor(Color.green);
            graphics.fillRect(80, 40, 60, 20);
            graphics.setColor(Color.BLACK);
            graphics.drawString("RANDOM", 82, 55);
            // Load Button
            graphics.setColor(Color.green);
            graphics.fillRect(150, 40, 60, 20);
            graphics.setColor(Color.BLACK);
            graphics.drawString("LOAD", 160, 55);
            // Save Button
            graphics.setColor(Color.green);
            graphics.fillRect(220, 40, 60, 20);
            graphics.setColor(Color.BLACK);
            graphics.drawString("SAVE", 230, 55);
        }

        // Dispose of the graphics context and show the contents of the buffer
        graphics.dispose();
        bufferStrategy.show();
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
                } else if (gameState[i][j] == true
                        && (countSurroundingCells(i, j) < 2 || countSurroundingCells(i, j) > 3)) { // Any Live cell with too Many neighbours or too few neighbours dies
                    temp[i][j] = false;
                } else { // Any Live Cell with 2 or 3 alive neighbours lives on to the next generation
                    temp[i][j] = gameState[i][j];
                }
            }
        }
        gameState = temp; // Update the Cells
    }

    // Function to check the states of the cells neighbours
    // We will iterate through every cell and check its surrounding cells states
    // By returning count, we can use it in the 'rules' function to check for
    // conditions
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

    // Method used to load the file 
    // This is run when the load button is clicked
    public void load() {
        String line = null;
        String filename = workingDirectory+"\\Main.txt"; // The file I use in this assignment 
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            // Go Through the file and check for the one values
            // This check is used to know which Cells to paint white 
            for (int i = 0; i<grid; i++) {
                line = reader.readLine();
                for (int j = 0; j<grid; j++) {
                    gameState[i][j] = (line.charAt(j) == '1');
                }
            }
            reader.close();
        } catch (IOException e) {
        }
    }
    
    // Method used to Save the file
    // This is run when the Save button is clicked
    public void save() {
        String filename = workingDirectory+"\\Main.txt"; // The file to save the state to
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            // Go through each cell and check its state 
            // The corresponding 1 or 0 is stored in the text file for each cell
            for (int i = 0; i<grid; i++) {
                for (int j = 0; j<grid; j++) {
                    writer.write(gameState[i][j] ? "1" : "0"); // Conditional operator to check for 1 or 0
                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Buttons are all checked through this method
    public void checkButtons(MouseEvent e) {

        // Check if the Start Button is Pressed
        if ((e.getX() >= 10 && e.getX() <= 70) && (e.getY() >= 40 && e.getY() <= 60)) {
            playing = true;
            repaint();
        }
        // Check if the Randomise Button is Pressed
        if ((e.getX() >= 80 && e.getX() <= 140) && (e.getY() >= 40 && e.getY() <= 60)) {
            randomise();
        }

        // Check if the load Button is pressed
        if ((e.getX() >= 150 && e.getX() <= 210) && (e.getY() >= 40 && e.getY() <= 60)) {
            load();
        }
        // Check if the Save Button is pressed
        if ((e.getX() >= 220 && e.getX() <= 280) && (e.getY() >= 40 && e.getY() <= 60)) {
            save();
        }
    }

    // Mouse Clicked is still necessary for if the user wants to just paint one cell
    // or to press the buttons
    public void mouseClicked(MouseEvent e) {
        // Created a method to check if the buttons were pressed for tidy code
        checkButtons(e);

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

    // Mouse Dragged Method to allow for Painting of cells in a continuous way instead of constantly clicking  
    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / cell;
        int y = e.getY() / cell;

        // Check Where the cell was clicked and make sure it is within the bounds
        if (x >= 0 && x < grid && y >= 0 && y < grid) {
            gameState[x][y] = true; // Change the Cells State to true 
            repaint(); // Repaint the cell
        }
    }
    
    // Other Mouse Events not used
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}

    public static void main(String[] args) {
        new Main();
    }
}