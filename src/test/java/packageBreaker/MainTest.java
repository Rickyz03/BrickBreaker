////////////////////////////////////////////////////////////////////
// Riccardo Stefani
////////////////////////////////////////////////////////////////////

package packageBreaker;

import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainTest {
    @Test
    public void testMainIstantiation(){
        // Verifica che la classe App possa essere istanziata correttamente
        Main main = new Main();
        assertNotNull(main);
    }

    @Test
    public void testMain() {
        // Esegui il metodo main e ottieni il JFrame
        JFrame frame = Main.createFrame();

        // Verifica le proprietà del JFrame
        assertEquals(10, frame.getBounds().x);
        assertEquals(10, frame.getBounds().y);
        assertEquals(700, frame.getBounds().width);
        assertEquals(600, frame.getBounds().height);
        assertEquals("Brick Breaker", frame.getTitle());
        assertFalse(frame.isResizable());
        assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());

        // Verifica che il BrickBreaker sia stato aggiunto al JFrame
        assertTrue(frame.getContentPane().getComponent(0) instanceof BrickBreaker);

        // Facoltativo: Verifica se il JFrame è visibile
        assertTrue(frame.isVisible());
    }
}
