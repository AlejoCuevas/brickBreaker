package ball;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {

    private int x,y;
    private int xDir, yDir;
    private final int diametro = 20;

    public Ball(int x, int y) {

        this.x = x;
        this.y = y;
        this.xDir = -1;
        this.yDir = -2;

    }

    public void mover() {

        x += xDir;
        y += yDir;

    }

    public void reverseXDir(){

        xDir = -xDir;
    }

    public void reverseYDir(){
        yDir = -yDir;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(x,y,diametro,diametro);
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getDiametro() {return diametro;}
}