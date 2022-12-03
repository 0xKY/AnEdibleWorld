package me.kaloyankys.anedibleworld;

import me.kaloyankys.anedibleworld.util.SpecialBehaviour;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;

public record EdibleBlock(BlockItem block, String skin, String name, DamageSource weakness, SpecialBehaviour behaviour) {
    public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder().alwaysEdible().hunger(0).saturationModifier(0).build();
}
