package xyz.domi1819.invisiblights;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
public class LightSourceBlock extends Block implements IWaterLoggable {

    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static boolean isHidden;

    private final BlockItem blockItem;

    public LightSourceBlock() {
        super(Properties.create(Material.MISCELLANEOUS, DyeColor.YELLOW).doesNotBlockMovement().hardnessAndResistance(0.2F).lightValue(15).sound(SoundType.CLOTH));
        setRegistryName(InvisibLights.MOD_ID, "light_source");
        setDefaultState(getDefaultState().with(POWERED, false).with(WATERLOGGED, false));
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
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED, WATERLOGGED);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return super.getStateForPlacement(ctx).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.updatePostPlacement(state, facing, neighborState, world, pos, neighborPos);
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentState, Direction side) {
        return isHidden || adjacentState.getBlock() == this;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return isHidden || world instanceof ServerWorld ? VoxelShapes.empty() : VoxelShapes.fullCube();
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
