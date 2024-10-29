package powerup;

import java.awt.*;

public class RemocionDeBloques extends PowerUp {

    private boolean usado;

    public RemocionDeBloques(int x, int y) {

        super(x, y);

    }


    protected Color getColor() {

        return Color.YELLOW;

    }

    public void activar() {

        System.out.println("Bloque eliminado por remocion de bloques.");
        usado = true;
    }
    public boolean esUsado() {
        return usado;
    }
}
