package re.domi.invisiblights;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.network.play.server.SAnimateHandPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

@SuppressWarnings("WeakerAccess")
public class LightRodItem extends Item
{
    public LightRodItem(String name)
    {
        super(new Properties().group(ItemGroup.TOOLS).maxStackSize(1));
        this.setRegistryName(InvisibLights.MOD_ID, name);
    }

    public LightRodItem()
    {
        this("light_rod");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        if (world.isRemote && player.isSneaking())
        {
            LightSourceBlock.LightSourcesHidden = !LightSourceBlock.LightSourcesHidden;
            Minecraft.getInstance().worldRenderer.loadRenderers();

            world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.8F, LightSourceBlock.LightSourcesHidden ? 0.9F : 1, false);
        }

        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext ctx)
    {
        World world = ctx.getWorld();
        PlayerEntity player = ctx.getPlayer();

        if (world.isRemote || player == null || player.isSneaking())
        {
            return ActionResultType.PASS;
        }

        Direction side = ctx.getFace();
        BlockPos newPos = ctx.getPos().offset(side);
        Hand hand = ctx.getHand();
        ItemStack heldItemStack = player.getHeldItem(hand);

        if ((player.isCreative() || this.canAffordLightSource(player.inventory, heldItemStack))
                && player.canPlayerEdit(newPos, side, heldItemStack)
                && world.getBlockState(newPos).isReplaceable(new BlockItemUseContext(ctx))
                && world.getBlockState(newPos).getBlock() != InvisibLights.LIGHT_SOURCE)
        {
            BlockState state = this.getPlacementBlockState(InvisibLights.LIGHT_SOURCE.getStateForPlacement(new BlockItemUseContext(ctx)
            {
                private BlockPos realPos = newPos;

                @Override
                public BlockPos getPos()
                {
                    return this.realPos;
                }
            }));

            if (world.setBlockState(newPos, state))
            {
                SoundType soundType = state.getBlock().getSoundType(state, world, newPos, player);
                world.playSound(null, newPos, soundType.getPlaceSound(), SoundCategory.BLOCKS, (soundType.volume + 1F) / 2F, soundType.pitch * 0.8F);

                ((ServerWorld) world).getChunkProvider().sendToTrackingAndSelf(player, new SAnimateHandPacket(player, hand == Hand.MAIN_HAND ? 0 : 3));

                if (!player.isCreative())
                {
                    this.postPlace(player.inventory, heldItemStack);
                }

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    public boolean canAffordLightSource(PlayerInventory inv, ItemStack heldItemStack)
    {
        int invCount = 0;

        for (ItemStack stack : inv.mainInventory)
        {
            if (stack.getItem() == Items.GLOWSTONE_DUST)
            {
                invCount += stack.getCount();
                if (invCount >= 2)
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

    public void postPlace(PlayerInventory inv, ItemStack heldItemStack)
    {
        inv.clearMatchingItems(stack -> stack.getItem() == Items.GLOWSTONE_DUST, 2);
    }
}
