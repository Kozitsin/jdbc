/**
 * Created by Александр on 28.02.2016.
 */

public class Main {
    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainForm();
            }
        });
    }
}
