package mapa;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BrickBreaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = ge.getDefaultScreenDevice();

        graphicsDevice.setFullScreenWindow(frame);

        Menu menu = new Menu();
        frame.add(menu);

        Game game = new Game(frame, 1); // Pasar el frame al juego
        frame.setVisible(true); // Hacer visible el frame
    }
}
