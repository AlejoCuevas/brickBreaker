package powerup;

import java.awt.*;

public class RalentizarTiempo extends PowerUp {

    public RalentizarTiempo(int x, int y) {
        super(x, y);
    }

    @Override
    protected Color getColor() {
        return Color.MAGENTA; // Color para este power-up
    }

    @Override
    public void activar() {
        // Lógica para ralentizar el tiempo
        System.out.println("Ralentizando el tiempo.");
    }

}
