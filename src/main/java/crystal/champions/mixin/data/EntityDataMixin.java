package crystal.champions.mixin.data;

import crystal.champions.IChampions;
import crystal.champions.affix.Affix;
import crystal.champions.affix.AffixRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("java:S2160")
@Mixin(LivingEntity.class)
public abstract class EntityDataMixin extends Entity implements IChampions {
    @Unique private int tier = 0;
    @Unique private String affixes = "";
    @Unique private String adaptationType = "";
    @Unique private int adaptationCount = 0;
    @Unique private boolean isShielding = false;

    @Override
    public int champions$getChampionTier() {
        return tier;
    }
    @Override
    public void champions$setChampionTier(int value) {
        tier = value;
    }
    @Override
    public String champions$getAffixesString() {
        return affixes;
    }
    @Override
    public void champions$setAffixesString(String string) {
        affixes = string;
    }
    @Override
    public String champions$getAdaptationType() {
        return adaptationType;
    }
    @Override
    public void champions$setAdaptationType(String t) {
        adaptationType = t;
    }
    @Override
    public int champions$getAdaptation() {
        return adaptationCount;
    }
    @Override
    public void champions$setAdaptation(int value) {
        adaptationCount = value;
    }
    @Override
    public boolean champions$isShielding() {
        return isShielding;
    }
    @Override
    public void champions$setShielding(boolean bl) {
        isShielding = bl;
    }

    protected EntityDataMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onChampionTick(CallbackInfo ci) {
        if (this.getWorld().isClient) return;
        if (tier == 0 || affixes == null || affixes.isEmpty()) return;

        final LivingEntity entity = (LivingEntity) (Object) this;
        final List<Affix> affixList = Arrays.stream(affixes.split(","))
                .map(AffixRegistry.ALL_AFFIXES::get)
                .filter(Objects::nonNull)
                .toList();
        for (Affix affix : affixList) {
            affix.onTick(entity);
            if (entity instanceof MobEntity mob) affix.onAttack(entity, mob);
        }
        if (entity.age % 100 == 0) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, tier / 3, false, false, false));
        }
    }


    @SuppressWarnings("java:S1192")
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeChampionData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("tier", this.champions$getChampionTier());
        if (this.tier == 0) return;
        if (affixes != null) {
            nbt.putString("affixes", this.champions$getAffixesString());
        }
        nbt.putString("adaptationType", this.champions$getAdaptationType());
        nbt.putInt("adaptationCount", this.champions$getAdaptation());
    }

    @SuppressWarnings("java:S1192")
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readChampionData(NbtCompound nbt, CallbackInfo ci) {
        this.champions$setChampionTier(nbt.getInt("tier"));
        if (this.tier == 0) return;
        if (nbt.contains("affixes")) {
            this.champions$setAffixesString(nbt.getString("affixes"));
        }
        if (nbt.contains("adaptationType")) {
            this.champions$setAdaptationType(nbt.getString("adaptationType"));
        }
        if (nbt.contains("adaptationCount")) {
            this.champions$setAdaptation(nbt.getInt("adaptationCount"));
        }
    }
}