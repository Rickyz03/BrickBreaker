////////////////////////////////////////////////////////////////////
// Riccardo Stefani
////////////////////////////////////////////////////////////////////

package packageBreaker;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainTest {
    @Test
    public void testHelloWorld() {
        // Reindirizza l'output standard
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Esegui il metodo main
        App.main(new String[]{});

        // Verifica che l'output sia corretto
        assertEquals("Hello World!\n", outContent.toString());
    }

    @Test
    public void testClassIstantiation(){
        // Verifica che la classe App possa essere istanziata correttamente
        App app = new App();
        assertNotNull(app);
    }
}
