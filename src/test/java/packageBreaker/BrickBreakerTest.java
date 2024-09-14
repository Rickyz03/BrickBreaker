////////////////////////////////////////////////////////////////////
// Riccardo Stefani
////////////////////////////////////////////////////////////////////

package packageBreaker;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.KeyEvent;

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
        verify(g).drawString("0", 590, 30); // Il punteggio iniziale è 0
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
        brickBreaker.paint(g);

        // Verifica che la pallina venga disegnata
        verify(g, atLeastOnce()).setColor(Color.yellow); // Il colore giallo potrebbe essere usato anche altrove
        verify(g).fillOval(brickBreaker.ballPosX, brickBreaker.ballPosY, 20, 20); // Pallina
    }

    @Test
    public void testPaintGameOver() {
        // Simula la condizione di game over
        brickBreaker.ballPosY = 580;
        brickBreaker.paint(g);

        // Verifica che venga mostrato il messaggio di Game Over
        verify(g, atLeastOnce()).setColor(Color.red);
        verify(g, atLeastOnce()).setFont(new Font("serif", Font.BOLD, 30));
        verify(g).drawString("Game Over, Scores: 0", 190, 300); // Il punteggio è ancora 0
        verify(g, atLeastOnce()).setFont(new Font("serif", Font.BOLD, 20));
        verify(g).drawString("Press Enter to Restart", 230, 350);
    }

    @Test
    public void testPaintYouWon() {
        // Simula la vittoria
        brickBreaker.totalBricks = 0;
        brickBreaker.paint(g);

        // Verifica che venga mostrato il messaggio di vittoria
        verify(g, atLeastOnce()).setColor(Color.red);
        verify(g, atLeastOnce()).setFont(new Font("serif", Font.BOLD, 30));
        verify(g).drawString("You Won", 260, 300);
        verify(g, atLeastOnce()).setFont(new Font("serif", Font.BOLD, 20));
        verify(g).drawString("Press Enter to Restart", 230, 350);
    }

    @Test
    public void testMoveRight() {
        int initialPlayerX = brickBreaker.playerX;
        brickBreaker.moveRight();
        assertTrue(brickBreaker.play);
        assertEquals(initialPlayerX + 20, brickBreaker.playerX);
    }

    @Test
    public void testMoveLeft() {
        int initialPlayerX = brickBreaker.playerX;
        brickBreaker.moveLeft();
        assertTrue(brickBreaker.play);
        assertEquals(initialPlayerX - 20, brickBreaker.playerX);
    }

    @Test
    public void testKeyPressRight() {
        KeyEvent rightKey = new KeyEvent(brickBreaker, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, 'D');
        brickBreaker.keyPressed(rightKey);
        assertTrue(brickBreaker.play);
    }

    @Test
    public void testKeyPressLeft() {
        KeyEvent leftKey = new KeyEvent(brickBreaker, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, 'A');
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

