package mapa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel implements ActionListener {

    private JButton jugarButton;
    private JButton salirButton;

    public Menu() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        jugarButton = new JButton("Jugar");
        salirButton = new JButton("Salir");

        jugarButton.addActionListener(this);
        salirButton.addActionListener(this);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(jugarButton, gbc);

        gbc.gridy = 1;
        add(salirButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jugarButton) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.remove(this); // Remover el menú
            Game game = new Game(); // Crear nueva instancia de juego
            frame.add(game); // Agregar el juego al frame
            frame.revalidate(); // Revalidar para aplicar cambios
            frame.repaint(); // Redibujar el frame
            game.requestFocus(); // Asegurarse de que el juego tenga el foco
        } else if (e.getSource() == salirButton) {
            System.exit(0); // Cerrar la aplicación
        }
    }
}
