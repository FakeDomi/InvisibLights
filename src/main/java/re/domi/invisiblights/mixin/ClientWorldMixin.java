package re.domi.invisiblights.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
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
        if (player != null && (player.getMainHandStack().getItem() == InvisibLights.LightRod || player.getOffHandStack().getItem() == InvisibLights.LightRod))
        {
            cir.setReturnValue(ClientWorld.BlockParticle.LIGHT);
        }
    }
}
