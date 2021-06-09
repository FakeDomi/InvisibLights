package re.domi.invisiblights.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin
{
    @Redirect(method = "<clinit>",
        slice = @Slice(from = @At(value = "NEW", target = "net/minecraft/block/LightBlock")),
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;strength(FF)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 0))
    private static AbstractBlock.Settings lightBlockStrengthCall(AbstractBlock.Settings settings, float hardness, float resistance)
    {
        return settings.strength(0.2F, resistance);
    }
}
