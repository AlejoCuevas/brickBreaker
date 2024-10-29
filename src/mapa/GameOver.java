package mapa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOver extends JPanel implements ActionListener {
    private final JFrame frame;
    private final int score;

    public GameOver(JFrame frame, int score) {
        this.frame = frame;
        this.score = score;

        setLayout(new BorderLayout());
        JLabel messageLabel = new JLabel("Game Over", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 50));
        messageLabel.setForeground(Color.RED);
        add(messageLabel, BorderLayout.CENTER);

        JLabel scoreLabel = new JLabel("Final Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(scoreLabel, BorderLayout.SOUTH);

        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(this);
        add(retryButton, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
        Main.main(null); // Reiniciar el juego al presionar "Retry"
    }
}
