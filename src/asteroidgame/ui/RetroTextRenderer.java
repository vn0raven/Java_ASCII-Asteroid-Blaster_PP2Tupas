package asteroidgame.ui;

import asteroidgame.core.GameEngine;
import asteroidgame.core.GameFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts game data into a fixed-size Unicode retro arcade screen.
 * It does not control gameplay. It only formats what the user sees.
 */
public class RetroTextRenderer {
    private static final int BOARD_WIDTH = GameEngine.BOARD_WIDTH;
    private static final int BOARD_HEIGHT = GameEngine.BOARD_HEIGHT;
    private static final int SCREEN_COLUMNS = BOARD_WIDTH + 2;
    private static final int SCREEN_ROWS = BOARD_HEIGHT + 9;

    public String render(GameFrame frame) {
        List<String> lines = new ArrayList<String>();

        lines.add(borderLine('╔', '═', '╗'));
        lines.add(boxed(centerText("✦ RETRO ASTEROID BLASTER ✦")));
        lines.add(borderLine('╠', '═', '╣'));
        lines.add(boxed(centerText(buildHud(frame))));
        lines.add(borderLine('╠', '─', '╣'));

        char[][] grid = frame.getGrid();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            lines.add(renderBoardRow(grid, row, frame));
        }

        lines.add(borderLine('╠', '─', '╣'));
        lines.add(boxed(centerText(buildControls(frame))));
        lines.add(boxed(centerText(buildStatus(frame))));
        lines.add(borderLine('╚', '═', '╝'));

