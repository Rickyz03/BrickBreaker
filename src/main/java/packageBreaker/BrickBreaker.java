////////////////////////////////////////////////////////////////////
// Riccardo Stefani
////////////////////////////////////////////////////////////////////

package packageBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

public class BrickBreaker extends JPanel implements KeyListener, ActionListener {
    boolean play = false;
    boolean isTheFirstMatch = true; // Variabile per distinguere la prima partita
    int score = 0;
    int totalBricks = 21;
    Timer timer;
    int delay = 8;
    int playerX = 310;
    int ballPosX; // Ora sorteggiato all'inizio
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

        // Sorteggia la posizione iniziale e la direzione della pallina
        resetBallPositionAndDirection();
    }

    // Metodo per sorteggiare la posizione della pallina
    private void resetBallPositionAndDirection() {
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
        g.drawString("" + score, 590, 30);

        // The paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // The ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if (totalBricks <= 0) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        if (ballPosY > 570) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Scores: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
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
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

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
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                playerX = 310; // Riposiziona il player al centro
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                // Se è il primo match, usa la posizione già sorteggiata
                if (isTheFirstMatch) {
                    isTheFirstMatch = false; // La prima partita è iniziata
                } else {
                    // Se non è il primo match, sorteggia una nuova posizione per la pallina
                    resetBallPositionAndDirection();
                }

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame obj = new JFrame();
        BrickBreaker gamePlay = new BrickBreaker();
        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Brick Breaker");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
        obj.setVisible(true);
    }
}

class MapGenerator {
    public int[][] map;
    public int[][] brickValues;  // Array per memorizzare i valori dei blocchi
    public int brickWidth;
    public int brickHeight;
    private HashMap<Integer, Color> valueToColorMap;  // Mappa per associare i valori ai colori

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        brickValues = new int[row][col];  // Inizializza l'array per i valori

        // Definisci i multipli di 5 e assicurati che sommino a 500
        ArrayList<Integer> brickValueList = generateBrickValues(row * col, 500);

        // Mescola i valori per distribuirli casualmente
        Collections.shuffle(brickValueList);

        int index = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;  // Imposta il blocco come attivo
                brickValues[i][j] = brickValueList.get(index);  // Assegna un valore al blocco
                index++;
            }
        }

        // Mappa valori a colori
        valueToColorMap = new HashMap<>();
        valueToColorMap.put(5, Color.red);
        valueToColorMap.put(10, Color.blue);
        valueToColorMap.put(15, Color.green);
        valueToColorMap.put(20, Color.yellow);
        valueToColorMap.put(25, Color.orange);
        valueToColorMap.put(30, Color.pink);
        valueToColorMap.put(35, Color.cyan);
        valueToColorMap.put(40, Color.magenta);
        valueToColorMap.put(45, Color.gray);
        valueToColorMap.put(50, Color.white);

        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    // Genera una lista di valori casuali che sommano a 500
    private ArrayList<Integer> generateBrickValues(int numBricks, int totalValue) {
        ArrayList<Integer> values = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        // Aggiungi multipli di 5 casuali
        while (values.size() < numBricks - 1) {
            int value = (random.nextInt(10) + 1) * 5;  // Genera multipli di 5 tra 5 e 50
            values.add(value);
        }

        // Calcola il valore mancante per arrivare esattamente a 500
        int currentSum = values.stream().mapToInt(Integer::intValue).sum();
        int finalValue = totalValue - currentSum;
        if (finalValue > 0 && finalValue % 5 == 0) {
            values.add(finalValue);
        } else {
            // Se il valore non è valido, aggiusta uno dei valori
            int adjustment = finalValue - (finalValue % 5);
            values.add(adjustment);
        }

        return values;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // Ottieni il valore del blocco
                    int value = brickValues[i][j];

                    // Ottieni il colore corrispondente al valore
                    g.setColor(valueToColorMap.get(value));

                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}
