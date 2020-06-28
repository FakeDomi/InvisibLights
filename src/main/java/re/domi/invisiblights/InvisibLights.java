package re.domi.invisiblights;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InvisibLights implements ModInitializer
{
    public static Block LightSource;
    public static Item LightRod;

    @Override
    public void onInitialize()
    {
        LightSource = Registry.register(Registry.BLOCK, new Identifier("invisiblights", "light_source"), new LightSourceBlock());
        Registry.register(Registry.ITEM, new Identifier("invisiblights", "light_source"), new BlockItem(LightSource, new Item.Settings().group(ItemGroup.DECORATIONS)));

        LightRod = Registry.register(Registry.ITEM, new Identifier("invisiblights", "light_rod"), new LightRodItem());
    }
}
