package re.domi.invisiblights;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
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

import javax.annotation.Nonnull;

@SuppressWarnings({"deprecation", "WeakerAccess"})
public class LightSourceBlock extends Block implements IWaterLoggable
{
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static boolean LightSourcesHidden = true;

    public LightSourceBlock()
    {
        super(Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0.2F).func_235838_a_(state -> 15).doesNotBlockMovement());
        this.setDefaultState(this.getDefaultState().with(POWERED, false).with(WATERLOGGED, false));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos)
    {
        return 1.0F;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentState, Direction side)
    {
        return LightSourcesHidden || adjacentState.getBlock() == this;
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return LightSourcesHidden || world instanceof ServerWorld ? VoxelShapes.empty() : VoxelShapes.fullCube();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos)
    {
        return state.getFluidState().isEmpty();
    }

    @Override
    public boolean isReplaceable(BlockState state, @Nonnull BlockItemUseContext useContext)
    {
        return true;
    }

    @Nonnull
    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Nonnull
    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos)
    {
        if (state.get(WATERLOGGED))
        {
            world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.updatePostPlacement(state, facing, neighborState, world, pos, neighborPos);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx)
    {
        return super.getStateForPlacement(ctx).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getPos()).getFluid() == Fluids.WATER);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED, WATERLOGGED);
    }
}
