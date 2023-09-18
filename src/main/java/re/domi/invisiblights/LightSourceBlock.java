package re.domi.invisiblights;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import re.domi.invisiblights.config.Config;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class LightSourceBlock extends Block implements Waterloggable
{
    public LightSourceBlock()
    {
        super(FabricBlockSettings.of(Material.AIR).hardness(0.2F).resistance(0.2F).luminance(15).noCollision());
        this.setDefaultState(this.getStateManager().getDefaultState().with(InvisibLights.FROM_POWERED_ROD, false).with(Properties.WATERLOGGED, false));
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos)
    {
        return 1.0F;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction)
    {
        return InvisibLightsClient.LightSourcesHidden || stateFrom.getBlock() == this;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return InvisibLightsClient.LightSourcesHidden || world instanceof ServerWorld ? VoxelShapes.empty() : VoxelShapes.fullCube();
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos)
    {
        return !state.get(Properties.WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos)
    {
        if (state.get(Properties.WATERLOGGED))
        {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder)
    {
        List<ItemStack> drops = new ArrayList<>(1);

        if (!state.get(InvisibLights.FROM_POWERED_ROD) && Config.LightSourceGlowstoneCost > 0)
        {
            drops.add(new ItemStack(Items.GLOWSTONE_DUST, Config.LightSourceGlowstoneCost));
        }

        return drops;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(InvisibLights.FROM_POWERED_ROD, Properties.WATERLOGGED);
    }
}
