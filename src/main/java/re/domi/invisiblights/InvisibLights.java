package re.domi.invisiblights;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@SuppressWarnings("WeakerAccess")
@Mod(InvisibLights.MOD_ID)
public class InvisibLights
{
    public static final String MOD_ID = "invisiblights";

    public static final LightSourceBlock LIGHT_SOURCE = new LightSourceBlock();
    public static final LightRodItem LIGHT_ROD = new LightRodItem();
    public static final PoweredLightRodItem POWERED_LIGHT_ROD = new PoweredLightRodItem();

    public InvisibLights()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.Forge.ConfigSpec);
        Config.Forge.load(Config.Forge.ConfigSpec, FMLPaths.CONFIGDIR.get().resolve("invisiblights-common.toml"));

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addGenericListener(Block.class, this::onBlocksRegister);
        bus.addGenericListener(Item.class, this::onItemsRegister);
        bus.addListener(this::onClientSetup);

    }

    private void onBlocksRegister(final RegistryEvent.Register<Block> blocks)
    {
        blocks.getRegistry().register(LIGHT_SOURCE);
    }

    private void onItemsRegister(final RegistryEvent.Register<Item> items)
    {
        items.getRegistry().register(LIGHT_SOURCE.getBlockItem());
        items.getRegistry().register(LIGHT_ROD);
        items.getRegistry().register(POWERED_LIGHT_ROD);
    }

    @SuppressWarnings({"deprecation", "unused"})
    private void onClientSetup(final FMLClientSetupEvent event)
    {
        DeferredWorkQueue.runLater(() -> RenderTypeLookup.setRenderLayer(LIGHT_SOURCE, RenderType.getTranslucent()));
    }
}
