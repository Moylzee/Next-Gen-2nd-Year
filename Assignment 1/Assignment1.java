import java.awt.*;
import javax.swing.*;

public class Assignment1 extends JFrame {
    private static final  Dimension WindowSize = new Dimension(600,600);

    public Assignment1() {
        setTitle("Assignment 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setResizable(false); //Makes it not possible to resize the window
    }
    public void paint(Graphics g) {
        //Nested loop used to create squares until they fill the window
        for (int i=0; i<10; i++) {
            int x = i * WindowSize.width/10; // X value for co-ordinate system
            //WindowSize is used to always remain inside the max values of the window 
            for (int j=0; j<10; j++) {
                int y = j * WindowSize.height/10; // Y value for co-ordinate system
                //To Generate Colors (RGB)
                int red = (int)(Math.random()*256);
                int green = (int)(Math.random()*256);
                int blue = (int)(Math.random()*256);
                Color c = new Color(red, green, blue); //Generates the color based on the 3 random values
                g.setColor(c); //Use the Color generated 
                g.fillRect(x, y, 50, 50); //Create the Square
            }
        }
    }
    public static void main(String[] args) {
        Assignment1 window = new Assignment1();
        window.setVisible(true); //Makes the window visible
    }
}