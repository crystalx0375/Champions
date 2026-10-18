package crystal.champions.mixin.entity;

import crystal.champions.IChampions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"java:S110", "java:S2160"})
@Mixin(CreeperEntity.class)
public class CreeperEntityMixin extends HostileEntity {
    @Shadow private int explosionRadius;
    @Shadow private int fuseTime = 30;

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "explode", at = @At("HEAD"))
    private void increaseExplosionRadius(CallbackInfo ci) {
        if (this instanceof IChampions champion) {
            final int tier = champion.champions$getChampionTier();
            if (tier > 0) {
                this.explosionRadius = (int) (this.explosionRadius * (float) tier);
            }
            if (tier == 5) {
                this.explosionRadius = this.explosionRadius * 2;
            }
        }
    }
}
