package crystal.champions.mixin.data;

import crystal.champions.IChampions;
import crystal.champions.net.ChampionsNetworking;
import crystal.champions.net.Payload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

@SuppressWarnings("java:S2160")
@Mixin(MobEntity.class)
public abstract class ServerUpdatePackets extends LivingEntity implements IChampions {
    @Unique private static final double S_SQ = 225D;
    @Unique private static final double C_SQ = 1600D;
    @Unique private static final double SCAN_RADIUS = 80D;
    @Unique private static final int SYNC = 20;
    @Unique private static final float DIFF = 0.001F;

    @Unique private final Set<UUID> sSent = new HashSet<>();
    @Unique private final Set<UUID> cSent = new HashSet<>();
    @Unique private final Set<UUID> sIn = new HashSet<>();
    @Unique private final Set<UUID> cIn = new HashSet<>();

    @Unique private float lastHealth = -1f;

    protected ServerUpdatePackets(net.minecraft.entity.EntityType<? extends LivingEntity> type, net.minecraft.world.World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void manageChampionHud(CallbackInfo ci) {
        if (this.getWorld().isClient || this.champions$getChampionTier() <= 0) return;

        final int tier = this.champions$getChampionTier();
        final String affixes = this.champions$getAffixesString();
        final float health = this.getHealth();
        final float maxHealth = this.getMaxHealth();

        final float currentHealth = maxHealth > 0 ? this.getHealth() / maxHealth : 0F;
        final boolean broadcast = Math.abs(currentHealth - lastHealth) > DIFF || this.age % SYNC == 0;

        for (ServerPlayerEntity player : this.getWorld().getEntitiesByClass(ServerPlayerEntity.class, this.getBoundingBox().expand(SCAN_RADIUS), q -> true)) {
            if (!ServerPlayNetworking.canSend(player, Payload.ChampionUpdate.SERVER_UPDATE_ID)) continue;

            final double sq = player.squaredDistanceTo(this);
            final UUID id = player.getUuid();
            if (sq <= S_SQ) sIn.add(id);
            if (sq <= C_SQ) cIn.add(id);
        }

        final MobEntity mob = (MobEntity) (Object) this;
        this.championsSync(
                sSent, sIn, mob, broadcast,
                (player, buf) -> ChampionsNetworking.sendUpdateS(player, buf, tier, affixes, health, maxHealth),
                ChampionsNetworking::sendRemoveS
        );

        this.championsSync(
                cSent, cIn, mob, broadcast,
                (player, buf) -> ChampionsNetworking.sendUpdateC(player, buf, tier, affixes, health, maxHealth),
                ChampionsNetworking::sendRemoveC
        );
        
        lastHealth = currentHealth;
        sIn.clear();
        cIn.clear();
    }

    @Unique
    private void championsSync(
            Set<UUID> sent,
            Set<UUID> inRange,
            MobEntity mob,
            boolean broadcast,
            BiConsumer<ServerPlayerEntity, MobEntity> send,
            BiConsumer<ServerPlayerEntity, MobEntity> remove
    ) {
        final var server = this.getWorld().getServer();
        if (server == null) return;

        for (Iterator<UUID> it = sent.iterator(); it.hasNext(); ) {
            final UUID id = it.next();
            if (inRange.contains(id)) continue;
            it.remove();
            final ServerPlayerEntity p = server.getPlayerManager().getPlayer(id);
            if (p != null && ServerPlayNetworking.canSend(p, Payload.ChampionSRemove.REMOVE_ID_SERVER)) {
                remove.accept(p, mob);
            }
        }

        for (UUID id : inRange) {
            if (sent.contains(id) && !broadcast) continue;
            sent.add(id);
            final ServerPlayerEntity p = server.getPlayerManager().getPlayer(id);
            if (p != null) send.accept(p, mob);
        }
    }
}