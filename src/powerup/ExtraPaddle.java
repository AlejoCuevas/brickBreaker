package powerup;

import java.awt.*;

public class ExtraPaddle extends PowerUp {

    public ExtraPaddle(int x, int y) {
        super(x, y);
    }

    @Override
    protected Color getColor() {
        return Color.GREEN; // Color del power-up extra
    }

    @Override
    public void activar() {
    }
}
