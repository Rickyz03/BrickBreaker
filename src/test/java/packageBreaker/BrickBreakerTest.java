////////////////////////////////////////////////////////////////////
// Riccardo Stefani
////////////////////////////////////////////////////////////////////

package packageBreaker;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;

public class BrickBreakerTest {

    private BrickBreaker brickBreaker;
    private Graphics2D g;

    @Before
    public void setUp() {
        // Questo metodo viene eseguito prima di ogni test
        brickBreaker = new BrickBreaker();
        g = mock(Graphics2D.class); // Mock dell'oggetto Graphics2D
    }

    @Test
    public void testStartGame() {
        JFrame frame = BrickBreaker.startGame();

        assertNotNull("Il frame non dovrebbe essere null", frame);

        assertEquals("Il titolo del frame dovrebbe essere 'Brick Breaker'", "Brick Breaker", frame.getTitle());

        assertEquals("La larghezza del frame dovrebbe essere 700", 700, frame.getWidth());
        assertEquals("L'altezza del frame dovrebbe essere 600", 600, frame.getHeight());

        assertFalse("Il frame non dovrebbe essere ridimensionabile", frame.isResizable());

        assertTrue("Il frame dovrebbe essere visibile", frame.isVisible());

        assertEquals("Il default close operation dovrebbe essere EXIT_ON_CLOSE", JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
    }

    @Test
    public void testInitialConditions() {
        assertFalse(brickBreaker.play);
        assertEquals(0, brickBreaker.score);
        assertEquals(21, brickBreaker.totalBricks);
    }

    @Test
    public void testPaintBackground() {
        brickBreaker.paint(g);

        // Verifica che il background venga impostato come nero
        verify(g, atLeastOnce()).setColor(Color.black);
        verify(g).fillRect(1, 1, 692, 592);
    }

    @Test
    public void testPaintBorders() {
        brickBreaker.paint(g);

        // Verifica che i bordi vengano disegnati in giallo
        verify(g, atLeastOnce()).setColor(Color.yellow); // Può essere invocato più volte
        verify(g, times(1)).fillRect(0, 0, 3, 592);
        verify(g, times(1)).fillRect(0, 0, 692, 3);
        verify(g, times(1)).fillRect(691, 0, 3, 592);
    }

    @Test
    public void testPaintScore() {
        brickBreaker.paint(g);

        // Verifica che il punteggio venga disegnato
        verify(g, atLeastOnce()).setColor(Color.white); // Il colore bianco può essere impostato più volte
        verify(g).setFont(new Font("serif", Font.BOLD, 25));
        verify(g).drawString("0", 587, 30); // Il punteggio iniziale è 0
    }

    @Test
    public void testPaintPaddle() {
        brickBreaker.paint(g);

        // Verifica che il paddle venga disegnato
        verify(g, atLeastOnce()).setColor(Color.green);
        verify(g).fillRect(brickBreaker.playerX, 550, 100, 8); // Paddle
    }

    @Test
    public void testPaintBall() {
        brickBreaker.play = true; // the ball will be drawn only if play is true
        brickBreaker.paint(g);

        // Verifica che la pallina venga disegnata
        verify(g, atLeastOnce()).setColor(Color.yellow); // Il colore giallo potrebbe essere usato anche altrove
        verify(g).fillOval(brickBreaker.ballPosX, brickBreaker.ballPosY, 20, 20); // Pallina
    }

    @Test
    public void testResetBallPositionAndDirection() {
        // Esegui il reset della posizione e della direzione della pallina
        brickBreaker.resetBallPositionAndDirection();

        assertTrue("La posizione X della pallina dovrebbe essere tra 20 e 670",
                brickBreaker.ballPosX >= 20 && brickBreaker.ballPosX <= 670);

        assertEquals("La posizione Y della pallina dovrebbe essere fissa a 350",
                350, brickBreaker.ballPosY);

        assertTrue("La direzione X della pallina dovrebbe essere -1 o 1",
                brickBreaker.ballXDir == -1 || brickBreaker.ballXDir == 1);

        assertEquals("La direzione Y della pallina dovrebbe essere -2",
                -2, brickBreaker.ballYDir);
    }

    @Test
    public void testBallIntersectsBrick() {
        // Supponiamo che il primo blocco sia visibile e abbia un valore
        int initialScore = brickBreaker.score;
        int initialTotalBricks = brickBreaker.totalBricks;

        // Piazza play a true e fissa le direzioni della pallina
        brickBreaker.play = true;
        brickBreaker.resetBallPositionAndDirection();
        int inizialDirX = brickBreaker.ballXDir;
        int inizialDirY = brickBreaker.ballYDir;

        // Posiziona la pallina in modo che intersechi il primo blocco
        brickBreaker.ballPosX = 80 + 1; // Imposta X della pallina a sinistra del blocco
        brickBreaker.ballPosY = 50 + 1; // Imposta Y della pallina sopra il blocco

        // Simula l'azione
        ActionEvent event = new ActionEvent(brickBreaker, ActionEvent.ACTION_PERFORMED, "test");
        brickBreaker.actionPerformed(event);

        // Verifica che il punteggio sia stato aggiornato e il blocco sia stato rimosso
        assertTrue("Score should be increased", brickBreaker.score > initialScore);
        assertEquals("Total bricks should be decreased by 1", initialTotalBricks - 1, brickBreaker.totalBricks);

        // Verifica la direzione della pallina
        assertEquals("Ball X direction should be the same", inizialDirX, brickBreaker.ballXDir);
        assertEquals("Ball Y direction should be reversed", -inizialDirY, brickBreaker.ballYDir);

        // Verifica che il blocco sia stato rimosso
        assertEquals("Brick should be removed (value should be 0)", 0, brickBreaker.map.map[0][0]);
    }

    @Test
    public void testEnterKeyPressed_FirstMatch() {
        MapGenerator originalMap = brickBreaker.map; // Mappa originale

        // Simula la pressione del tasto ENTER
        KeyEvent enterKey = new KeyEvent(brickBreaker, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        brickBreaker.keyPressed(enterKey);

        assertTrue("The game should start when ENTER is pressed.", brickBreaker.play);

        assertEquals("Player should be repositioned at the center.", 300, brickBreaker.playerX);

        assertEquals("Score should be reset to 0.", 0, brickBreaker.score);

        assertEquals("Total bricks should be reset to 21.", 21, brickBreaker.totalBricks);

        assertNotEquals("Ball position X should be randomized.", 0, brickBreaker.ballPosX);
        assertEquals("Ball position Y should be fixed at 350.", 350, brickBreaker.ballPosY);

        assertFalse("isTheFirstMatch should be set to false after the first match.", brickBreaker.isTheFirstMatch);

        assertSame("The map should not be recreated on the first match.", originalMap, brickBreaker.map);
    }

    @Test
    public void testEnterKeyPressed_NotFirstMatch() {
        // Simula un secondo match
        brickBreaker.isTheFirstMatch = false;
        MapGenerator originalMap = brickBreaker.map; // Mappa originale

        // Simula la pressione del tasto ENTER
        KeyEvent enterKey = new KeyEvent(brickBreaker, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        brickBreaker.keyPressed(enterKey);

        assertNotSame("The map should be recreated after the first match.", originalMap, brickBreaker.map);
    }

    @Test
    public void testPaintGameOver() {
        // Simula la condizione di game over
        brickBreaker.ballPosY = 580;
        brickBreaker.paint(g);

        // Verifica che venga mostrato il messaggio di Game Over
        verify(g, atLeastOnce()).setColor(Color.red);
        verify(g, atLeastOnce()).setFont(new Font("serif", Font.BOLD, 30));
        verify(g).drawString("Game Over, Score: 0", 187, 300); // Il punteggio è ancora 0
        verify(g, atLeastOnce()).setColor(Color.white);
        verify(g, atLeastOnce()).setFont(new Font("serif", Font.BOLD, 20));
        verify(g).drawString("Press Enter to Restart", 240, 350);
    }

    @Test
    public void testPaintYouWon() {
        // Simula la vittoria
        brickBreaker.totalBricks = 0;
        brickBreaker.paint(g);

        // Verifica che venga mostrato il messaggio di vittoria
        verify(g, atLeastOnce()).setColor(Color.red);
        verify(g, atLeastOnce()).setFont(new Font("serif", Font.BOLD, 30));
        verify(g).drawString("You Won", 277, 300);
        verify(g, atLeastOnce()).setColor(Color.white);
        verify(g, atLeastOnce()).setFont(new Font("serif", Font.BOLD, 20));
        verify(g).drawString("Press Enter to Restart", 237, 350);
    }

    @Test
    public void testMoveRight() {
        int initialPlayerX = brickBreaker.playerX;
        brickBreaker.play = true; // you can move the player only if play is true
        brickBreaker.moveRight();
        assertTrue(brickBreaker.play);
        assertEquals(initialPlayerX + 20, brickBreaker.playerX);
    }

    @Test
    public void testMoveLeft() {
        int initialPlayerX = brickBreaker.playerX;
        brickBreaker.play = true; // you can move the player only if play is true
        brickBreaker.moveLeft();
        assertTrue(brickBreaker.play);
        assertEquals(initialPlayerX - 20, brickBreaker.playerX);
    }

    @Test
    public void testKeyPressRight() {
        KeyEvent rightKey = new KeyEvent(brickBreaker, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, 'D');
        brickBreaker.play = true; // you can move the player only if play is true
        brickBreaker.keyPressed(rightKey);
        assertTrue(brickBreaker.play);
    }

    @Test
    public void testKeyPressLeft() {
        KeyEvent leftKey = new KeyEvent(brickBreaker, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, 'A');
        brickBreaker.play = true; // you can move the player only if play is true
        brickBreaker.keyPressed(leftKey);
        assertTrue(brickBreaker.play);
    }

    @Test
    public void testGameOver() {
        brickBreaker.ballPosY = 580;
        brickBreaker.actionPerformed(null);
        assertFalse(brickBreaker.play);
    }

    @Test
    public void testWinCondition() {
        brickBreaker.totalBricks = 0;
        brickBreaker.actionPerformed(null);
        assertFalse(brickBreaker.play);
    }
}

