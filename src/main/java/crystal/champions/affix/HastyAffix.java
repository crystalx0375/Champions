package crystal.champions.affix;

import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class HastyAffix extends Affix {
    private static final ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

    public HastyAffix() {
        super("hasty");
    }

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.age % 20 == 0) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20, config.hastyAmplifier, true, false, false));
        }
    }
}
