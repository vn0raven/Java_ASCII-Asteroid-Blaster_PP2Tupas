package asteroidgame.ui;

import asteroidgame.core.GameEngine;

import javax.swing.SwingUtilities;

/**
 * Keeps Swing startup code inside the UI package.
 */
public class SwingLauncher {

    private SwingLauncher() {
        // Utility class. No object needed.
    }

    public static void launch() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameEngine engine = new GameEngine();
                SwingGameWindow window = new SwingGameWindow(engine);
                window.start();
            }
        });
    }
}
