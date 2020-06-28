package xyz.domi1819.invisiblights;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;

public class LightSourceBlock extends Block {

    private static boolean isHidden;

    private final BlockItem blockItem;

    public LightSourceBlock() {
        super(Properties.create(Material.MISCELLANEOUS, DyeColor.YELLOW).doesNotBlockMovement().hardnessAndResistance(0.2F).lightValue(15).sound(SoundType.CLOTH));
        setRegistryName(InvisibLights.MOD_ID, "light_source");
        this.blockItem = new BlockItem(this, new Item.Properties().group(ItemGroup.DECORATIONS));
        this.blockItem.setRegistryName(InvisibLights.MOD_ID, "light_source");
    }

    public BlockItem getBlockItem() {
        return blockItem;
    }

    protected static boolean getHidden() {
        return isHidden;
    }

    protected static void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(new ItemStack(Items.GLOWSTONE_DUST, 2));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentState, Direction side) {
        return isHidden || adjacentState.getBlock() == this;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return isHidden ? VoxelShapes.empty() : VoxelShapes.fullCube();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

}
