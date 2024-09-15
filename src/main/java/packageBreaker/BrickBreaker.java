////////////////////////////////////////////////////////////////////
// Riccardo Stefani
////////////////////////////////////////////////////////////////////

package packageBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.Collections;

public class BrickBreaker extends JPanel implements KeyListener, ActionListener {
    boolean play = false;
    boolean isTheFirstMatch = true; // Variabile per distinguere la prima partita
    int score = 0;
    int totalBricks = 21;
    Timer timer;
    int delay = 8;
    int playerX = 300;
    int ballPosX;
    int ballPosY;
    int ballXDir;
    int ballYDir;
    MapGenerator map;
    SecureRandom secureRandom;

    public BrickBreaker() {
        map = new MapGenerator(3, 7);
        secureRandom = new SecureRandom();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    // Metodo per sorteggiare la posizione della pallina
    void resetBallPositionAndDirection() {
        ballPosX = secureRandom.nextInt(670 - 20) + 20;  // Sorteggia la posizione orizzontale
        ballPosY = 350;  // Verticale fissa

        // Sorteggia una direzione casuale tra due opzioni
        double randomValue = secureRandom.nextDouble();  // Genera un valore casuale tra 0.0 e 1.0

        if (randomValue < 0.5) {
            ballXDir = -1;  // Alto sinistra
        } else {
            ballXDir = 1;   // Alto destra
        }
        ballYDir = -2;
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Drawing map
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 587, 30);

        // The paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // The ball (only when the game starts)
        if (play) {
            g.setColor(Color.yellow);
            g.fillOval(ballPosX, ballPosY, 20, 20);
        }

        // Messaggio di istruzioni all'inizio
        if (isTheFirstMatch) {
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter to start", 200, 300);

            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Use the left and right arrows", 203, 350);
            g.drawString("to move the paddle", 250, 380);
        }

        // Condizioni di vittoria
        if (totalBricks <= 0) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won", 277, 300);

            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 237, 350);
        }

        // Condizioni di game over
        if (ballPosY > 570) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 187, 300);

            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 240, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDir = -ballYDir;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(brickRect)) {
                            int brickValue = map.blockValues[i][j]; // Ottieni il punteggio del blocco colpito
                            map.setBrickValue(0, i, j); // Rimuovi il blocco
                            totalBricks--;
                            score += brickValue; // Incrementa il punteggio in base al valore del blocco

                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballXDir = -ballXDir;
                            } else {
                                ballYDir = -ballYDir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXDir;
            ballPosY += ballYDir;
            if (ballPosX < 0) {
                ballXDir = -ballXDir;
            }
            if (ballPosY < 0) {
                ballYDir = -ballYDir;
            }
            if (ballPosX > 670) {
                ballXDir = -ballXDir;
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int leftBoundary = 3; // La linea gialla sul bordo sinistro
        int rightBoundary = 692 - 3 - 100; // La larghezza della finestra meno la linea gialla e la larghezza del player

        if (play) { // Permetti il movimento solo se il gioco è attivo
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (playerX >= rightBoundary) {
                    playerX = rightBoundary; // Impedisci di andare oltre il limite destro
                } else {
                    moveRight();
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (playerX <= leftBoundary) {
                    playerX = leftBoundary; // Impedisci di andare oltre il limite sinistro
                } else {
                    moveLeft();
                }
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                playerX = 300; // Riposiziona il player al centro
                score = 0;
                totalBricks = 21;

                // Sorteggia la posizione e la direzione della pallina
                resetBallPositionAndDirection();

                // Se è il primo match, usa la mappa già creata
                if (isTheFirstMatch) {
                    isTheFirstMatch = false; // La prima partita è iniziata
                } else {
                    // Se non è il primo match, crea una nuova mappa
                    map = new MapGenerator(3, 7);
                }

                repaint();
            }
        }
    }

    public void moveRight() {
        if (play) { // Permetti il movimento solo se il gioco è attivo
            playerX += 20;
        }
    }

    public void moveLeft() {
        if (play) { // Permetti il movimento solo se il gioco è attivo
            playerX -= 20;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {} // Necessario l'override per poter implementare l'interfaccia java.awt.event.KeyListener

    @Override
    public void keyTyped(KeyEvent e) {} // Necessario l'override per poter implementare l'interfaccia java.awt.event.KeyListener

    public static JFrame startGame() {
        JFrame frame = new JFrame();
        BrickBreaker gamePlay = new BrickBreaker();

        frame.setBounds(10, 10, 700, 600);
        frame.setTitle("Brick Breaker");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePlay);
        frame.setVisible(true);
        return frame; // Restituisce il JFrame frame al main JFrame
    }

    public static void main(String[] args) {
        startGame(); // Chiama la funzione startGame
    }

}


class MapGenerator {
    public int[][] map; // Mappa dei blocchi (0 per assente, valore del punteggio per presente)
    public int[][] blockValues; // Punteggi dei blocchi
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        blockValues = new int[row][col];
        ArrayList<Integer> shuffledValues = new ArrayList<>();

        // Aggiungi i punteggi fissi alla lista
        // Punteggi fissi per i blocchi
        int[] fixedBlockValues = {
                5, 10, 15, 20, 25, 25, 20, 15, 10, 5,
                5, 10, 15, 20, 25, 25, 20, 15, 5, 5, 5
        };
        for (int value : fixedBlockValues) {
            shuffledValues.add(value);
        }

        // Mischia i punteggi
        Collections.shuffle(shuffledValues);

        // Assegna i valori casualmente ai blocchi
        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = 1; // 1 indica che il blocco è presente
                blockValues[i][j] = shuffledValues.get(index); // Assegna il punteggio casuale
                index++;
            }
        }

        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    int brickValue = blockValues[i][j];
                    Color brickColor = getColorForValue(brickValue); // Ottieni il colore in base al punteggio
                    g.setColor(brickColor);
                    g.fillRect(j * brickWidth + 77, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 77, i * brickHeight + 50, brickWidth, brickHeight);

                    // Scrivi il punteggio sopra il blocco
                    g.setColor(Color.black); // Cambia il colore del testo in nero
                    g.setFont(new Font("serif", Font.BOLD, 20));
                    g.drawString("" + brickValue, j * brickWidth + 77 + brickWidth / 2 - 10, i * brickHeight + 50 + brickHeight / 2 + 5);
                }
            }
        }
    }

    // Metodo per ottenere il colore in base al punteggio
    private Color getColorForValue(int value) {
        switch (value) {
            case 25:
                return Color.red;
            case 20:
                return Color.orange;
            case 15:
                return Color.yellow;
            case 10:
                return Color.green;
            case 5:
                return Color.blue;
            default:
                return Color.white; // Default se il valore non è previsto
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}
