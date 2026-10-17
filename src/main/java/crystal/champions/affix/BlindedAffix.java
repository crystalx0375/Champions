package crystal.champions.affix;

import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.Random;

public class BlindedAffix extends Affix {
    private static final ChampionsConfigAffixes config = ChampionsConfigAffixes.get();
    private static final Random rnd = new Random();

    public BlindedAffix() {
        super("blinded");
    }

    @Override
    public void onHurt(LivingEntity champion, LivingEntity target) {
        if (rnd.nextFloat() < config.blindChance) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, config.blindDuration, 0, false, true, true));
        }
    }
}
