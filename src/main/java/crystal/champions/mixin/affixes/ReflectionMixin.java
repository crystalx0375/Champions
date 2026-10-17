package crystal.champions.mixin.affixes;

import crystal.champions.IChampions;
import crystal.champions.affix.Affix;
import crystal.champions.affix.AffixRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ReflectionMixin {
    /**
     * Adding random vector for velocity, playing sound
     * @param source checking for mob is not the source of damage
     * @param amount value of damage
     */
    @Inject(method = "damage", at = @At("TAIL"))
    private void applyReflection(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof IChampions champion && champion.champions$getAffixesString().contains("reflection")) {
            if (!(source.getAttacker() instanceof LivingEntity attacker) || source.getAttacker() == null) return;
            final Affix affix = AffixRegistry.ALL_AFFIXES.get("reflection");
            if (affix != null) {
                affix.onHurt(null, attacker);
            }
        }
    }
}
