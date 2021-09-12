package re.domi.invisiblights.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import re.domi.invisiblights.InvisibLights;

@Mixin(ClientWorld.class)
public class ClientWorldMixin
{
    @Inject(method = "getBlockParticle", at = @At("HEAD"), cancellable = true)
    private void getBlockParticle(CallbackInfoReturnable<ClientWorld.BlockParticle> cir)
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null)
        {
            Item mainHandItem = player.getMainHandStack().getItem();
            Item offHandItem = player.getOffHandStack().getItem();

            if (mainHandItem == InvisibLights.LightRod || offHandItem == InvisibLights.LightRod || mainHandItem == Items.GLOWSTONE_DUST)
            {
                cir.setReturnValue(ClientWorld.BlockParticle.LIGHT);
            }
        }
    }
}
