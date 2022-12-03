package me.kaloyankys.anedibleworld.mixin;

import me.kaloyankys.anedibleworld.EdibleBlock;
import me.kaloyankys.anedibleworld.EdibleBlocks;
import me.kaloyankys.anedibleworld.util.PlayerDuck;
import me.kaloyankys.anedibleworld.util.SpecialBehaviour;
import me.kaloyankys.anedibleworld.client.AnEdibleWorldClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerDuck {
    private static final TrackedData<String> SKIN = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.STRING);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        SpecialBehaviour behaviour = this.getBehaviour();
        EdibleBlock eaten = EdibleBlocks.getBlockEaten(this);
        if (behaviour != null && eaten != null) {
            behaviour.tick(this.world, eaten.block().getDefaultStack(), this, this.getBlockPos());
            if (AnEdibleWorldClient.R.wasPressed()) {
                behaviour.special(this.world, EdibleBlocks.getBlockEaten(this).block().getDefaultStack(), this, this.getBlockPos());
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "initDataTracker")
    private void initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(SKIN, "");
    }

    private SpecialBehaviour getBehaviour() {
        EdibleBlock eaten = EdibleBlocks.getBlockEaten(this);
        if (!Objects.equals(this.getSkin(), "") && eaten != null) {
            return eaten.behaviour();
        } return null;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        EdibleBlock eaten = EdibleBlocks.getBlockEaten(this);
        if (eaten != null && this.getBehaviour() != null) {
            if (source == eaten.weakness()) {
                return super.damage(source, amount * 3f);
            } else if (this.getBehaviour().damage(this.world, eaten.block().getDefaultStack(), this, this.getBlockPos())) {
                return super.damage(source, 0.0f);
            }
        }
        return super.damage(source, amount);
    }

    @Override
    public String getSkin() {
        return this.dataTracker.get(SKIN);
    }

    @Override
    public void setSkin(String skin) {
        this.dataTracker.set(SKIN, skin);
    }
}
