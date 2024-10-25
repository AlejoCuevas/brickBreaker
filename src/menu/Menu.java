package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel implements ActionListener {

    private JButton playButton;
    private JButton exitButton;
    private ActionListener startGameListener;

    public Menu(ActionListener startGameListener) {
        this.startGameListener = startGameListener;

        setLayout(new BorderLayout()); // Utiliza BorderLayout para ocupar todo el espacio

        playButton = new JButton("Jugar");
        exitButton = new JButton("Salir");

        // Configurar el panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Usa FlowLayout para centrar los botones
        buttonPanel.setOpaque(false); // Fondo transparente
        buttonPanel.add(playButton);
        buttonPanel.add(exitButton);

        // Añadir acción a los botones
        playButton.addActionListener(this);
        exitButton.addActionListener(this);

        add(buttonPanel, BorderLayout.CENTER); // Añadir el panel de botones al centro
        setBackground(Color.BLACK); // Fondo negro
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            startGameListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "startGame"));
        } else if (e.getSource() == exitButton) {
            System.exit(0); // Salir del juego
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Personalizar el fondo del menú, si lo deseas
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight()); // Fondo azul
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("BRICK BREAKER", getWidth() / 2 - 150, 100); // Título del juego
    }
}
