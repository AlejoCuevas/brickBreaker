package mapa;

import ball.Ball;
import ball.CannonBall;
import brick.Brick;
import generador.Generador;
import paddle.Paddle;
import powerup.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    private Timer ralentizarTiempo;
    private boolean tiempoRalentizado = false;

    private CannonBall cannonBall;
    private boolean cannonBallActive = false;


    public Game() {
        powerUps = new ArrayList<>();
        extraPaddle = null;
        balls = new ArrayList<>();
        balls.add(new Ball(500, 300)); // Añadir la primera pelota inicial

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        int paddleStartX = (screenWidth - Paddle.WIDTH) / 2;
        int paddleStartY = (screenHeight - 108);

        paddle = new Paddle(paddleStartX, paddleStartY);
        mapa = new Generador(5);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        timer = new Timer(8, this);
        timer.start();


        ralentizarTiempo = new Timer(5000, e -> resetTime()); // Ralentiza por 5 segundos!



        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (play) {
                    PowerUp remocionDeBloques = powerUps.stream()
                            .filter(powerUp -> powerUp instanceof RemocionDeBloques && powerUp.esActivo() && !((RemocionDeBloques) powerUp).esUsado())
                            .findFirst()
                            .orElse(null);

                    if (remocionDeBloques != null) {
                        eliminarBloque(e.getX(), e.getY());
                        remocionDeBloques.activar(); // Activar el power-up y marcarlo como usado
                    }
                }
            }
        });

    }

    public void setFullscreen(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = ge.getDefaultScreenDevice();

        frame.setUndecorated(true);
        graphicsDevice.setFullScreenWindow(frame); // Cambiar a pantalla completa
        frame.setVisible(true); // Hacerlo visible
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
        if (cannonBallActive && cannonBall != null) {
            cannonBall.draw(g); // Dibuja la CannonBall
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

                if (cannonBallActive && cannonBall != null) {
                    // Mover la bola del cañón si está activa
                    cannonBall.mover();
                    if (cannonBall.getY() < 0) {
                        cannonBallActive = false; // Si la bola sale de la pantalla, desactivarla
                        cannonBall = null; // Limpiar la referencia
                    }
                }

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
                                if (Math.random() < 0.3) {
                                    powerUps.add(generarPowerUp(ball.getX(), ball.getY()));
                                }
                            } else {
                                ball.reverseYDir(); // Rebota sin eliminar si es indestructible
                            }

                            break; // Salir del bucle de ladrillos, ya que se ha manejado la colisión
                        }
                    }
                }
            }

            if (extraPaddle != null) {
                extraPaddle.setX(paddle.getX() + paddle.getWidth() + 5); // Ajuste de posición sincronizado
            }

            // Añadir nuevas pelotas creadas al romper bloques
            balls.addAll(newBalls);

            for (PowerUp powerUp : powerUps) {
                if (powerUp.esActivo()) {
                    powerUp.moverAbajo();
                    Rectangle paddleRect = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

                    if (powerUp.getBounds().intersects(paddleRect)) {
                        powerUp.desactivar();
                        activarPowerUp(powerUp);
                    }
                }
            }





            repaint();
        }
    }

    private PowerUp generarPowerUp(int x, int y) {
        int powerUpType = (int) (Math.random() * 5); // Suponiendo que tienes 4 tipos de power-ups

        switch (powerUpType) {
            case 0:
                return new RemocionDeBloques(x, y);
            case 1:
                return new AtraccionMagnetica(x, y);
            case 2:
                return new CanonDeRebote(x, y);
            case 3:
                return new RalentizarTiempo(x, y);
            case 4: // Nuevo caso para el ExtraPaddle
                return new ExtraPaddle(x, y);
            default:
                return null; // No debería llegar aquí
        }
    }

    private void activarPowerUp(PowerUp powerUp) {
        if (powerUp instanceof ExtraPaddle) {
            if (extraPaddle == null) {
                extraPaddle = new Paddle(paddle.getX() + paddle.getWidth(), paddle.getY());
                System.out.println("¡Pala extra activada!");
            }
        }
        if (powerUp instanceof RemocionDeBloques) {
            // Implementar lógica de Remoción de Bloques
        } else if (powerUp instanceof AtraccionMagnetica) {
            cannonBall = new CannonBall(paddle.getX() + (paddle.getWidth() / 2) - (CannonBall.DIAMETRO / 2), paddle.getY() - CannonBall.DIAMETRO);
            cannonBallActive = false; // Mantenerla inactiva al principio
            System.out.println("Activando Atracción Magnética.");
        } else if (powerUp instanceof CanonDeRebote) {
            // Implementar lógica de Cañón de Rebote
        } else if (powerUp instanceof RalentizarTiempo) {
            activarRalentizarTiempo();
            // Implementar lógica de Ralentizar el tiempo
        }
        powerUp.activar(); // Activar el power-up
    }

    private void eliminarBloque(int x, int y) {
        for (int i = 0; i < mapa.getMapa().length; i++) {
            for (int j = 0; j < mapa.getMapa()[0].length; j++) {
                Brick brick = mapa.getMapa()[i][j];
                if (brick != null && brick.esVisible() && brick.getBounds().contains(x, y)) {
                    brick.setVisible(false); // Eliminar el ladrillo
                    System.out.println("Bloque eliminado por clic: " + brick.getX() + ", " + brick.getY());
                    score += 10; // Incrementar puntuación
                    return; // Salir después de eliminar un bloque
                }
            }
        }
    }

    private void activarRalentizarTiempo() {
        if (!tiempoRalentizado) {
            System.out.println("Activando Ralentizar Tiempo.");
            tiempoRalentizado = true;
            timer.setDelay(16); // Reducir velocidad a la mitad
            ralentizarTiempo.start(); // Iniciar el temporizador para restaurar
        }
    }

    private void resetTime() {
        System.out.println("Restaurando velocidad normal.");
        tiempoRalentizado = false;
        timer.setDelay(8); // Restaurar velocidad normal
        ralentizarTiempo.stop(); // Detener el temporizador
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
            // Disparar la bola del cañón si está activa
            if (cannonBallActive) {
                cannonBall.disparar(); // Método para iniciar el movimiento hacia arriba
                cannonBallActive = true; // Activar el cañón cuando el jugador presiona la barra espaciadora
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}





}