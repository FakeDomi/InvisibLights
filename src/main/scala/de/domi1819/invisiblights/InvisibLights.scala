package de.domi1819.invisiblights

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPreInitializationEvent, FMLInitializationEvent}
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.common.registry.GameRegistry

import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.oredict.{OreDictionary, ShapedOreRecipe}

@Mod(modid="InvisibLights", name="InvisibLights", version="2.3-7", modLanguage="scala")
object InvisibLights
{
  val blockLightSource = new BlockLightSource
  val itemLightRod = new ItemLightRod
  val itemDarkRod = new ItemDarkRod
  var (glowstoneMinCost, glowstoneMaxCost) = (1, 2)
  var (harderRecipes, disableBlockDrops) = (false, false)

  @EventHandler
  def preInit(event: FMLPreInitializationEvent)
  {
    val config = new Configuration(event.getSuggestedConfigurationFile)

    config.load

    val propGlowstoneMinCost = config.get("Tweaking", "GlowstoneMinCost", 1)
    propGlowstoneMinCost.comment = "The minimum amount of Glowstone Dust needed to place a Light Source"
    glowstoneMinCost = propGlowstoneMinCost.getInt(1)

    val propGlowstoneMaxCost = config.get("Tweaking", "GlowstoneMaxCost", 2)
    propGlowstoneMaxCost.comment = "The maximum amount of Glowstone Dust one Light Source can take to create"
    glowstoneMaxCost = propGlowstoneMaxCost.getInt(2)

    val propHarderRecipes = config.get("Tweaking", "HarderRecipes", false)
    propHarderRecipes.comment = "Makes the Rod of Light require a Diamond Block (instead of a single Item) and the Rod of Darkness require a Diamond"
    harderRecipes = propHarderRecipes.getBoolean(false)

    val propDisableBlockDrops = config.get("Tweaking", "DisableBlockDrops", false)
    propDisableBlockDrops.comment = "When this is enabled, Light Sources don't drop anything on removal"
    disableBlockDrops = propDisableBlockDrops.getBoolean(false)

    config.save
  }

  @EventHandler
  def init(event: FMLInitializationEvent)
  {
    GameRegistry.registerBlock(blockLightSource, "blockLightSource")
    GameRegistry.registerItem(itemLightRod, "itemLightRod")
    GameRegistry.registerItem(itemDarkRod, "itemDarkRod")

    OreDictionary.registerOre("stickWood", Items.stick)

    if (harderRecipes)
    {
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemLightRod), Array ("G", "S", "D", 'G', Blocks.glowstone, 'S', "stickWood", 'D', "blockDiamond") map { _.asInstanceOf[AnyRef] }: _*))
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemDarkRod), Array ("O", "S", "D", 'O', Blocks.obsidian, 'S', "stickWood", 'D', "gemDiamond") map { _.asInstanceOf[AnyRef] }: _*))
    }
    else
    {
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemLightRod), Array ("G", "S", "D", 'G', Blocks.glowstone, 'S', "stickWood", 'D', "gemDiamond") map { _.asInstanceOf[AnyRef] }: _*))
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemDarkRod), Array ("O", "S", 'O', Blocks.obsidian, 'S', "stickWood") map { _.asInstanceOf[AnyRef] }: _*))
    }
  }
}