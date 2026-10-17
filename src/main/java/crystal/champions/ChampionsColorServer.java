package crystal.champions;

import crystal.champions.config.ChampionsConfigClient;

public class ChampionsColorServer {
    private ChampionsColorServer() {}

    private static int parseHex(String hex) {
        try {
            return Integer.decode(hex);
        } catch (NumberFormatException e) {
            return 0xFFFFFF;
        }
    }

    public static int getColor(int tier) {
        final ChampionsConfigClient config = ChampionsConfigClient.get();

        try {
            return switch (tier) {
                case 1 -> parseHex(config.hexTier1);
                case 2 -> parseHex(config.hexTier2);
                case 3 -> parseHex(config.hexTier3);
                case 4 -> parseHex(config.hexTier4);
                default -> parseHex(config.hexTier5);
            };
        } catch (Exception e) {
            return 0xffffff;
        }
    }
}
