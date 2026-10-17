package crystal.champions.client.raycast;

import crystal.champions.client.mixin.ClientWorldAccessor;
import crystal.champions.client.net.ClientPacket;
import crystal.champions.config.ChampionsConfigClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Map;
import java.util.UUID;

import static crystal.champions.client.net.ClientPacket.activeChampions;

public class PosRayCast {
    public static ClientPacket.ChampionDisplayInfo findBestChampion(MinecraftClient client, float delta) {
        final ChampionsConfigClient config = ChampionsConfigClient.get();
        if (config.onlyForView || client.world == null || client.player == null) return null;

        final long now = System.currentTimeMillis();
        ClientPacket.ChampionDisplayInfo bestInfo = null;
        for (Map.Entry<UUID, ClientPacket.ChampionDisplayInfo> entry : activeChampions.entrySet()) {
            final ClientPacket.ChampionDisplayInfo info = entry.getValue();

            final UUID uuid = entry.getKey();
            final Entity targetEntity = ((ClientWorldAccessor) client.world).getEntityManager().getLookup().get(uuid);

            final boolean cache = now - info.lastUpdate() > config.cacheServer;
            final boolean canSee = canSee(client, targetEntity, delta);
            final boolean alwaysRender = config.alwaysRenderBox;
            final boolean alive = info.health() <= 0;

            if (cache || alive || (!canSee && !alwaysRender)) {
                activeChampions.remove(entry.getKey());
                continue;
            }


            if (bestInfo == null || info.tier() > bestInfo.tier()) {
                bestInfo = info;
            }
        }
        return bestInfo;
    }

    private static boolean canSee(MinecraftClient client, Entity target, float delta) {
        final Entity cameraEntity = client.getCameraEntity();
        if (cameraEntity == null || client.world == null) return false;
        if (target == null) return false;

        final Vec3d startPos = cameraEntity.getCameraPosVec(delta);
        final Vec3d endPos = target.getBoundingBox().getCenter();
        return ChampionsRayCast.SET_FLAG(() -> {
            final RaycastContext ctx = new RaycastContext(
                    startPos, endPos,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    cameraEntity
            );
            return client.world.raycast(ctx).getType() != HitResult.Type.BLOCK;
        });
    }
}
