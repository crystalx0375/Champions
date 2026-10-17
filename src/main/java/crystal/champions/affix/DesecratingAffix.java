package crystal.champions.affix;

import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

public class DesecratingAffix extends Affix {
    private static final ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

    public DesecratingAffix() {
        super("desecrating");
    }

    @Override
    public void onAttack(LivingEntity entity, MobEntity mob) {
        if (entity.age % config.timeBeforeDesecrating != 0) return;
        final LivingEntity target = mob.getTarget();
        if (target != null) {
            final World world = entity.getWorld();
            AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(world, target.getX(), target.getY(), target.getZ());
            cloud.setRadius(3.0f);
            cloud.setWaitTime(10);
            cloud.setDuration(config.cloudDuration);
            cloud.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 10, 1));

            world.spawnEntity(cloud);
        }
    }
}
