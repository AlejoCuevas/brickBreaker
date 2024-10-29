package ball;

import java.awt.*;

public class CannonBall extends Ball {
    public static final int DIAMETRO = 20; // Diámetro de la bola del cañón
    private int ySpeed = -10; // Velocidad hacia arriba

    public CannonBall(int x, int y) {
        super(x, y);
    }

    @Override
    public void mover() {

        setY(getY() + ySpeed);
    }

    public void disparar() {
        ySpeed = -10;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED); // Color de la bola del cañón
        g.fillOval(getX(), getY(), DIAMETRO, DIAMETRO);
    }

    @Override
    public int getDiametro() {
        return DIAMETRO; // Retorna el diámetro para la colisión
    }
}
