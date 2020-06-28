package xyz.domi1819.invisiblights;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class LightRodItem extends Item {

    public LightRodItem() {
        super(new Properties().group(ItemGroup.TOOLS));
        setRegistryName(InvisibLights.MOD_ID, "light_rod");
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        if (world.isRemote || player == null || player.isCrouching()) {
            return ActionResultType.PASS;
        }
        Direction side = context.getFace();
        BlockPos pos = context.getPos().offset(side);
        BlockState state = world.getBlockState(pos);
        if (player.canPlayerEdit(pos, side, stack) && state.getMaterial().isReplaceable() && (player.isCreative() || this.canPlace(player))) {
            world.setBlockState(pos, InvisibLights.LIGHT_SOURCE.getDefaultState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
            world.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1, 0.9F);
            player.swing(context.getHand(), false);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (player.isCrouching()) {
            if (world.isRemote) {
                LightSourceBlock.setHidden(!LightSourceBlock.getHidden());
                Minecraft.getInstance().worldRenderer.setWorldAndLoadRenderers((ClientWorld) world); // straightforward
                world.playSound(player.getPosX() + 0.5, player.getPosY() + 0.5, player.getPosZ() + 0.5, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, LightSourceBlock.getHidden() ? 0.9F : 1, false);
            }
            player.swing(hand, false);
            return ActionResult.resultSuccess(player.getHeldItem(hand));
        }
        return ActionResult.resultPass(player.getHeldItem(hand));
    }

    private boolean canPlace(PlayerEntity player) {
        for (ItemStack invStack : player.inventory.mainInventory) {
            if (invStack.getItem() == Items.GLOWSTONE || invStack.getCount() >= 2) {
                invStack.shrink(2);
                return true;
            }
        }
        return false;
    }

}
