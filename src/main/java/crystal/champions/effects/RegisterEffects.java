package crystal.champions.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class RegisterEffects {
    public static final RegistryEntry<StatusEffect> STUN = register(new Stun());
    private RegisterEffects() {}

    private static RegistryEntry<StatusEffect> register(StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("champions", "stun"), statusEffect);
    }
}
