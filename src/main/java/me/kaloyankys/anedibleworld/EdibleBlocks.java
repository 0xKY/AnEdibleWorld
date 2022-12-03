package me.kaloyankys.anedibleworld;

import me.kaloyankys.anedibleworld.util.PlayerDuck;
import me.kaloyankys.anedibleworld.util.SpecialBehaviour;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Stream;

public class EdibleBlocks {
    private static final Random R = new Random();
    public static final ArrayList<EdibleBlock> EDIBLE_BLOCKS = new ArrayList<>();


    public static final EdibleBlock GRASS_BLOCK = setEdible(Blocks.GRASS_BLOCK, "grass_block", "Grass Block", null, new SpecialBehaviour() {
        @Override
        public void tick(World world, ItemStack stack, LivingEntity user, BlockPos pos) {
            if (world.getBlockState(pos.down()).isOf(Blocks.DIRT) && user.isSneaking()) {
                world.setBlockState(pos.down(), Blocks.GRASS_BLOCK.getDefaultState());
                BoneMealItem.createParticles(world, pos, R.nextInt(10) + 10);
            } else if (world.getBlockState(pos).isOf(Blocks.GRASS) && user.isSneaking()) {
                BoneMealItem.useOnFertilizable(new ItemStack(Items.BONE_MEAL), world, pos);
            }
        }
        @Override
        public void use(World world, ItemStack stack, LivingEntity user, BlockPos pos) {
        }
        @Override
        public void hit(World world, ItemStack stack, LivingEntity user, BlockPos pos) {
        }
        @Override
        public void die(World world, ItemStack stack, LivingEntity user, BlockPos pos) {

        }
        @Override
        public void special(World world, ItemStack stack, LivingEntity user, BlockPos pos) {

        }

        @Override
        public boolean damage(World world, ItemStack stack, LivingEntity user, BlockPos pos) {
            return false;
        }
    });


    public static final List<EdibleBlock> LOG_BLOCKS = setManyEdible(BlockTags.LOGS_THAT_BURN, "log_blocks", "Log Blocks", DamageSource.IN_FIRE, new SpecialBehaviour() {
        @Override
        public void tick(World world, ItemStack stack, LivingEntity user, BlockPos pos) {
        }
        @Override
        public void use(World world, ItemStack stack, LivingEntity user, BlockPos pos) {
        }
        @Override
        public void hit(World world, ItemStack stack, LivingEntity user, BlockPos pos) {
        }
        @Override
        public void die(World world, ItemStack stack, LivingEntity user, BlockPos pos) {

        }
        @Override
        public void special(World world, ItemStack stack, LivingEntity user, BlockPos pos) {

        }

        @Override
        public boolean damage(World world, ItemStack stack, LivingEntity user, BlockPos pos) {
            PlayerDuck mixin = (PlayerDuck) user;
            if (Objects.equals(mixin.getSkin(), "log_blocks")) {
                mixin.setSkin("stripped_log_blocks");
                return true;
            } return false;
        }
    });


    /**
     *
     * @param block The block to be set to edible
     * @param skin The name of the texture of the skin that the player is set to when the block is eaten
     * @param name The name of the block
     * @param behaviour The special behaviour after the block is eaten;
     * @return {@link EdibleBlock}
     *
     */
    private static EdibleBlock setEdible(Block block, String skin, String name, DamageSource weakness, SpecialBehaviour behaviour) {
        EdibleBlock edibleBlock = new EdibleBlock((BlockItem) block.asItem(), skin, name, weakness, behaviour);
        EDIBLE_BLOCKS.add(edibleBlock);
        return edibleBlock;
    }

    /**
     *
     * @param tag The tag which determines the set of blocks that are to be set to edible
     * @param skin The name of the texture of the skin that the player is set to when one the blocks in the set is eaten
     * @param name The name of the block set
     * @param behaviour The special behaviour after one the blocks in the set is eaten;
     * @return {@link EdibleBlock}
     *
     */
    private static List<EdibleBlock> setManyEdible(TagKey<Block> tag, String skin, String name, DamageSource weakness, SpecialBehaviour behaviour) {
        List<EdibleBlock> edibleSet = new ArrayList<>();
        Stream<RegistryEntry<Block>> tagStream = tagToRegistries(tag);
        if (tagStream != null) {
            tagStream.forEach((blockRegistryEntry -> {
                Block block = blockRegistryEntry.value();
                EdibleBlock edibleBlock = new EdibleBlock((BlockItem) block.asItem(), skin, name, weakness, behaviour);
                EDIBLE_BLOCKS.add(edibleBlock);
                edibleSet.add(edibleBlock);
            }));
        }
        return edibleSet;
    }

    /**
     *
     * @param blockItem {@link BlockItem} to be converted to {@link EdibleBlock}
     * @return {@link EdibleBlock}
     */
    public static EdibleBlock toEdible(BlockItem blockItem) {
        for (EdibleBlock edibleBlock : EDIBLE_BLOCKS) {
            if (edibleBlock.block() == blockItem) {
                return edibleBlock;
            }
        } return null;
    }

    /**
     *
     * @param skin {@link String} to be converted to {@link EdibleBlock}
     * @return {@link EdibleBlock}
     */
    public static EdibleBlock toEdible(String skin) {
        for (EdibleBlock edibleBlock : EDIBLE_BLOCKS) {
            if (Objects.equals(edibleBlock.skin(), skin)) {
                return edibleBlock;
            }
        } return null;
    }

    /**
     *
     * @param blockItem {@link BlockItem} to be checked
     * @return True or false depending on whether the {@link BlockItem} is edible
     */
    public static boolean isEdible(BlockItem blockItem) {
        for (EdibleBlock edibleBlock : EDIBLE_BLOCKS) {
            if (edibleBlock.block() == blockItem) {
                return true;
            }
        } return false;
    }

    /**
     *
     * @param tag {@link TagKey<Block>} to be converted to a stream
     * @return A {@link Stream<RegistryEntry>} of every {@link RegistryEntry<Block>} in the block tag
     */
    public static Stream<RegistryEntry<Block>> tagToRegistries(TagKey<Block> tag) {
        if (Registry.BLOCK.getEntryList(tag).isPresent()) {
            return Registry.BLOCK.getEntryList(tag).get().stream();
        } return null;
    }

    /**
     *
     * @param entity The entity to be checked
     * @return The last block the entity has eaten
     */
    public static EdibleBlock getBlockEaten(LivingEntity entity) {
        return EdibleBlocks.toEdible(((PlayerDuck)entity).getSkin());
    }
}
