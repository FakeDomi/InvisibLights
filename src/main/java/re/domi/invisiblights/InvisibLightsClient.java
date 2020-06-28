package re.domi.invisiblights;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@SuppressWarnings("unused")
public class InvisibLightsClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        BlockRenderLayerMap.INSTANCE.putBlock(InvisibLights.LightSource, RenderLayer.getTranslucent());
    }
}
