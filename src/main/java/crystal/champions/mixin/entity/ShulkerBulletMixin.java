package crystal.champions.mixin.entity;

import crystal.champions.IBullet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBulletEntity.class)
public abstract class ShulkerBulletMixin extends Entity implements IBullet {
    @Unique private static final TrackedData<Boolean> ARCTIC = DataTracker.registerData(ShulkerBulletMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique private static final TrackedData<Boolean> MOLTEN = DataTracker.registerData(ShulkerBulletMixin.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initChampionTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(ARCTIC, false);
        builder.add(MOLTEN, false);
    }

    @Override public void champions$setArctic(boolean arctic) {
        this.dataTracker.set(ARCTIC, arctic);
    }
    @Override public boolean champions$isArctic() {
        return this.dataTracker.get(ARCTIC);
    }
    @Override public void champions$setMolten(boolean molten) {
        this.dataTracker.set(MOLTEN, molten);
    }
    @Override public boolean champions$isMolten() {
        return this.dataTracker.get(MOLTEN);
    }

    protected ShulkerBulletMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Redirecting (changing original code of minecraft)
     * Added conditional to Arctic and Molten bullet
     */
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
            )
    )
    private void addCustomParticles(World world, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (champions$isArctic()) {
            world.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0, 0, 0);
        } else if (champions$isMolten()) {
            if (this.age % 2 == 0) world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
            world.addParticle(ParticleTypes.WHITE_ASH, x, y, z, 0, 0.2, 0);
        } else {
            world.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
        }
    }

    /**
     * Applying custom effects after hit
     * @param entityHitResult getting entity to check is it LivingEntity
     * @param ci CallbackInfo
     */
    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void championEffect(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (!(entityHitResult.getEntity() instanceof LivingEntity target)) return;

        if (champions$isArctic()) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 5));
            ci.cancel();
        }
        if (champions$isMolten()) {
            target.setOnFireFor(60);
            ci.cancel();
        }
    }

    /**
     * Writing state to bullet
     * @param nbt using nbt to save state of bullet
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeChampionData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("arctic", champions$isArctic());
        nbt.putBoolean("molten", champions$isMolten());
    }

    /**
     * Reading state of bullet
     * @param nbt reading nbt
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readChampionData(NbtCompound nbt, CallbackInfo ci) {
        champions$setArctic(nbt.getBoolean("arctic"));
        champions$setMolten(nbt.getBoolean("molten"));
    }
}