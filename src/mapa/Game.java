package mapa;

import ball.Ball;
import brick.Brick;
import generador.Generador;
import paddle.Paddle;
import menu.Menu;
import gameover.GameOver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, ActionListener {

    private Timer timer;
    private ArrayList<Ball> balls;
    private Paddle paddle;
    private Generador mapa;
    private boolean play = false;
    private GraphicsDevice graphicsDevice;

    private int score = 0; // Puntuación del jugador

    public Game() {
        balls = new ArrayList<>();
        balls.add(new Ball(500, 300)); // Añadir la primera pelota inicial

        paddle = new Paddle(0, 0);

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        int paddleStartX = (screenWidth - paddle.getWidth()) / 2;
        int paddleStartY = (screenHeight - 108);

        paddle = new Paddle(paddleStartX, paddleStartY);
        mapa = new Generador(5);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addKeyListener(this);

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


    @Override
    public void paint(Graphics g) {
        super.paint(g);
            // Fondo
            g.setColor(Color.BLUE);
            g.fillRect(1, 1, getWidth(), getHeight());
            // Dibujar componentes
            for (Ball ball : balls) {
                ball.draw(g);
            }
            paddle.draw(g);
            mapa.draw(g);

            // Mostrar puntuación y vidas
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Puntuación: " + score, 20, 30);


    }
        @Override
        public void actionPerformed (ActionEvent e){
            if (play) {
                ArrayList<Ball> newBalls = new ArrayList<>();

                for (Ball ball : balls) {
                    ball.mover();

                    // Detectar colisiones con los bordes
                    if (ball.getX() < 0 || ball.getX() > getWidth()) {
                        ball.reverseXDir();
                    }
                    if (ball.getY() < 0) {
                        ball.reverseYDir();
                    }

                    // Detectar colisión con la pala
                    Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getDiametro(), ball.getDiametro());
                    Rectangle paddleRect = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

                    if (ballRect.intersects(paddleRect)) {
                        ball.reverseYDir(); // Invertir dirección vertical
                        int paddleCenter = paddle.getX() + paddle.getWidth() / 2;
                        int impactPoint = ball.getX() + ball.getDiametro() / 2;
                        int distanceFromCenter = impactPoint - paddleCenter;
                        double angleFactor = 0.05; // Controla cuánto afecta el impacto al ángulo
                        ball.setxDir((int) (distanceFromCenter * angleFactor));
                        ball.aumentarVelocidad();
                    }

                    // Detectar colisión con ladrillos
                    boolean hitBrick = false;
                    for (int i = 0; i < mapa.getMapa().length; i++) {
                        for (int j = 0; j < mapa.getMapa()[0].length; j++) {
                            Brick brick = mapa.getMapa()[i][j];
                            if (brick != null && brick.isVisible() && ballRect.intersects(new Rectangle(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight()))) {
                                if (!brick.esIndestructible()) {
                                    brick.setVisible(false);
                                    score += 10; // Incrementar puntuación
                                    ball.reverseYDir();
                                    ball.aumentarVelocidad();
                                    newBalls.add(new Ball(ball.getX(), ball.getY()));
                                    hitBrick = true; // Marcamos que hemos golpeado un ladrillo
                                } else {
                                    ball.reverseYDir(); // Rebota sin eliminar si es indestructible
                                }
                                break;
                            }
                        }
                    }
                }

                // Añadir nuevas pelotas creadas al romper bloques
                balls.addAll(newBalls);

                repaint();
            }
        }


        public void keyPressed (KeyEvent e){
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                paddle.moverIzquierda();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                paddle.moverDerecha();
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE && !play) {
                play = true;
            }
        }


        @Override
        public void keyReleased (KeyEvent e){}

        @Override
        public void keyTyped (KeyEvent e){}

        public static void main (String[]args){
            new Game();
        }
}


