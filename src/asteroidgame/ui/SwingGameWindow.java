package asteroidgame.ui;

import asteroidgame.core.GameConfig;
import asteroidgame.core.GameEngine;
import asteroidgame.core.GameFrame;
import asteroidgame.core.InputState;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Swing window and timer for the game.
 * All Java Swing code is isolated here in the ui package.
 */
public class SwingGameWindow extends JFrame implements ActionListener {
    private GameEngine engine;
    private RetroTextRenderer renderer;
    private KeyboardInput keyboardInput;
    private FixedGridPanel displayPanel;
    private Timer timer;

    public SwingGameWindow(GameEngine engine) {
        this.engine = engine;
        this.renderer = new RetroTextRenderer();
        this.keyboardInput = new KeyboardInput();
        this.displayPanel = new FixedGridPanel(renderer.getScreenRows(), renderer.getScreenColumns());
        this.timer = new Timer(GameConfig.TIMER_DELAY, this);

        setupWindow();
        drawFrame();
    }

    private void setupWindow() {
        add(displayPanel);
        addKeyListener(keyboardInput);
        displayPanel.addKeyListener(keyboardInput);

        setTitle("Retro Asteroid Blaster - Final Build");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true);

        pack();
        setLocationRelativeTo(null);
    }

    public void start() {
        setVisible(true);
        displayPanel.requestFocusInWindow();
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        InputState input = keyboardInput.collectInputState();
        engine.update(input);

        if (engine.isQuitRequested()) {
            System.exit(0);
        }

        drawFrame();
    }

    private void drawFrame() {
        GameFrame frame = engine.getCurrentFrame();
        displayPanel.setCurrentLevel(frame.getLevel());
        displayPanel.setScreenText(renderer.render(frame));
    }
}