        return joinLines(lines);
    }

    public int getScreenColumns() {
        return SCREEN_COLUMNS;
    }

    public int getScreenRows() {
        return SCREEN_ROWS;
    }

    private String renderBoardRow(char[][] grid, int row, GameFrame frame) {
        String overlay = getOverlayText(row, frame);

        if (overlay != null) {
            return boxed(centerText(overlay));
        }

        StringBuilder line = new StringBuilder();
        line.append('║');

        for (int col = 0; col < BOARD_WIDTH; col++) {
            char symbol = grid[row][col];
            line.append(decorateSpace(symbol, row, col));
        }

        line.append('║');
        return line.toString();
    }

    private String getOverlayText(int row, GameFrame frame) {
        if (frame.isStartScreen()) {
            if (row == 2) return "ARENA MODE: 6 LEVEL CAMPAIGN";
            
            int sel = frame.getMenuSelectedIndex();
            
            // Draw the cursor pointing to the active selection
            if (row == 6) return sel == 0 ? "► PLAY GAME ◄" : "  PLAY GAME  ";
            if (row == 8) return sel == 1 ? "► HIGH SCORES ◄" : "  HIGH SCORES  ";
            if (row == 10) return sel == 2 ? "► QUIT ◄" : "  QUIT  ";

            if (row == 14) return "W/S OR UP/DOWN TO MOVE";
            if (row == 15) return "PRESS ENTER TO SELECT";
        }

        if (frame.isLevelIntro()) {
            if (row == 4) return "LEVEL " + frame.getLevel() + " / " + frame.getMaxLevel();
            if (row == 6) return frame.getLevelName();
            if (row == 8) return levelTip(frame.getLevel());
            if (row == 11) return "TARGET SCORE: " + frame.getNextLevelScore();
            if (row == 14) return "PRESS ENTER TO LAUNCH";
        }

        if (frame.isPaused()) {
            if (row == 7) return "▣ PAUSED ▣";
            if (row == 9) return "PRESS P TO RESUME";
        }

        if (frame.isLevelUp()) {
            String[] menu = frame.getUpgradeMenuLines();
            if (row == 1) return "✹ LEVEL " + (frame.getLevel() + 1) + " UNLOCKED ✹";
            if (row == 3) return "CHOOSE ONE SHIP UPGRADE";
            if (row == 5) return menu[0];
            if (row == 7) return menu[1];
            if (row == 9) return menu[2];
            if (row == 11) return menu[3];
            if (row == 14) return "PRESS 1, 2, 3, OR 4";
            if (row == 16) return "MAXED OPTIONS SHOW A WARNING";
        }

        if (frame.isVictory()) {
            if (row == 2) return "✹ MISSION COMPLETE ✹";
            if (row == 4) return "YOU CLEARED ALL 6 LEVELS";
            if (row == 6) return "FINAL SCORE: " + frame.getScore();
            if (row == 8) return "ASTEROIDS DESTROYED: " + frame.getAsteroidsDestroyed();
            if (row == 10) return "UFOS DESTROYED: " + frame.getUfosDestroyed();
            if (row == 12) return "POWER-UPS COLLECTED: " + frame.getPowerUpsCollected();
            if (row == 14) return "FINAL BUILD FIRE " + frame.getFireRateLevel() + "  SHOT " + frame.getShotLevel() + "  SHIELD " + frame.getShieldCharges();
            if (row == 15) return "PRESS R TO RESTART";
        }

        if (frame.isGameOver()) {
            if (row == 3) return "✹ GAME OVER ✹";
            if (row == 5) return "FINAL SCORE: " + frame.getScore();
            if (row == 7) return "LEVEL REACHED: " + frame.getLevel() + " / " + frame.getMaxLevel();
            if (row == 9) return "ASTEROIDS DESTROYED: " + frame.getAsteroidsDestroyed();
            if (row == 11) return "UFOS DESTROYED: " + frame.getUfosDestroyed();
            if (row == 13) return "POWER-UPS COLLECTED: " + frame.getPowerUpsCollected();
            if (row == 15) return "DANGER REACHED: " + frame.getDangerLevel() + " / " + frame.getDangerLimit();
            if (row == 17) return "PRESS R TO RESTART";
        }

        if (frame.isHighScoreScreen()) {
            if (row == 2) return "✦ LOCAL LEADERBOARD ✦";    
            if (row == 6) return "1. JACA    " + padLeft(String.valueOf(frame.getHighScore()), 6, '0');
            if (row == 8) return "2. CPU     001000";
            if (row == 10) return "3. CPU     000500";     
            if (row == 15) return "PRESS ENTER OR M TO RETURN";
        }

        return null;
    }

    private String levelTip(int level) {
        if (level == 1) return "MOVE IN 8 DIRECTIONS - SHIP FACES YOU";
        if (level == 2) return "ASTEROIDS CAN DRIFT IN DIAGONALLY";
        if (level == 3) return "METEOR SHARD CLUSTERS BEGIN";
        if (level == 4) return "UFO CONTACT: DODGE ENEMY FIRE";
        if (level == 5) return "METEOR STORM + UFO CROSSFIRE";
        return "FINAL WAVE: SURVIVE AND FINISH STRONG";
    }

    private char decorateSpace(char symbol, int row, int col) {
        if (symbol != ' ') {
            return symbol;
        }
        // Stable star field. It does not animate, so the screen no longer feels like it is shifting.
        int pattern = (row * 19 + col * 13) % 131;

        if (pattern == 0) {
            return '·';
        }

        return ' ';
    }

    private String buildHud(GameFrame frame) {
        String score = "SCORE " + padLeft(String.valueOf(frame.getScore()), 4, '0');
        String hp = "HP " + padLeft(String.valueOf(frame.getLives()), 2, '0') + "/" + padLeft(String.valueOf(frame.getMaxLives()), 2, '0');
        String level = "LV " + frame.getLevel() + "/" + frame.getMaxLevel();
        String danger = "DNG " + frame.getDangerLevel() + "/" + frame.getDangerLimit();

        if (frame.isVictory()) {
            danger = "CLEARED";
        }

        return score + "  " + hp + "  " + level + "  " + danger;
    }

    private String buildControls(GameFrame frame) {
        if (frame.isStartScreen()) return "ENTER START | Q QUIT";
        if (frame.isHighScoreScreen()) return "ENTER RETURN | Q QUIT";
        if (frame.isLevelIntro()) return "ENTER LAUNCH | M MENU | Q QUIT";
        if (frame.isLevelUp()) return "1 FIRE | 2 LIFE | 3 SHOT | 4 SHIELD";
        if (frame.isVictory()) return "R RESTART | M MENU | Q QUIT";
        if (frame.isGameOver()) return "R RESTART | M MENU | Q QUIT";
        if (frame.isPaused()) return "P RESUME | M MENU | Q QUIT";   

        return "WASD/ARROWS FACE+MOVE | SPACE FIRE"; 
    }

    private String buildStatus(GameFrame frame) {
        if (frame.getStatusMessage() != null && frame.getStatusMessage().length() > 0) {
            return frame.getStatusMessage();
        }

        if (frame.isStartScreen()) return "FIXED GRID + UNICODE RETRO STYLE";
        if (frame.isLevelIntro()) return "BALANCED ARENA + UFO CONTACT FROM LV4";
        if (frame.isLevelUp()) return "UPGRADES HAVE CAPS FOR BALANCE";
        if (frame.isPaused()) return "GAME LOOP STOPPED TEMPORARILY";
        if (frame.isVictory()) return "CAMPAIGN CLEARED";
        if (frame.isGameOver()) return "THANKS FOR PLAYING";
        if (frame.isPlayerHitFlash()) return "WARNING: SHIP HIT OR SHIELD USED!";

        String next = "NEXT " + padLeft(String.valueOf(frame.getNextLevelScore()), 4, '0');
        String fire = "F" + frame.getFireRateLevel() + "/" + frame.getFireRateCap();
        String shot = "S" + frame.getShotLevel() + "/" + frame.getShotCap();
        String shield = "SH" + frame.getShieldCharges() + "/" + frame.getShieldCap();
        return next + "  " + fire + "  " + shot + "  " + shield;
    }

    private String borderLine(char left, char fill, char right) {
        StringBuilder line = new StringBuilder();
        line.append(left);
        for (int i = 0; i < BOARD_WIDTH; i++) {
            line.append(fill);
        }
        line.append(right);
        return line.toString();
    }

    private String boxed(String text) {
        String cleanText = fitText(text);
        return "║" + cleanText + "║";
    }

    private String centerText(String text) {
        String cleanText = text;

        if (cleanText.length() > BOARD_WIDTH) {
            cleanText = cleanText.substring(0, BOARD_WIDTH);
        }

        int totalPadding = BOARD_WIDTH - cleanText.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        return repeat(' ', leftPadding) + cleanText + repeat(' ', rightPadding);
    }

    private String fitText(String text) {
        if (text.length() > BOARD_WIDTH) {
            return text.substring(0, BOARD_WIDTH);
        }

        return text + repeat(' ', BOARD_WIDTH - text.length());
    }

    private String repeat(char character, int count) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(character);
        }

        return result.toString();
    }

    private String padLeft(String text, int targetLength, char padding) {
        if (text.length() >= targetLength) {
            return text;
        }

        return repeat(padding, targetLength - text.length()) + text;
    }

    private String joinLines(List<String> lines) {
        StringBuilder screen = new StringBuilder();

        for (int i = 0; i < lines.size(); i++) {
            screen.append(lines.get(i));

            if (i < lines.size() - 1) {
                screen.append('\n');
            }
        }

        return screen.toString();
    }
}
