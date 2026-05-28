package asteroidgame.systems.upgrades;

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

/**
 * Handles upgrade choices shown after each level is cleared.
 */
public class UpgradeManager {

    public boolean applyUpgrade(Player player, int choice) {
        if (choice == 1 && player.getFireRateLevel() < Player.FIRE_RATE_CAP) {
            player.upgradeFireRate();
            return true;
        }

        if (choice == 2 && player.getMaxLives() < Player.MAX_LIFE_CAP) {
            player.increaseMaxLives();
            return true;
        }

        if (choice == 3 && player.getShotLevel() < Player.SHOT_CAP) {
            player.upgradeShotLevel();
            return true;
        }

        if (choice == 4 && player.getShieldCharges() < Player.SHIELD_CAP) {
            player.addShield();
            return true;
        }

        return false;
    }

    public String describeChoice(int choice) {
        if (choice == 1) return "FIRE SPEED";
        if (choice == 2) return "MAX LIVES";
        if (choice == 3) return "SHOT WIDTH";
        if (choice == 4) return "SHIELD";
        return "UPGRADE";
    }

    public String[] buildUpgradeMenu(Player player) {
        String[] lines = new String[4];

        lines[0] = buildLine(1, "FIRE SPEED", player.getFireRateLevel(), Player.FIRE_RATE_CAP,
                "lower cooldown");
        lines[1] = buildLine(2, "MAX LIVES", player.getMaxLives() - 3, Player.MAX_LIFE_CAP - 3,
                "heal + raise cap");
        lines[2] = buildLine(3, "SHOT WIDTH", player.getShotLevel(), Player.SHOT_CAP,
                "directional double/triple");
        lines[3] = buildLine(4, "SHIELD", player.getShieldCharges(), Player.SHIELD_CAP,
                "block one hit");

        return lines;
    }

    private String buildLine(int number, String name, int current, int cap, String effect) {
        String progress = current + "/" + cap;

        if (current >= cap) {
            progress = "MAX";
        }

        return number + "  " + name + "  [" + progress + "]  " + effect;
    }
}
