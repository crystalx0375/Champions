package crystal.champions.client.render;

import crystal.champions.client.net.ClientPacket;
import crystal.champions.config.ChampionsConfigClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static crystal.champions.client.render.ChampionsColor.applyColor;
import static crystal.champions.client.render.ChampionsColor.resetColor;

public class RenderBar {
    private static final Identifier CHAMPION_STARS = Identifier.of("champions", "textures/gui/staricon.png");
    private static final Identifier CUSTOM_BARS = Identifier.of("champions", "textures/gui/custom_bars.png");

    private RenderBar() {}

    /**
     * Rendering champion bar
     * @param data data of champion
     * @param centerX pos for render champion bar
     * @param y pos for render champion bar
     */
    public static void renderChampion(DrawContext context, int centerX, int y, ClientPacket.ChampionData data, MinecraftClient client) {
        final ChampionsConfigClient config = ChampionsConfigClient.get();
        final int color = data.color();

        renderStars(context, centerX  + config.xOffsetStars, y + config.yOffsetStars, data.tier(), color);
        context.drawCenteredTextWithShadow(client.textRenderer, Text.literal(getTextForTier(data)).append(data.name()), centerX + config.xOffsetText, y + config.yOffsetText, color);
        renderProgressBar(context, centerX + config.xOffsetBar, y + config.yOffsetBar, data.percent(), color);

        if (data.affixes() != null && !data.affixes().isEmpty()) {
            renderAffixes(context, client, centerX + config.xOffsetAffixes, y + config.yOffsetAffixes, data.affixes());
        }
    }

    private static String getTextForTier(ClientPacket.ChampionData data) {
        return switch (data.tier()) {
            case (1) -> "Skilled ";
            case (2) -> "Elite ";
            case (3) -> "Legendary ";
            case (4) -> "Ultimate ";
            case (5) -> "Mythical ";
            default -> "Skilled ";
        };
    }

    private static void renderStars(DrawContext context, int centerX, int y, int count, int color) {
        if (count <= 0) return;
        applyColor(context, color);
        for (int i = 0; i < count; i++) {
            context.drawTexture(CHAMPION_STARS, centerX - (int) (count * 11 * 0.5F) + (i * 11), y, 0, 0, 10, 10, 10, 10);
        }
        resetColor(context);
    }

    private static void renderProgressBar(DrawContext context, int centerX, int y, float percent, int color) {
        final int progress = (int) (percent * 182);

        applyColor(context, color);
        context.drawTexture(CUSTOM_BARS, centerX - (int) (182 * 0.5F), y, 0, 0, 182, 5, 182, 10);
        if (progress > 0) {
            context.drawTexture(CUSTOM_BARS, centerX - (int) (182 * 0.5F), y, 0, 5, progress, 5, 182, 10);
            resetColor(context);
        } else {
            resetColor(context);
        }
    }

    private static void renderAffixes(DrawContext context, MinecraftClient client, int centerX, int y, String translate) {
        final String[] split = translate.split("•|·|\\s+|,");
        final MutableText finalText = Text.empty();

        for (String string : split) {
            final String affixTemp = string.trim();
            if (affixTemp.isEmpty()) continue;
            if (!finalText.getSiblings().isEmpty()) {
                finalText.append(Text.literal(" • ").formatted(Formatting.DARK_GRAY));
            }

            finalText.append(Text.translatable(affixTemp.startsWith("affix.") ? affixTemp : "affix." + affixTemp).formatted(Formatting.GRAY, Formatting.ITALIC));
        }
        context.drawCenteredTextWithShadow(client.textRenderer, finalText, centerX, y, 0xFFFFFF);
    }
}
