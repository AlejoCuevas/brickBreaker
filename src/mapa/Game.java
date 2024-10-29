package mapa;

import ball.Ball;
import brick.Brick;
import generador.Generador;
import paddle.Paddle;
import powerup.PowerUp;

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
    private ArrayList<PowerUp> powerUps;
    private Paddle extraPaddle;

    private int score = 0; // Puntuación del jugador

    public Game() {

        powerUps = new ArrayList<>();
        extraPaddle = null;
        balls = new ArrayList<>();
        balls.add(new Ball(500, 300)); // Añadir la primera pelota inicial

        paddle = new Paddle(0, 0);

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        int paddleStartX = (screenWidth - paddle.getWidth()) / 2;
        int paddleStartY = (screenHeight - 108);

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

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Llama a la implementación de paint de JPanel para limpiar el lienzo
            // Fondo
            g.setColor(Color.BLUE);
            g.fillRect(1, 1, getWidth(), getHeight());

            // Dibujar componentes
            for (Ball ball : balls) {
                ball.draw(g);
            }
            paddle.draw(g);
            mapa.draw(g);

        for (PowerUp powerUp : powerUps) {
            powerUp.draw(g);
        }
        if (extraPaddle != null) {
            extraPaddle.draw(g);
        }


            // Mostrar puntuación
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Puntuación: " + score, 20, 30);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
                    controlarColision(ball, paddle);
                }

                // Detectar colisión con la pala extra
                if (extraPaddle != null) {
                    Rectangle extraPaddleRect = new Rectangle(extraPaddle.getX(), extraPaddle.getY(), extraPaddle.getWidth(), extraPaddle.getHeight());
                    if (ballRect.intersects(extraPaddleRect)) {
                        controlarColision(ball, extraPaddle);
                    }
                }

                // Detectar colisión con ladrillos
                boolean hitBrick = false;
                for (int i = 0; i < mapa.getMapa().length; i++) {
                    for (int j = 0; j < mapa.getMapa()[0].length; j++) {
                        Brick brick = mapa.getMapa()[i][j];
                        if (brick != null && brick.esVisible() && ballRect.intersects(new Rectangle(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight()))) {
                            // Si no es indestructible, se elimina
                            if (!brick.esIndestructible()) {
                                brick.setVisible(false); // Eliminar ladrillo
                                System.out.println("Bloque eliminado: " + brick.getX() + ", " + brick.getY());
                                score += 10; // Incrementar puntuación
                                ball.reverseYDir(); // Cambiar dirección de la bola
                                ball.aumentarVelocidad(); // Aumentar velocidad
                                newBalls.add(new Ball(ball.getX(), ball.getY()));
                                hitBrick = true; // Marcar que se ha golpeado un ladrillo
                                // Generar power-up con probabilidad de 30%
                                if (hitBrick && Math.random() < 0.3) {
                                    powerUps.add(new PowerUp(ball.getX(), ball.getY()));
                                }
                            } else {
                                ball.reverseYDir(); // Rebota sin eliminar si es indestructible
                            }

                            break; // Salir del bucle de ladrillos, ya que se ha manejado la colisión
                        }
                    }
                }

            }



            // Añadir nuevas pelotas creadas al romper bloques
            balls.addAll(newBalls);

            for (PowerUp powerUp : powerUps) {
                if (powerUp.esActivo()) {
                    powerUp.moverAbajo();
                    Rectangle paddleRect = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

                    if (powerUp.getBounds().intersects(paddleRect)) {
                        powerUp.desactivar();
                        activarPowerUp();
                    }
                }
            }

            // Movimiento sincronizado de la pala extra
            if (extraPaddle != null) {
                extraPaddle.setX(paddle.getX() + paddle.getWidth() + 10); // Colocación a la derecha de la pala
            }

            repaint();
        }
    }

    private void activarPowerUp() {

        if(extraPaddle == null) {

            extraPaddle = new Paddle(paddle.getX() + paddle.getWidth(), paddle.getY());

        }

    }

    private void controlarColision(Ball ball, Paddle paddle) {
        ball.reverseYDir();
        int paddleCenter = paddle.getX() + paddle.getWidth() / 2;
        int impactPoint = ball.getX() + ball.getDiametro() / 2;
        int distanceFromCenter = impactPoint - paddleCenter;
        double angleFactor = 0.05;
        ball.setxDir((int) (distanceFromCenter * angleFactor));
        ball.aumentarVelocidad();

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