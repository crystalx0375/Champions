package crystal.champions.affix;

import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.Random;

public class ReflectionAffix extends Affix {
    private static final Random rnd = new Random();
    private static final ChampionsConfigAffixes config = ChampionsConfigAffixes.get();

    public ReflectionAffix() {
        super("reflection");
    }

    @Override
    public void onHurt(LivingEntity champion, LivingEntity attacker) {
        attacker.damage(attacker.getWorld().getDamageSources().thorns(attacker), config.reflectionDamage);
        final double rX = rnd.nextDouble(-0.15, 0.15);
        final double rZ = rnd.nextDouble(-0.15, 0.15);
        final double x = attacker.getVelocity().x + rX;
        final double z = attacker.getVelocity().z + rZ;
        attacker.takeKnockback(0.3, x, z);
        attacker.getWorld().playSound(
                null,
                attacker.getBlockPos(),
                SoundEvents.ENCHANT_THORNS_HIT,
                SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );
    }
}
