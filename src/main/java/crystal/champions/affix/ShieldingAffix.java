package crystal.champions.affix;

import crystal.champions.IChampions;
import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class ShieldingAffix extends Affix {
    private static final ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

    public ShieldingAffix() {
        super("shielding");
    }

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.getWorld().isClient) return;

        final long time = entity.getWorld().getTime();
        final boolean shieldWork = (time % config.shieldAllTime) < config.shieldWork;

        final IChampions champion = (IChampions) entity;

        if (champion.champions$isShielding() != shieldWork) {
            champion.champions$setShielding(shieldWork);
        }
        if (shieldWork) {
            ((ServerWorld) entity.getWorld()).spawnParticles(
                    ParticleTypes.EFFECT,
                    entity.getX(), entity.getRandomBodyY(), entity.getZ(),
                    0, 1.0, 1.0, 1.0, 0.8
            );
        }
    }
}
