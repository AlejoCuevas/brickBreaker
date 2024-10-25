package gameover;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOver extends JPanel implements ActionListener {

    private JButton restartButton;
    private JButton exitButton;
    private ActionListener restartGameListener;

    public GameOver(ActionListener restartGameListener) {
        this.restartGameListener = restartGameListener;

        setLayout(new BorderLayout()); // Usa BorderLayout para ocupar todo el espacio

        restartButton = new JButton("Reiniciar");
        exitButton = new JButton("Salir");

        // Configurar el panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Usa FlowLayout para centrar los botones
        buttonPanel.setOpaque(false); // Fondo transparente
        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);

        // Añadir acción a los botones
        restartButton.addActionListener(this);
        exitButton.addActionListener(this);

        add(buttonPanel, BorderLayout.CENTER); // Añadir el panel de botones al centro
        setBackground(Color.BLACK); // Fondo negro
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            restartGameListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "restartGame"));
        } else if (e.getSource() == exitButton) {
            System.exit(0); // Salir del juego
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Personalizar el fondo de la pantalla de Game Over
        g.setColor(Color.RED);
        g.fillRect(0, 0, getWidth(), getHeight()); // Fondo rojo
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("GAME OVER", getWidth() / 2 - 150, 100); // Mensaje de Game Over
    }
}
