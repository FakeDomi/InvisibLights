package xyz.domi1819.invisiblights;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class LightRodItem extends Item {

    public LightRodItem() {
        super(new Properties().group(ItemGroup.TOOLS));
        setRegistryName(InvisibLights.MOD_ID, "light_rod");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            return ActionResult.resultPass(player.getHeldItem(hand));
        }
        else if (player.isCrouching()) {
            LightSourceBlock.setHidden(!LightSourceBlock.getHidden());
            Minecraft.getInstance().worldRenderer.setWorldAndLoadRenderers((ClientWorld) world); // straightforward
            return ActionResult.resultSuccess(player.getHeldItem(hand));
        }
        return ActionResult.resultPass(player.getHeldItem(hand));
    }

}
