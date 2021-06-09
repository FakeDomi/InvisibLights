package re.domi.invisiblights;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("WeakerAccess")
public class InvisibLights implements ModInitializer
{
    public static final int GLOWSTONE_COST = 2;

    public static Item LightRod;

    @Override
    public void onInitialize()
    {
        LightRod = Registry.register(Registry.ITEM, new Identifier("invisiblights", "light_rod"), new LightRodItem());
    }
}
