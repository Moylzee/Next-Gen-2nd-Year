import java.awt.*;
import javax.swing.*;

public class MovingSquaresApplication extends JFrame implements Runnable {

    private static final Dimension WindowSize = new Dimension(600, 600);
    private static final int NumGameObjects = 30;
    private GameObject[]  GameObjectsArray = new GameObject[NumGameObjects];

    public MovingSquaresApplication() {
        setTitle("Assignment 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setResizable(false); //Makes it not possible to resize the window

        // Add the squares to the array as game objects 
        for (int i =0; i<NumGameObjects; i++) {
            GameObject obj = new GameObject();
            GameObjectsArray[i] = obj;
        }
        // Thread 
        Thread t = new Thread(this);
        t.start();
    }

    public void paint (Graphics g) {

        g.setColor(Color.WHITE); // Set to white so it will repaint the window white 
        g.fillRect(0, 0, 600, 600); // Fill the window with the white square the size of the window        
           
        for (GameObject obj : GameObjectsArray) {
            obj.move(); // Calls the move function in the onject class
            obj.paint(g); // Calls the paint function in the object class 
        }   
    }

    public void run() {
        while (true) {
            repaint(); // Repaint the Screen after every time  
                try { // Thread 
                    Thread.sleep(500);
                } catch (InterruptedException e ) {
                    e.printStackTrace();
                } 
            }
    }
    public static void main (String[] args) {
        MovingSquaresApplication window = new MovingSquaresApplication();
        window.setVisible(true);
    }
}
