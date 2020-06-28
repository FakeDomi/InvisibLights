package re.domi.invisiblights;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

@SuppressWarnings({"deprecation", "WeakerAccess"})
public class LightSourceBlock extends Block implements Waterloggable
{
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");
    public static boolean LightSourcesHidden = true;

    public LightSourceBlock()
    {
        super(FabricBlockSettings.of(Material.AIR).hardness(0.2f).resistance(0.2f).lightLevel(15).collidable(false).nonOpaque());

        this.setDefaultState(this.getStateManager().getDefaultState().with(POWERED, false).with(Properties.WATERLOGGED, false));
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView view, BlockPos pos)
    {
        return 1F;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState neighbor, Direction facing)
    {
        return LightSourcesHidden || neighbor.getBlock() == this;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return LightSourcesHidden || world instanceof ServerWorld ? VoxelShapes.empty() : VoxelShapes.fullCube();
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
    {
        if (state.get(Properties.WATERLOGGED))
        {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        //noinspection ConstantConditions
        return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED);
        builder.add(Properties.WATERLOGGED);
    }
}
