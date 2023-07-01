package re.domi.invisiblights;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
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

        LightRod = Registry.register(Registries.ITEM, new Identifier("invisiblights", "light_rod"), new LightRodItem());
        PoweredLightRod = Registry.register(Registries.ITEM, new Identifier("invisiblights", "powered_light_rod"), new PoweredLightRodItem());

        LightSource = Registry.register(Registries.BLOCK, new Identifier("invisiblights", "light_source"), new LightSourceBlock());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries ->
        {
            entries.add(new ItemStack(LightRod));
            entries.add(new ItemStack(PoweredLightRod));

            ItemStack fullyCharged = new ItemStack(PoweredLightRod);
            PoweredLightRod.setStoredEnergy(fullyCharged, PoweredLightRod.getEnergyCapacity(fullyCharged));

            entries.add(fullyCharged);
        });
    }
}
