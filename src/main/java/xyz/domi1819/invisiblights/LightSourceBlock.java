package xyz.domi1819.invisiblights;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LightSourceBlock extends Block {

    private static boolean isHidden;

    private final BlockItem blockItem;

    public LightSourceBlock() {
        super(Properties.create(Material.MISCELLANEOUS, DyeColor.YELLOW).hardnessAndResistance(0.2F).lightValue(15).sound(SoundType.CLOTH).variableOpacity());
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
    public boolean isAir(BlockState state) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return isHidden;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return context.hasItem(InvisibLights.LIGHT_ROD) || !isHidden && context != ISelectionContext.dummy() ? VoxelShapes.fullCube() : VoxelShapes.empty();
    }

    /*
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return isHidden ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }
     */

}
