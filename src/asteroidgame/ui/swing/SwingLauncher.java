package asteroidgame.ui.swing;

import asteroidgame.audio.*;
import asteroidgame.config.*;
import asteroidgame.core.*;
import asteroidgame.state.*;
import asteroidgame.objects.assets.*;
import asteroidgame.objects.base.*;
import asteroidgame.objects.asteroids.*;
import asteroidgame.objects.effects.*;
import asteroidgame.objects.enemies.*;
import asteroidgame.objects.player.*;
import asteroidgame.objects.powerups.*;
import asteroidgame.objects.projectiles.*;
import asteroidgame.systems.collision.*;
import asteroidgame.systems.scoring.*;
import asteroidgame.systems.spawning.*;
import asteroidgame.systems.upgrades.*;
import asteroidgame.ui.input.*;
import asteroidgame.ui.render.*;
import asteroidgame.ui.swing.*;

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
