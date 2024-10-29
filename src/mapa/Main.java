package mapa;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Crear un JFrame
        JFrame frame = new JFrame("BrickBreaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Sin bordes
        frame.setResizable(false); // No redimensionable

        // Configurar el JFrame para pantalla completa
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = ge.getDefaultScreenDevice();

        // Establecer a pantalla completa
        graphicsDevice.setFullScreenWindow(frame);

        Menu menu = new Menu(); // Crear el menú
        frame.add(menu); // Añadir el menú al frame
        frame.setVisible(true); // Hacer visible el frame
    }
}
