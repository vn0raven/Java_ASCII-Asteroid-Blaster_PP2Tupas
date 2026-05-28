package asteroidgame.ui.render;

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
        // The board is 40 rows tall. Shifting all UI elements down to 
        // roughly rows 12-26 vertically centers them perfectly.
        
        if (frame.isStartScreen()) {
            if (row == 12) return "ARENA MODE: 7 LEVEL CAMPAIGN";
            
            int sel = frame.getMenuSelectedIndex();
            
            // Padded to exactly 17 characters wide so the arrows align cleanly
            if (row == 16) return sel == 0 ? "►   PLAY GAME   ◄" : "    PLAY GAME    ";
            if (row == 18) return sel == 1 ? "►  HIGH SCORES  ◄" : "   HIGH SCORES   ";
            if (row == 20) return sel == 2 ? "►     QUIT      ◄" : "      QUIT       ";

            if (row == 25) return "W/S OR UP/DOWN TO MOVE";
            if (row == 26) return "PRESS ENTER TO SELECT";
        }

        if (frame.isLevelIntro()) {
            if (row == 14) return "LEVEL " + frame.getLevel() + " / " + frame.getMaxLevel();
            if (row == 16) return frame.getLevelName();
            if (row == 18) return levelTip(frame.getLevel());
            if (row == 21) return frame.getLevel() >= frame.getMaxLevel() ? "OBJECTIVE: DEFEAT THE BOSS" : "TARGET SCORE: " + frame.getNextLevelScore();
            if (row == 25) return "PRESS ENTER TO LAUNCH";
        }

        if (frame.isPaused()) {
            if (row == 18) return "▣ PAUSED ▣";
            if (row == 20) return "PRESS P TO RESUME";
        }

        if (frame.isLevelUp()) {
            String[] menu = frame.getUpgradeMenuLines();
            if (row == 10) return "✹ LEVEL " + (frame.getLevel() + 1) + " UNLOCKED ✹";
            if (row == 12) return "CHOOSE ONE SHIP UPGRADE";
            if (row == 15) return menu[0];
            if (row == 17) return menu[1];
            if (row == 19) return menu[2];
            if (row == 21) return menu[3];
            if (row == 25) return "PRESS 1, 2, 3, OR 4";
            if (row == 26) return "MAXED OPTIONS SHOW A WARNING";
        }

        if (frame.isVictory()) {
            if (row == 10) return "✹ MISSION COMPLETE ✹";
            if (row == 12) return "YOU DEFEATED THE LEVEL 7 BOSS";
            if (row == 15) return "FINAL SCORE: " + frame.getScore();
            if (row == 17) return "ASTEROIDS DESTROYED: " + frame.getAsteroidsDestroyed();
            if (row == 19) return "UFOS DESTROYED: " + frame.getUfosDestroyed();
            if (row == 21) return "POWER-UPS COLLECTED: " + frame.getPowerUpsCollected();
            if (row == 24) return "FINAL BUILD FIRE " + frame.getFireRateLevel() + "  SHOT " + frame.getShotLevel() + "  SHIELD " + frame.getShieldCharges();
            if (row == 26) return "PRESS R TO RESTART";
        }

        if (frame.isGameOver()) {
            if (row == 10) return "✹ GAME OVER ✹";
            if (row == 12) return "FINAL SCORE: " + frame.getScore();
            if (row == 14) return "LEVEL REACHED: " + frame.getLevel() + " / " + frame.getMaxLevel();
            if (row == 16) return "ASTEROIDS DESTROYED: " + frame.getAsteroidsDestroyed();
            if (row == 18) return "UFOS DESTROYED: " + frame.getUfosDestroyed();
            if (row == 20) return "POWER-UPS COLLECTED: " + frame.getPowerUpsCollected();
            if (row == 22) return "DANGER REACHED: " + frame.getDangerLevel() + " / " + frame.getDangerLimit();
            if (row == 26) return "PRESS R TO RESTART";
        }

        if (frame.isHighScoreScreen()) {
            if (row == 12) return "✦ LOCAL LEADERBOARD ✦";    
            if (row == 16) return "1. JACA    " + padLeft(String.valueOf(frame.getHighScore()), 6, '0');
            if (row == 18) return "2. CPU     001000";
            if (row == 20) return "3. CPU     000500";     
            if (row == 25) return "PRESS ENTER OR M TO RETURN";
        }

        return null;
    }

    private String levelTip(int level) {
        if (level == 1) return "MOVE IN 8 DIRECTIONS - SHIP FACES DIRECTION";
        if (level == 2) return "ASTEROIDS CAN DRIFT IN DIAGONALLY";
        if (level == 3) return "METEOR SHARD CLUSTERS BEGIN";
        if (level == 4) return "UFO CONTACT: DODGE ENEMY FIRE";
        if (level == 5) return "METEOR STORM + UFO CROSSFIRE";
        if (level == 6) return "FINAL WAVE: SURVIVE BEFORE BOSS";
        return "BOSS BATTLE: DESTROY THE MOTHERSHIP";
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

        if (frame.hasBoss()) {
            danger = "BOSS " + frame.getBossHp() + "/" + frame.getBossMaxHp();
        } else if (frame.isVictory()) {
            danger = "CLEARED";
        }

        return score + "  " + hp + "  " + level + "  " + danger;
    }

    private String buildControls(GameFrame frame) {
        if (frame.isStartScreen()) return "ENTER = SELECT   |   Q = QUIT";
        if (frame.isHighScoreScreen()) return "ENTER = RETURN   |   Q = QUIT";
        if (frame.isLevelIntro()) return "ENTER = LAUNCH   |   M = MENU   |   Q = QUIT";
        if (frame.isLevelUp()) return "1 = FIRE   |   2 = LIFE   |   3 = SHOT   |   4 = SHIELD";
        if (frame.isVictory()) return "R = RESTART   |   M = MENU   |   Q = QUIT";
        if (frame.isGameOver()) return "R = RESTART   |   M = MENU   |   Q = QUIT";
        if (frame.isPaused()) return "P = RESUME   |   M = MENU   |   Q = QUIT";   

        return "WASD / ARROWS = MOVE   |   SPACE = FIRE   |   P = PAUSE"; 
    }

    private String buildStatus(GameFrame frame) {
        if (frame.getStatusMessage() != null && frame.getStatusMessage().length() > 0) {
            return frame.getStatusMessage();
        }

        if (frame.isStartScreen()) return "";
        if (frame.isLevelIntro()) return "";
        if (frame.isLevelUp()) return "SELECT ONE UPGRADE TO CONTINUE";
        if (frame.isPaused()) return "GAME PAUSED";
        if (frame.isVictory()) return "CAMPAIGN CLEARED";
        if (frame.isGameOver()) return "PRESS R TO TRY AGAIN";
        if (frame.isPlayerHitFlash()) return "WARNING: SHIP HIT";

        if (frame.hasBoss()) {
            String bossBar = buildBossBar(frame.getBossHp(), frame.getBossMaxHp());
            return "MOTHERSHIP HP " + bossBar + "  PHASE " + getBossPhase(frame.getBossHp(), frame.getBossMaxHp());
        }

        String next = "NEXT " + padLeft(String.valueOf(frame.getNextLevelScore()), 4, '0');
        String fire = "F" + frame.getFireRateLevel() + "/" + frame.getFireRateCap();
        String shot = "S" + frame.getShotLevel() + "/" + frame.getShotCap();
        String shield = "SH" + frame.getShieldCharges() + "/" + frame.getShieldCap();
        return next + "  " + fire + "  " + shot + "  " + shield;
    }

    private String buildBossBar(int hp, int maxHp) {
        if (maxHp <= 0) {
            return "----------";
        }

        int filled = (int) Math.round((hp * 10.0) / maxHp);
        if (filled < 0) filled = 0;
        if (filled > 10) filled = 10;

        return repeat('█', filled) + repeat('░', 10 - filled);
    }

    private int getBossPhase(int hp, int maxHp) {
        if (maxHp <= 0) return 1;
        if (hp <= maxHp / 3) return 3;
        if (hp <= (maxHp * 2) / 3) return 2;
        return 1;
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
