package crystal.champions.mixin.entity;

import crystal.champions.IChampions;
import crystal.champions.rank.ChampionRank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static crystal.champions.rank.ChampionFactory.pickAffixes;
import static crystal.champions.rank.ChampionFactory.applyRank;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends Entity {
    protected SlimeEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * After deaths of slime adding chance to spawn champion to smaller slime
     * @param slimeEntity smaller slime
     */
    @Inject(
            method = "remove",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void applyChampions(RemovalReason reason, CallbackInfo ci, int i, Text text, boolean bl, float f, float g, int j, int k, int l, float h, float m, SlimeEntity slimeEntity) {
        final IChampions child = (IChampions) slimeEntity;
        final ChampionRank rank = ChampionRank.roll(slimeEntity.getRandom());
        if (rank.tier() > 0) {
            child.champions$setChampionTier(rank.tier());
            child.champions$setAffixesString(pickAffixes(rank));
            applyRank(slimeEntity, rank);
        }
    }
}