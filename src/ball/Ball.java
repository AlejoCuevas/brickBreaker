package ball;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {

    private int x;
    private int y;
    private int xDir, yDir;
    private final int diametro = 20;
    private double velocidad = 1.0;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
        this.xDir = -1;
        this.yDir = 2;
    }

    public void mover() {
        x += (int) (xDir * velocidad);
        y += (int) (yDir * velocidad);
    }

    public void reverseXDir() {
        xDir = -xDir;
    }

    public void reverseYDir() {
        yDir = -yDir;
    }

    public void aumentarVelocidad() {
        velocidad *= 1.1;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, diametro, diametro);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDiametro() {
        return diametro;
    }

    public void setxDir(int i) {
        xDir = i;
    }

    public void setyDir(int j) { // Nuevo método
        yDir = j;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {

        this.y = y;
    }
}