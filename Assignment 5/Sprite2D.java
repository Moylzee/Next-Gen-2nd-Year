import java.awt.Graphics;
import java.awt.Image;

public class Sprite2D {

// member data
protected double x,y;
protected Image myImage;
protected Image myImage2;
protected boolean isVisible = true;

// static member data
protected static int winWidth;
// constructor
protected int framesDrawn = 0;

public Sprite2D(Image i) {
    myImage = i;
    myImage2 = i;
}

public void setPosition(double xx, double yy) {
    x=xx;
    y=yy;
}

// Draw the Images that should be visible
public void paint(Graphics g) {
    framesDrawn++;
    if (isVisible) {
    if (framesDrawn % 100 < 50) {
        g.drawImage(myImage, (int) x, (int) y, null);
    } else {
        g.drawImage(myImage2, (int) x, (int) y, null);
    }
}
}

public static void setWinWidth(int w) {
    winWidth = w;
}
}