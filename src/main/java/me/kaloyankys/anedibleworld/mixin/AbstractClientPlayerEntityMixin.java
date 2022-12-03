package me.kaloyankys.anedibleworld.mixin;

import me.kaloyankys.anedibleworld.AnEdibleWorld;
import me.kaloyankys.anedibleworld.util.PlayerDuck;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;


@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends LivingEntity {
    public AbstractClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("RETURN"), method = "getSkinTexture", cancellable = true)
    protected void getSkinSus(CallbackInfoReturnable<Identifier> cir) {
        PlayerDuck mixin = (PlayerDuck) this;
        if (!Objects.equals(mixin.getSkin(), "")) {
            cir.setReturnValue(new Identifier(AnEdibleWorld.MOD_ID, "textures/entity/player/" + mixin.getSkin() + ".png"));
        }

    }

}
