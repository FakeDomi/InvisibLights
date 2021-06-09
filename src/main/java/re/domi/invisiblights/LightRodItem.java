package re.domi.invisiblights;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@SuppressWarnings("WeakerAccess")
public class LightRodItem extends Item
{
    public LightRodItem()
    {
        super(new Settings().group(ItemGroup.TOOLS).maxCount(1));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context)
    {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (world.isClient || player == null)
        {
            return ActionResult.PASS;
        }

        Direction side = context.getSide();
        BlockPos newPos = context.getBlockPos().offset(side);
        Hand hand = context.getHand();
        ItemStack heldItemStack = player.getStackInHand(hand);

        if ((player.isCreative() || this.canAffordLightSource(player.getInventory(), heldItemStack))
                && player.canPlaceOn(newPos, side, heldItemStack)
                && world.getBlockState(newPos).canReplace(new ItemPlacementContext(context))
                && world.getBlockState(newPos).getBlock() != Blocks.LIGHT)
        {
            BlockState state = this.getPlacementBlockState(Blocks.LIGHT.getPlacementState(new ItemPlacementContext(context)
            {
                private final BlockPos realPos = newPos;

                @Override
                public BlockPos getBlockPos()
                {
                    return this.realPos;
                }
            }));

            if (world.setBlockState(newPos, state, 11))
            {
                BlockSoundGroup soundGroup = state.getBlock().getSoundGroup(state);
                world.playSound(null, newPos, soundGroup.getPlaceSound(), SoundCategory.BLOCKS, (soundGroup.volume + 1F) / 2F, soundGroup.pitch * 0.8F);

                ((ServerWorld) world).getChunkManager().sendToNearbyPlayers(player, new EntityAnimationS2CPacket(player, hand == Hand.MAIN_HAND ? 0 : 3));

                if (!player.isCreative())
                {
                    this.postPlace(player.getInventory(), heldItemStack);
                }

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    @SuppressWarnings("unused")
    public boolean canAffordLightSource(PlayerInventory inv, ItemStack heldItemStack)
    {
        int invCount = 0;

        for (ItemStack stack : inv.main)
        {
            if (stack.getItem() == Items.GLOWSTONE_DUST)
            {
                invCount += stack.getCount();
                if (invCount >= InvisibLights.GLOWSTONE_COST)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public BlockState getPlacementBlockState(BlockState original)
    {
        return original;
    }

    @SuppressWarnings("unused")
    public void postPlace(PlayerInventory inv, ItemStack heldItemStack)
    {
        int remaining = InvisibLights.GLOWSTONE_COST;

        for (int i = 0; i < inv.main.size(); i++)
        {
            ItemStack stack = inv.main.get(i);

            if (stack.getItem() == Items.GLOWSTONE_DUST)
            {
                if (stack.getCount() > remaining)
                {
                    stack.decrement(remaining);
                    remaining = 0;
                }
                else
                {
                    remaining -= stack.getCount();
                    stack = ItemStack.EMPTY;
                }

                inv.main.set(i, stack);

                if (remaining == 0)
                {
                    return;
                }
            }
        }
    }
}
