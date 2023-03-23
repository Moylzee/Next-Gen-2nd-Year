import java.awt.*;

public class GameObject {
    
    private int x,y;
    private Color c;

    public GameObject() {
        x = (int)(Math.random()*550);
        y = (int)(Math.random()*550);
    }

    // Move to a random place in the window 
    public void move() {
        x = (int)(Math.random()*550);
        y = (int)(Math.random()*550);
    }

    public void paint (Graphics g) {   
        // To get a random colour 
        int red = (int)(Math.random()*256);
        int green = (int)(Math.random()*256);
        int blue = (int)(Math.random()*256);
        c = new Color(red, green, blue); //Generates the color based on the 3 random values
        g.setColor(c); //Use the Color generated 
        g.fillRect(x, y, 50, 50); //Create the Square 
    }
}