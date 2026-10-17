package crystal.champions.mixin.affixes;

import crystal.champions.IChampions;
import crystal.champions.config.ChampionsConfigAffixes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class DampeningMixin {
    @Unique private static final ChampionsConfigAffixes configAffixes = ChampionsConfigAffixes.get();
    
    // Changing damage of dampening
    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float applyDampening(float amount, DamageSource source) {
        if (this instanceof IChampions champion
                && champion.champions$getAffixesString().contains("dampening")
                && (!source.isIn(DamageTypeTags.IS_PROJECTILE)
                || !source.isIn(DamageTypeTags.IS_FIRE))
        ) {
            return amount * configAffixes.dampeningAmount;
        }
        return amount;
    }
}
