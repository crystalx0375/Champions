package crystal.champions.rank;

import crystal.champions.affix.AffixRegistry;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChampionFactory {
    private ChampionFactory() {}

    public static boolean isEligible(MobEntity mob) {
        final boolean isAgg = mob instanceof HostileEntity || mob instanceof Angerable
                || mob instanceof CaveSpiderEntity || mob instanceof GhastEntity
                || mob instanceof PhantomEntity || mob instanceof ShulkerEntity
                || mob instanceof SilverfishEntity || mob instanceof SlimeEntity;

        final boolean isException = mob instanceof EnderDragonEntity || mob instanceof WitherEntity
                || mob instanceof IronGolemEntity || mob instanceof PolarBearEntity
                || mob instanceof WolfEntity;

        return isAgg && !isException;
    }

    public static void applyRank(MobEntity mob, ChampionRank rank) {
        modifyAttribute(mob, EntityAttributes.GENERIC_MAX_HEALTH, rank.growth_h());
        mob.setHealth(mob.getMaxHealth());

        if (mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE) != null) {
            modifyAttribute(mob, EntityAttributes.GENERIC_ATTACK_DAMAGE, rank.growth_s());
        }
    }

    public static String pickAffixes(ChampionRank rank) {
        final List<String> pool = new ArrayList<>(AffixRegistry.ALL_AFFIXES.keySet());
        Collections.shuffle(pool);

        return String.join(",", pool.subList(0, Math.min(rank.affixes(), pool.size())));
    }

    private static void modifyAttribute(MobEntity entity, RegistryEntry<EntityAttribute> attribute, float m) {
        var instance = entity.getAttributeInstance(attribute);
        if (instance != null) {
            instance.setBaseValue(instance.getBaseValue() * m);
        }
    }
}
