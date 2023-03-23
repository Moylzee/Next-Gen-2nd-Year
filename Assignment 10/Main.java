// Imports 
import javax.swing.JFrame;
import java.awt.*;

public class Main extends JFrame {

    // Done on an 800x800 window as easier to view when coding and reviewing
    // Dimensions of window and cells can be reduced to format a 200x200 grid 
    private static final Dimension WindowSize = new Dimension(800, 800);
    // Variables to create the cells in the window
    private static final int cell = 4;
    private static final int grid = 200;
    // Boolean 2d Array for the cells
    // Wall is a true Boolean (white)
    // Floor is a false boolean (black) 
    private boolean gameState[][] = new boolean[grid][grid];
    int run = 0; // Variable to keep track of how many times the rules method is called

    public Main() {
        setTitle("Assignment 10");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width / 2 - WindowSize.width / 2;
        int y = screensize.height / 2 - WindowSize.height / 2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true); // Make the Window visible
        setResizable(false); // Makes it not possible to resize the window

        // Give each cell either a true or false value 
        // True value for white cell and false value for black cell 
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                gameState[i][j] = Math.random() < 0.6; // 60% chance of a true value 
            }
        }

        // Perform the algorithm procedure 4 times 
        while (run < 4) {
            rules(); // Call the rules method 
            run++; // Track how many times the procedure has run 
        }
    }

    // Function to check the rules of each cell
    public void rules() {
        try {
            Thread.sleep(1000); // Sizeable delay to see the progress and not just the end result 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean[][] temp = new boolean[grid][grid]; // Temp boolean array to change the cell state 
            // Iterate Through the Window Cell by Cell
            for (int i = 0; i < grid; i++) {
                for (int j = 0; j < grid; j++) {
                    // If the cell has 5 or more true neighbours set it as true
                    if (checkSurroundingCells(i, j) >= 5) {
                        temp[i][j] = true; // assign the boolean value to the temp array 
                    }
                }
            }
            gameState = temp; // Update the Cells
            repaint(); // Repaint the window after every method run
        }
    


    // Function to check the status of the surrounding cells
    public int checkSurroundingCells(int x, int y) {
        int count = 0; // Variable to keep track of the number of white neighbouring cells
        // Iterate Through the Window Cell by Cell checking if the neighbouring cells are true or false
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
        return count; // Return the count value for the rules method 
    }

    public void paint(Graphics g) {
        // Paint the window in the cells
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                // Check each grid state and paint it white if true and black if false
                if (gameState[i][j]) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(i * cell, j * cell, cell, cell);
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
