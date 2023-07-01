package re.domi.invisiblights;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class InvisibLightsClient implements ClientModInitializer
{
    public static boolean LightSourcesHidden = true;

    @Override
    public void onInitializeClient()
    {
        BlockRenderLayerMap.INSTANCE.putBlock(InvisibLights.LightSource, RenderLayer.getTranslucent());
    }
}
