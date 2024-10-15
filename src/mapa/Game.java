package mapa;

import ball.Ball;
import brick.Brick;
import generador.Generador;
import paddle.Paddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, ActionListener {

    private Timer timer;
    private Ball ball;
    private Paddle paddle;
    private Generador mapa;
    private Boolean play = false;
    private GraphicsDevice graphicsDevice;

    public Game () {


        paddle = new Paddle(0,0);

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        int paddleStartX = (screenWidth - paddle.getWidth()) / 2;
        int paddleStartY = (screenHeight - 108);

        ball = new Ball(500, 300);
        paddle = new Paddle(paddleStartX, paddleStartY);
        mapa = new Generador(5);


    addKeyListener(this);
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);

    timer = new Timer(8, this);
    timer.start();

    setFullscreen();
    }

    private void setFullscreen() {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = ge.getDefaultScreenDevice();

        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);

        graphicsDevice.setFullScreenWindow(frame);
        frame.setResizable(false);

    }

    public void paint(Graphics g) {
        // Fondo
        g.setColor(Color.BLUE);
        g.fillRect(1, 1, getWidth(), getHeight());

        // Dibujar componentes
        ball.draw(g);
        paddle.draw(g);
        mapa.draw(g);

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            // Lógica para mover la bola y detectar colisiones
            ball.mover();

            // Detectar colisiones con los bordes
            if (ball.getX() < 0 || ball.getX() > getWidth()) {
                ball.reverseXDir();
            }
            if (ball.getY() < 0) {
                ball.reverseYDir();
            }

            // Detectar colisión con la pala
            if (new Rectangle(ball.getX(), ball.getY(), ball.getDiametro(), ball.getDiametro())
                    .intersects(new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight()))) {
                ball.reverseYDir();
            }

            for (int i = 0; i < mapa.getMapa().length; i++) {
                for (int j = 0; j < mapa.getMapa()[0].length; j++) {
                    Brick brick = mapa.getMapa()[i][j];
                    if (brick.isVisible() &&
                            new Rectangle(ball.getX(), ball.getY(), ball.getDiametro(), ball.getDiametro())
                                    .intersects(new Rectangle(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight()))) {

                        // Eliminar el ladrillo
                        brick.setVisible(false);
                        // Invertir la dirección de la pelota
                        ball.reverseYDir();
                        // Aumentar velocidad de la pelota
                        ball.aumentarVelocidad();
                        break; // Rompe el bucle para evitar múltiples colisiones en un solo movimiento
                    }
                }
            }

            repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddle.moverIzquierda();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.moverDerecha();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            play = true; // Iniciar el juego
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new Game();
    }

}

