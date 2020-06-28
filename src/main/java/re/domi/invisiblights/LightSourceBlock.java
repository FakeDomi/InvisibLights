package re.domi.invisiblights;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.Waterloggable;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityContext;
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
import net.minecraft.world.IWorld;

@SuppressWarnings({"deprecation", "WeakerAccess"})
public class LightSourceBlock extends Block implements Waterloggable
{
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");
    public static boolean LightSourcesHidden = true;

    public LightSourceBlock()
    {
        super(FabricBlockSettings.of(Material.AIR).hardness(0.2f).resistance(0.2f).lightLevel(15).collidable(false));

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
    public RenderLayer getRenderLayer()
    {
        return RenderLayer.TRANSLUCENT;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context)
    {
        return LightSourcesHidden || view instanceof ServerWorld ? VoxelShapes.empty() : VoxelShapes.fullCube();
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos)
    {
        if (state.get(Properties.WATERLOGGED))
        {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
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
