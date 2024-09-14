////////////////////////////////////////////////////////////////////
// Riccardo Stefani
////////////////////////////////////////////////////////////////////

package packageBreaker;

import javax.swing.JFrame;

public class Main {
    public static JFrame createFrame() {
        JFrame obj = new JFrame();
        BrickBreaker gamePlay = new BrickBreaker();
        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Brick Breaker");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
        obj.setVisible(true);
        return obj;
    }

    public static void main(String[] args) {
        createFrame();
    }
}
