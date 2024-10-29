package mapa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel implements ActionListener {

    private JButton jugarButton;
    private JButton salirButton;
    private JComboBox<String> nivelComboBox;
    private Image backgroundImage; // Para la imagen de fondo

    public Menu() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        backgroundImage = new ImageIcon("Captura.png").getImage();

        // Crear botón Jugar y Salir
        jugarButton = new JButton("Jugar");
        salirButton = new JButton("Salir");

        // Crear JComboBox para seleccionar el nivel
        String[] niveles = {"Nivel 1", "Nivel 2", "Nivel 3", "Nivel 4"};
        nivelComboBox = new JComboBox<>(niveles);

        jugarButton.addActionListener(this);
        salirButton.addActionListener(this);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(nivelComboBox, gbc); // Agregar el JComboBox al menú

        gbc.gridy = 1;
        add(jugarButton, gbc);

        gbc.gridy = 2;
        add(salirButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibujar la imagen de fondo
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jugarButton) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.remove(this); // Remover el menú
            int selectedLevel = nivelComboBox.getSelectedIndex() + 1; // Ajustar el índice de nivel seleccionado
            Game game = new Game(frame, selectedLevel); // Crear nueva instancia de juego con el nivel seleccionado
            frame.add(game); // Agregar el juego al frame
            frame.revalidate();
            frame.repaint();
            game.requestFocus();
        } else if (e.getSource() == salirButton) {
            System.exit(0); // Cerrar la aplicación
        }
    }
}
