package crystal.champions.affix;

import crystal.champions.IChampions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import java.util.Optional;

public class AdaptiveAffix extends Affix {

    public AdaptiveAffix() {
        super("adaptive");
    }

    public float calculateDamage(LivingEntity entity, DamageSource source, float amount) {
        final IChampions champion = (IChampions) entity;
        final String lastType = champion.champions$getAdaptationType();
        final String currentType = (Optional.of(source.getTypeRegistryEntry()).get().getType().toString());

        if (source.getAttacker() instanceof LivingEntity || source.getSource() == champion && source.getAttacker() == champion) return amount;

        if (lastType.equals(currentType)) {
            final int count = champion.champions$getAdaptation();
            final float newAmount = amount - amount * count * 0.15f;
            final float minDamage = amount * 0.2f;

            champion.champions$setAdaptation(count + 1);
            return Math.max(newAmount, minDamage);
        } else {
            champion.champions$setAdaptationType(currentType);
            champion.champions$setAdaptation(1);
            return amount;
        }
    }
}
