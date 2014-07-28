package de.domi1819.invisiblights

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.registry.GameRegistry

import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack

@Mod(modid="InvisibLights", name="InvisibLights", version="2.0-4", modLanguage="scala")
object InvisibLights
{
  val blockLightSource = new BlockLightSource
  val itemLightRod = new ItemLightRod

  @EventHandler
  def init(event: FMLInitializationEvent)
  {
    GameRegistry.registerBlock(blockLightSource, "blockLightSource")
    GameRegistry.registerItem(itemLightRod, "itemLightRod")

    GameRegistry.addRecipe(new ItemStack(itemLightRod), Array ("G", "S", "D", 'G', Blocks.glowstone, 'S', Items.stick, 'D', Items.diamond) map { _.asInstanceOf[AnyRef] }: _*)
  }
}