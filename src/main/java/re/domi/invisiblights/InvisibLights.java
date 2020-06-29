package re.domi.invisiblights;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("WeakerAccess")
@Mod(InvisibLights.MOD_ID)
public class InvisibLights
{

    public static final String MOD_ID = "invisiblights";

    public static LightSourceBlock LIGHT_SOURCE;
    public static LightRodItem LIGHT_ROD;
    public static PoweredLightRodItem POWERED_LIGHT_ROD;

    public InvisibLights()
    {
        Config.load();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addGenericListener(Block.class, this::onBlocksRegister);
        bus.addGenericListener(Item.class, this::onItemsRegister);
    }

    private void onBlocksRegister(final RegistryEvent.Register<Block> blocks)
    {
        LIGHT_SOURCE = register(new LightSourceBlock(), blocks.getRegistry(), "light_source");
    }

    private void onItemsRegister(final RegistryEvent.Register<Item> items)
    {
        IForgeRegistry<Item> registry = items.getRegistry();

        register(new BlockItem(LIGHT_SOURCE, new Item.Properties().group(ItemGroup.DECORATIONS)), registry, "light_source");
        LIGHT_ROD = register(new LightRodItem(), registry, "light_rod");
        POWERED_LIGHT_ROD = register(new PoweredLightRodItem(), registry, "powered_light_rod");
    }

    private static <TInput extends TEntry, TEntry extends ForgeRegistryEntry<TEntry>> TInput register
            (TInput thing, IForgeRegistry<TEntry> registry, String name)
    {
        registry.register(thing.setRegistryName("invisiblights", name));
        return thing;
    }
}
