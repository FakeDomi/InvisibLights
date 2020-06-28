package xyz.domi1819.invisiblights;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
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

public class LightRodItem extends Item {

    public LightRodItem(String name) {
        super(new Properties().group(ItemGroup.TOOLS).maxStackSize(1));
        setRegistryName(InvisibLights.MOD_ID, name);
    }

    public LightRodItem() {
        this("light_rod");
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        PlayerEntity player = ctx.getPlayer();
        World world = ctx.getWorld();
        if (world.isRemote || player == null || player.isCrouching()) {
            return ActionResultType.PASS;
        }
        return place(ctx);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (player.isCrouching()) {
            if (world.isRemote) {
                LightSourceBlock.setHidden(!LightSourceBlock.getHidden());
                Minecraft.getInstance().worldRenderer.loadRenderers();
                world.playSound(player.getPosX() + 0.5, player.getPosY() + 0.5, player.getPosZ() + 0.5, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.8F, LightSourceBlock.getHidden() ? 0.9F : 1, false);
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

    @SuppressWarnings("ConstantConditions")
    private ActionResultType place(ItemUseContext ctx) {
        PlayerEntity player = ctx.getPlayer();
        BlockPos pos = ctx.getPos();
        Direction side = ctx.getFace();
        if (player.canPlayerEdit(pos, side, ctx.getItem()) && (player.isCreative() || this.canPlace(player))) {
            World world = ctx.getWorld();
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() != InvisibLights.LIGHT_SOURCE && state.isReplaceable(new BlockItemUseContext(ctx))) {
                world.setBlockState(pos, InvisibLights.LIGHT_SOURCE.getDefaultState());
            }
            else {
                BlockPos offset = pos.offset(side);
                BlockState offsetState = world.getBlockState(offset);
                if (offsetState.getBlock() != InvisibLights.LIGHT_SOURCE && offsetState.isReplaceable(new BlockItemUseContext(ctx))) {
                    world.setBlockState(offset, InvisibLights.LIGHT_SOURCE.getDefaultState());
                }
            }
            SoundType type = InvisibLights.LIGHT_SOURCE.getSoundType(state, world, pos, player);
            world.playSound(null, pos, type.getPlaceSound(), SoundCategory.BLOCKS, (type.volume + 1F) / 2F, type.pitch * 0.8F);
            player.swing(ctx.getHand(), false);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

}
