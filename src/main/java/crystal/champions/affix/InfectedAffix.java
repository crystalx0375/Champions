package crystal.champions.affix;

import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfectedAffix extends Affix {
    private static final ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

    public InfectedAffix() {
        super("infected");
    }

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.age % config.timeBeforeInfected != 0) return;

        final World world = entity.getWorld();

        final int count = (int) (entity.getHealth() * config.infectedFactorHealth + config.infectedSilverfish);
        final int maxCount = Math.min(count, config.maxSilverFishCount);

        for (int i = 0; i < maxCount; i++) {
            SilverfishEntity silverfish = EntityType.SILVERFISH.create(world);
            if (silverfish != null) {
                BlockPos pos = entity.getBlockPos();

                silverfish.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getRandom().nextFloat() * 360.0F, 0.0F);
                silverfish.initialize((ServerWorld) world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null);

                world.spawnEntity(silverfish);
            }
        }
    }
}
