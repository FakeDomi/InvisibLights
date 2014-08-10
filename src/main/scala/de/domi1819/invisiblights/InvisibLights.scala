package de.domi1819.invisiblights

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.common.registry.GameRegistry

import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.{OreDictionary, ShapedOreRecipe}

@Mod(modid="InvisibLights", name="InvisibLights", version="2.2-6", modLanguage="scala")
object InvisibLights
{
  val blockLightSource = new BlockLightSource
  val itemLightRod = new ItemLightRod
  val itemDarkRod = new ItemDarkRod

  @EventHandler
  def init(event: FMLInitializationEvent)
  {
    GameRegistry.registerBlock(blockLightSource, "blockLightSource")
    GameRegistry.registerItem(itemLightRod, "itemLightRod")
    GameRegistry.registerItem(itemDarkRod, "itemDarkRod")

    OreDictionary.registerOre("stickWood", Items.stick)

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemLightRod), Array ("G", "S", "D", 'G', Blocks.glowstone, 'S', "stickWood", 'D', "gemDiamond") map { _.asInstanceOf[AnyRef] }: _*))
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemDarkRod), Array ("O", "S", 'O', Blocks.obsidian, 'S', "stickWood") map { _.asInstanceOf[AnyRef] }: _*))
  }
}