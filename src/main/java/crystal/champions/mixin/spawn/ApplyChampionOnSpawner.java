package crystal.champions.mixin.spawn;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import crystal.champions.IChampions;
import crystal.champions.rank.ChampionRank;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static crystal.champions.rank.ChampionFactory.*;

@Mixin(MobSpawnerLogic.class)
public class ApplyChampionOnSpawner {
    @WrapOperation(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private boolean initChampionsOrPass(ServerWorld instance, Entity entity, Operation<Boolean> original) {
        if (entity instanceof MobEntity mobEntity
                && mobEntity instanceof IChampions i
                && isEligible(mobEntity))
        {
            final ChampionRank rank = ChampionRank.roll(mobEntity.getRandom());
            if (rank.tier() > 0)
            {
                i.champions$setChampionTier(rank.tier());
                applyRank(mobEntity, rank);
                i.champions$setAffixesString(pickAffixes(rank));
            }
        }

        return original.call(instance, entity);
    }
}
