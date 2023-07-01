package re.domi.invisiblights.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import re.domi.invisiblights.LightRodItem;

@Mixin(ClientWorld.class)
public class ClientWorldMixin
{
    @Inject(method = "getBlockParticle", at = @At("HEAD"), cancellable = true)
    private void getBlockParticle(CallbackInfoReturnable<Block> cir)
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null)
        {
            Item mainHandItem = player.getMainHandStack().getItem();
            Item offHandItem = player.getOffHandStack().getItem();

            if (mainHandItem instanceof LightRodItem || offHandItem instanceof LightRodItem || mainHandItem == Items.GLOWSTONE_DUST)
            {
                cir.setReturnValue(Blocks.LIGHT);
            }
        }
    }
}
