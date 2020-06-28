package re.domi.invisiblights;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Collections;
import java.util.List;

@SuppressWarnings({"deprecation", "WeakerAccess"})
public class LightSourceBlock extends Block
{
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");
    public static boolean LightSourcesHidden = true;

    public LightSourceBlock()
    {
        super(FabricBlockSettings.of(Material.AIR).hardness(0.2f).resistance(0.2f).lightLevel(15));

        this.setDefaultState(this.getStateManager().getDefaultState().with(POWERED, false));
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
        return LightSourcesHidden ? VoxelShapes.empty() : VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, EntityContext context)
    {
        return VoxelShapes.empty();
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder)
    {
        return state.get(POWERED) ? Collections.emptyList() : Collections.singletonList(new ItemStack(Items.GLOWSTONE_DUST, 2));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED);
    }
}
