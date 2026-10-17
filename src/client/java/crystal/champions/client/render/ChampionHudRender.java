package crystal.champions.client.render;

import crystal.champions.client.net.ClientPacket;
import crystal.champions.client.raycast.PosRayCast;
import crystal.champions.client.raycast.Vec3dRayCast;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import static crystal.champions.client.render.ChampionsColor.getColor;

public abstract class ChampionHudRender implements HudRenderCallback {
    /**
     * Rendering champions boss bar
     * First try to find player looking at champion "client"
     * Second try to ray cast all mobs in box "server"
     */
    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        final MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;

        final int centerX = client.getWindow().getScaledWidth() / 2;
        final float delta = tickCounter.getTickDelta(true);

        // Client
        final ClientPacket.ChampionDisplayInfo infoCl = Vec3dRayCast.findBestChampionCl(client, delta);
        if (infoCl != null) {
            RenderBar.renderChampion(context, centerX, 12, dataWrite(infoCl), client);
            return;
        }
        // Server
        final ClientPacket.ChampionDisplayInfo info = PosRayCast.findBestChampion(client, delta);
        if (info != null) {
            RenderBar.renderChampion(context, centerX, 12, dataWrite(info), client);
        }
    }

    private ClientPacket.ChampionData dataWrite(ClientPacket.ChampionDisplayInfo info) {
        return new ClientPacket.ChampionData(
                info.name(),
                info.tier(),
                info.affixes(),
                info.health() / info.maxHealth(),
                getColor(info.tier())
        );
    }
}