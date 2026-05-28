package asteroidgame.objects;

import java.util.Random;

/**
 * Stores the multi-cell asteroid sprite library.
 *
 * The asteroids are no longer simple circles. They use faceted rock shapes
 * inspired by retro arcade asteroids, with strong edges and inner fracture
 * details. Each family has multiple variants so the board does not feel
 * repetitive during gameplay.
 */
public final class AsteroidSprites {
    public static final int FAMILY_HEAVY = 0;
    public static final int FAMILY_STANDARD = 1;
    public static final int FAMILY_LIGHT = 2;

    private static final Random RANDOM = new Random();

    private static final char[][][] HEAVY_ARMORED = {
            sprite(
                    " /███\\ ",
                    "/▓▒▒▓\\ ",
                    "<▓▒▓▒▓>",
                    " \\▓_▓/ "
            ),
            sprite(
                    " /▓██\\ ",
                    "<█▒▓▒█>",
                    "<▓██▒▓>",
                    " \\__▓/ "
            ),
            sprite(
                    " /██▓\\ ",
                    "<▓▒▒▓▓>",
                    "<█▓▒▒█>",
                    " \\▓__/ "
            )
    };

    private static final char[][][] HEAVY_CRACKED = {
            sprite(
                    " /▓█_\\ ",
                    "/▒_▒▓\\ ",
                    "<▓▒_▒▓>",
                    " \\__▓/ "
            ),
            sprite(
                    " /█_▓\\ ",
                    "<▒▓_▒█>",
                    "<▓_▒▒▓>",
                    " \\_▓_/ "
            ),
            sprite(
                    " /▓_█\\ ",
                    "<█▒_▒▓>",
                    "<▒▓▓_▒>",
                    " \\___/ "
            )
    };

    private static final char[][][] HEAVY_WEAK = {
            sprite(
                    "  /▓\\  ",
                    " <▒_▓> ",
                    " <▓▒_> ",
                    "  \\_/  "
            ),
            sprite(
                    "  /▒\\  ",
                    " <▓_▒> ",
                    " <_▒▓> ",
                    "  \\_/  "
            ),
            sprite(
                    "  /█\\  ",
                    " <▒_▒> ",
                    " <▓__> ",
                    "  \\_/  "
            )
    };

    private static final char[][][] STANDARD_CRACKED = {
            sprite(
                    " /▓▓\\ ",
                    "<▓▒_▓>",
                    " \\▓_/ "
            ),
            sprite(
                    " /▒█\\ ",
                    "<▓_▒▓>",
                    " \\__/ "
            ),
            sprite(
                    " /▓█\\ ",
                    "<▒▓▒_>",
                    " \\_▓/ "
            ),
            sprite(
                    " /██\\ ",
                    "<▓▒_█>",
                    " \\__/ "
            ),
            sprite(
                    " /▒▓\\ ",
                    "<█▒▓>",
                    " \\_▓/ "
            ),
            sprite(
                    " /▓▒\\ ",
                    "<▒█_>",
                    " \\__/ "
            )
    };

    private static final char[][][] STANDARD_WEAK = {
            sprite(
                    " /▒\\ ",
                    "<▒_▓>",
                    " \\_/ "
            ),
            sprite(
                    " /▓\\ ",
                    "<__▒>",
                    " \\_/ "
            ),
            sprite(
                    " /▒\\ ",
                    "<▓_ >",
                    " \\_/ "
            ),
            sprite(
                    " /█\\ ",
                    "<_▒▓>",
                    " \\_/ "
            ),
            sprite(
                    " /▒\\ ",
                    "<▓_>",
                    " \\_/ "
            ),
            sprite(
                    " /▓\\ ",
                    "<▒_█>",
                    " \\_/ "
            )
    };

    private static final char[][][] LIGHT_WEAK = {
            sprite(
                    " /▒\\ ",
                    "<▓▒>",
                    " \\/ "
            ),
            sprite(
                    " /▓\\ ",
                    "<▒█>",
                    " \\/ "
            ),
            sprite(
                    " /█\\ ",
                    "<▒▓>",
                    " \\_ "
            ),
            sprite(
                    " /▒\\ ",
                    "<█▒>",
                    " _/ "
            ),
            sprite(
                    " /▓\\ ",
                    "<▓_>",
                    " \\/ "
            ),
            sprite(
                    " /▒\\ ",
                    "<_█>",
                    " \\_ "
            )
    };

    private AsteroidSprites() {
        // Utility class. No object needed.
    }

    public static int randomVariant(int family) {
        return RANDOM.nextInt(getVariantCount(family));
    }

    public static char[][] getSprite(int family, int damageStage, int variant) {
        char[][][] bank = getSpriteBank(family, damageStage);
        return bank[Math.floorMod(variant, bank.length)];
    }

    public static int getVariantCount(int family) {
        if (family == FAMILY_HEAVY) return HEAVY_ARMORED.length;
        if (family == FAMILY_STANDARD) return STANDARD_CRACKED.length;
        return LIGHT_WEAK.length;
    }

    public static boolean isSolid(char symbol) {
        return symbol != ' ';
    }

    private static char[][][] getSpriteBank(int family, int damageStage) {
        if (family == FAMILY_HEAVY) {
            if (damageStage >= Asteroid.ARMORED_STAGE) return HEAVY_ARMORED;
            if (damageStage == Asteroid.CRACKED_STAGE) return HEAVY_CRACKED;
            return HEAVY_WEAK;
        }

        if (family == FAMILY_STANDARD) {
            if (damageStage >= Asteroid.CRACKED_STAGE) return STANDARD_CRACKED;
            return STANDARD_WEAK;
        }

        return LIGHT_WEAK;
    }

    private static char[][] sprite(String... rows) {
        int width = 0;
        for (String row : rows) {
            width = Math.max(width, row.length());
        }

        char[][] result = new char[rows.length][width];
        for (int y = 0; y < rows.length; y++) {
            for (int x = 0; x < width; x++) {
                if (x < rows[y].length()) {
                    result[y][x] = rows[y].charAt(x);
                } else {
                    result[y][x] = ' ';
                }
            }
        }

        return result;
    }
}
