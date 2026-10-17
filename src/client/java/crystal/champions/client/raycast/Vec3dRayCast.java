package crystal.champions.client.raycast;

import crystal.champions.client.net.ClientPacket;
import crystal.champions.config.ChampionsConfigClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.UUID;

import static crystal.champions.client.net.ClientPacket.activeChampionsCl;

public class Vec3dRayCast {
    private static UUID targetUuid = null;
    private static long lastUpdateAt = 0;

    private record ClientLook(
            boolean check,
            long lastUpdate,
            UUID uuid
    ) {}

    public static ClientPacket.ChampionDisplayInfo findBestChampionCl(MinecraftClient client, float delta) {
        final ChampionsConfigClient config = ChampionsConfigClient.get();
        final long now = System.currentTimeMillis();

        final ClientLook entity = canSee(client, delta);
        if (entity != null && entity.check() && activeChampionsCl.containsKey(entity.uuid())) {
            targetUuid = entity.uuid();
            lastUpdateAt = now;
        }

        if (targetUuid != null) {
            ClientPacket.ChampionDisplayInfo info = activeChampionsCl.get(targetUuid);
            if (info != null && (now - lastUpdateAt < config.cacheClient) && info.health() > 0) {
                return info;
            } else {
                activeChampionsCl.remove(targetUuid);
                targetUuid = null;
            }
        }
        return null;
    }

    private static ClientLook canSee(MinecraftClient client, float delta) {
        final Entity cameraEntity = client.getCameraEntity();
        if (cameraEntity == null || client.world == null) return null;

        final Vec3d rotation = cameraEntity.getRotationVec(delta);
        final Vec3d startPos = cameraEntity.getCameraPosVec(delta);
        final Vec3d endPos = startPos.add(rotation.x * 80.0, rotation.y * 80.0, rotation.z * 80.0);
        return ChampionsRayCast.SET_FLAG(() -> {
            final RaycastContext ctx = new RaycastContext(
                    startPos, endPos,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    cameraEntity
            );
            final BlockHitResult blockHit = client.world.raycast(ctx);

            final double sq = blockHit.getType() != HitResult.Type.MISS ? blockHit.getPos().squaredDistanceTo(startPos) : 80.0 * 80.0;
            final EntityHitResult entityHitResult = ProjectileUtil.raycast(
                    cameraEntity,
                    startPos, endPos,
                    cameraEntity.getBoundingBox().stretch(rotation.multiply(80.0)).expand(1.0, 1.0, 1.0),
                    entity -> !entity.isSpectator() && entity.canHit(),
                    sq
            );

            if (entityHitResult != null && entityHitResult.getEntity() != null) {
                return new ClientLook(true, System.currentTimeMillis(), entityHitResult.getEntity().getUuid());
            }
            return new ClientLook(false, System.currentTimeMillis(), null);
        });
    }
}
