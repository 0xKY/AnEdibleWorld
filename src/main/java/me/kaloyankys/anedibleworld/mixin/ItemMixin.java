package me.kaloyankys.anedibleworld.mixin;

import me.kaloyankys.anedibleworld.AnEdibleWorld;
import me.kaloyankys.anedibleworld.EdibleBlock;
import me.kaloyankys.anedibleworld.EdibleBlocks;
import me.kaloyankys.anedibleworld.util.PlayerDuck;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemConvertible {
    @Shadow public abstract Item asItem();

    @Inject(at = @At("HEAD"), method = "finishUsing")
    public void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        PlayerDuck mixin = (PlayerDuck) user;
        try {
            BlockItem block = (BlockItem) stack.getItem();
            if (EdibleBlocks.isEdible(block)) {
                EdibleBlock edibleBlock = EdibleBlocks.toEdible(block);
                mixin.setSkin(edibleBlock.skin());
            }
        } catch (Exception e) {
            AnEdibleWorld.LOGGER.warn(stack.getName() + " is not a BlockItem.");
        }
    }

    @Inject(at = @At("HEAD"), method = "getUseAction", cancellable = true)
    public void getUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
        try {
            if (EdibleBlocks.isEdible((BlockItem) stack.getItem())) {
                cir.setReturnValue(UseAction.EAT);
            }
        } catch (Exception e) {
            AnEdibleWorld.LOGGER.warn(stack.getName() + " is not a BlockItem.");
        }
    }

    @Inject(at = @At("HEAD"), method = "getMaxUseTime", cancellable = true)
    public void getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        try {
            if (EdibleBlocks.isEdible((BlockItem) stack.getItem())) {
                cir.setReturnValue(40);
            }
        } catch (Exception e) {
            AnEdibleWorld.LOGGER.warn(stack.getName() + " is not a BlockItem.");
        }
    }
    @Inject(at = @At("HEAD"), method = "isFood", cancellable = true)
    private void isFood(CallbackInfoReturnable<Boolean> cir) {
        if (this.asItem() instanceof BlockItem block && EdibleBlocks.isEdible(block)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "getFoodComponent", cancellable = true)
    private void getFoodComponent(CallbackInfoReturnable<FoodComponent> cir) {
        if (this.asItem() instanceof BlockItem block && EdibleBlocks.isEdible(block)) {
            cir.setReturnValue(EdibleBlock.FOOD_COMPONENT);
        }
    }
}
