package re.domi.invisiblights;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import re.domi.invisiblights.config.Config;

@SuppressWarnings("WeakerAccess")
public class InvisibLights implements ModInitializer
{
    public static BooleanProperty FROM_POWERED_ROD = BooleanProperty.of("from_powered_rod");

    public static Item LightRod;
    public static PoweredLightRodItem PoweredLightRod;
    public static Block LightSource;

    @Override
    public void onInitialize()
    {
        Config.read();

        LightRod = Registry.register(Registry.ITEM, new Identifier("invisiblights", "light_rod"), new LightRodItem());
        PoweredLightRod = Registry.register(Registry.ITEM, new Identifier("invisiblights", "powered_light_rod"), new PoweredLightRodItem());

        LightSource = Registry.register(Registry.BLOCK, new Identifier("invisiblights", "light_source"), new LightSourceBlock());
    }
}
