package crystal.champions.affix;

import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

public class MagneticAffix extends Affix {
    private static final ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

    public MagneticAffix() {
        super("magnetic");
    }

    @Override
    public void onAttack(LivingEntity entity, MobEntity mob) {
        if (entity.age % config.magneticCooldown <= config.magneticPullTime) return;
        final LivingEntity target = mob.getTarget();
        if (target != null) {
            final Vec3d pullDir = entity.getPos().subtract(target.getPos()).normalize();
            final double i = 0.01 * config.strength;
            final float pX = (float) (pullDir.x * i + target.getVelocity().x * 0.5);
            final float pY = (float) (pullDir.y * i + target.getVelocity().y * 0.6);
            final float pZ = (float) (pullDir.z * i + target.getVelocity().z * 0.5);
            target.setVelocity(pX, pY, pZ);

            target.velocityModified = true;
        }
    }
}