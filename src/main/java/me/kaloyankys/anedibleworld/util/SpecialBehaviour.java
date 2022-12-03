package me.kaloyankys.anedibleworld.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface SpecialBehaviour {
    void tick(World world, ItemStack stack, LivingEntity user, BlockPos pos);
    void use(World world, ItemStack stack, LivingEntity user, BlockPos pos);
    void hit(World world, ItemStack stack, LivingEntity user, BlockPos pos);
    void die(World world, ItemStack stack, LivingEntity user, BlockPos pos);
    void special(World world, ItemStack stack, LivingEntity user, BlockPos pos);
    boolean damage(World world, ItemStack stack, LivingEntity user, BlockPos pos);
}
