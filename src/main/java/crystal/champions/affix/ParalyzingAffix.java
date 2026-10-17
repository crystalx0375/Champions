package crystal.champions.affix;

import crystal.champions.config.ChampionsConfigAffixes;
import crystal.champions.effects.RegisterEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Random;

public class ParalyzingAffix extends Affix{
    private static final Random rnd = new Random();
    private static final ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

    public ParalyzingAffix() {
        super("paralyzing");
    }

    @Override
    public void onHurt(LivingEntity champion, LivingEntity target) {
        if (rnd.nextFloat() < config.paralyzeChance) {
            target.addStatusEffect(new StatusEffectInstance(RegisterEffects.STUN, config.paralyzeDuration, 0));
        }
    }
}
