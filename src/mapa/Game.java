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


    private JFrame frame;

    private int score = 0; // Puntuación del jugador

    private Timer ralentizarTiempo;
    private boolean tiempoRalentizado = false;

    private CannonBall cannonBall;
    private boolean cannonBallActive = false;
    boolean isPaused = false;

    public Game(JFrame frame, int nivel) {
        this.frame = frame;
        powerUps = new ArrayList<>();
        extraPaddle = null;
        balls = new ArrayList<>();

        // Posición inicial de la pelota
        int ballStartY;
        if (nivel == 3 || nivel == 4) {
            ballStartY = 600;
        } else {
            ballStartY = 300; // Valor original para niveles 1 y 2
        }
        balls.add(new Ball(500, ballStartY)); // Añadir la primera pelota inicial

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        int paddleStartX = (screenWidth - Paddle.WIDTH) / 2;
        int paddleStartY = (screenHeight - 108); // Posicion de la paleta

        paddle = new Paddle(paddleStartX, paddleStartY);
        mapa = new Generador(nivel);

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




    @Override
    public void paint(Graphics g) {
        super.paint(g); // Llama a paint de JPanel para limpiar

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
            ArrayList<Ball> newBalls = new ArrayList<>(); // Almacena pelotas que despues se van a generar
            for (int i = 0; i < balls.size(); i++) {
                Ball ball = balls.get(i);
                ball.mover();

                if (ball.getY() > getHeight()) {
                    System.out.println("Pelota fuera de la pantalla. Eliminando la pelota.");
                    balls.remove(i); // Eliminar la pelota de la lista
                    i--;
                    continue; // Ir a la siguiente pelota
                }

                // Detectar colisiones con los bordes
                if (ball.getX() < 0 || ball.getX() > getWidth()) {
                    ball.reverseXDir();
                }
                if (ball.getY() < 0) {
                    ball.reverseYDir();
                }

                // Detectar colisión con la pala "hitbox"
                Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getDiametro(), ball.getDiametro());
                Rectangle paddleRect = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

                if (ballRect.intersects(paddleRect)) {
                    controlarColision(ball, paddle);
                }

                // Detectar colisión con la pala extra "hitbox"
                if (extraPaddle != null) {
                    Rectangle extraPaddleRect = new Rectangle(extraPaddle.getX(), extraPaddle.getY(), extraPaddle.getWidth(), extraPaddle.getHeight());
                    if (ballRect.intersects(extraPaddleRect)) {
                        controlarColision(ball, extraPaddle);
                    }
                }

                // Detectar colisión con ladrillos
                for (int j = 0; j < mapa.getMapa().length; j++) {
                    for (int k = 0; k < mapa.getMapa()[0].length; k++) {
                        Brick brick = mapa.getMapa()[j][k];
                        if (brick != null && brick.esVisible() && ballRect.intersects(new Rectangle(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight()))) {
                            if (!brick.esIndestructible()) {
                                brick.setVisible(false); // Eliminar ladrillo
                                System.out.println("Bloque eliminado: " + brick.getX() + ", " + brick.getY());
                                score += 10; // Incrementar puntuación
                                ball.reverseYDir(); // Cambiar dirección de la bola
                                ball.aumentarVelocidad(); // Aumentar velocidad

                                // Generar nueva pelota con una probabilidad del 25%
                                if (Math.random() < 0.50) { // 25% de probabilidad
                                    newBalls.add(new Ball(ball.getX(), ball.getY())); // Crear nueva pelota
                                }

                                // Generar power-up con probabilidad de 30%
                                if (Math.random() < 1) {
                                    powerUps.add(generarPowerUp(ball.getX(), ball.getY()));
                                }
                            } else {
                                // Si es indestructible, registrar el impacto
                                brick.impactar(i);
                                ball.reverseYDir(); // Rebota sin eliminar si es indestructible
                            }

                            break; // Salir del bucle de ladrillos, ya que se ha manejado la colisión
                        }
                    }
                }
            }

            // posición sincronizada para la pala extra
            if (extraPaddle != null) {
                extraPaddle.setX(paddle.getX() + paddle.getWidth() + 5);
            }

            // Añadir nuevas pelotas creadas al romper bloques
            balls.addAll(newBalls);

            // Manejo de power-ups
            for (PowerUp powerUp : powerUps) {
                if (powerUp.esActivo()) {
                    powerUp.moverAbajo();
                    Rectangle paddleRect = new Rectangle(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

            // Colision de powerup con pala 1
                    if (powerUp.getBounds().intersects(paddleRect)) {
                        powerUp.desactivar();
                        activarPowerUp(powerUp);
                    }
            //Colision de powerup con pala 2
                    if (extraPaddle != null) {
                        Rectangle extraPaddleRect = new Rectangle(extraPaddle.getX(), extraPaddle.getY(), extraPaddle.getWidth(), extraPaddle.getHeight());
                        if (powerUp.getBounds().intersects(extraPaddleRect)) {
                            powerUp.desactivar();
                            activarPowerUp(powerUp);
                        }
                    }
                }
            }

            // Comprobar si no quedan bloques rojos
            boolean allRedBlocksDestroyed = mapa.noQuedanBloquesRojos();
            if (allRedBlocksDestroyed) {
                regresarAlMenu(); // regresa al menu
            }

            // Comprobar si no quedan pelotas
            if (balls.isEmpty()) {
                System.out.println("No quedan pelotas. Regresando al menú.");
                regresarAlMenu(); // Regresar al menú principal
            }

            repaint();
        }
    }


    private void regresarAlMenu() {
        frame.remove(this); // Elimina el juego actual
        frame.add(new Menu()); // Agrega el menú principal
        frame.revalidate();
        frame.repaint();
        timer.stop();
        play = false; // Detiene el juego
    }

    private PowerUp generarPowerUp(int x, int y) {
        int powerUpType = (int) (Math.random() * 5); // El valor cambia dependiendo la cantidad de PowerUps

        switch (powerUpType) {
            case 0:
                return new RemocionDeBloques(x, y);
            case 1:
                return new AtraccionMagnetica(x, y);
            case 2:
                return new FakePowerUp(x, y);
            case 3:
                return new RalentizarTiempo(x, y);
            case 4:
                return new ExtraPaddle(x, y);
            default:
                return null;
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

        } else if (powerUp instanceof AtraccionMagnetica) {
            // Nueva lógica para Atracción Magnética: Generar pelota en la parte inferior
            int screenWidth = getWidth();
            int ballStartX = screenWidth / 2; // Colocar la pelota en el centro de la pantalla
            int ballStartY = getHeight() - 20; // Justo encima del límite inferior

            Ball nuevaPelota = new Ball(ballStartX, ballStartY);
            nuevaPelota.setyDir(-3); // Disparar la pelota hacia arriba
            balls.add(nuevaPelota); // Añadir la nueva pelota a la lista
            ((AtraccionMagnetica) powerUp).activar(balls);
            System.out.println("Pelota disparada hacia arriba con Atracción Magnética.");
        } else if (powerUp instanceof FakePowerUp) {
            // lógica de Cañón de Rebote
        } else if (powerUp instanceof RalentizarTiempo) {
            activarRalentizarTiempo();
            // lógica de Ralentizar el tiempo
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
        double angleFactor = 0.10;
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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (isPaused) {
                timer.start();
                System.out.println("Juego reanudado");
            } else {
                timer.stop();
                System.out.println("Juego en pausa");
            }
            isPaused = !isPaused;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {



    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
}