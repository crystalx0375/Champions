package crystal.champions.affix;

import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;

public class LivelyAffix extends Affix {
    private static final ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

    public LivelyAffix() {
        super("lively");
    }
    
    @Override
    public void onAttack(LivingEntity entity, MobEntity mob) {
        if (entity.age % config.entityHealTime != 0) return;

        final LivingEntity target = mob.getTarget();
        if (target == null) {
            mob.heal(config.entityHealNoTarget);
        } else {
            mob.heal(config.entityHeal);
        }
    }
}
