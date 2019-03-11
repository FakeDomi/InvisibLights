package xyz.domi1819.invisiblights

import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.{Item, ItemBlock}
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.{EventBusSubscriber, EventHandler}
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.registries.{IForgeRegistry, IForgeRegistryEntry}

@EventBusSubscriber
@Mod(modid="invisiblights", name="InvisibLights", version="3.0-8", modLanguage="scala")
object InvisibLights {
  @EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    val config = new Configuration(event.getSuggestedConfigurationFile)
    config.load()

    val propGlowstoneCost = config.get("Tweaking", "GlowstoneMinCost", 2)
    propGlowstoneCost.setComment("The amount of Glowstone Dust needed to place a Light Source. Set to 0 to make it free.")
    GlowstoneCost = propGlowstoneCost.getInt(2)

    val propDisableBlockDrops = config.get("Tweaking", "DisableBlockDrops", false)
    propDisableBlockDrops.setComment("When this is enabled, Light Sources don't drop anything on removal")
    DisableBlockDrops = propDisableBlockDrops.getBoolean(false)

    if (config.hasChanged) {
      config.save()
    }
  }

  @SubscribeEvent
  def registerBlocks(event: RegistryEvent.Register[Block]) {
    BlockLightSource = register(new BlockLightSource, event.getRegistry, "light_source")
  }

  @SubscribeEvent
  def registerItems(event: RegistryEvent.Register[Item]) {
    val registry = event.getRegistry
    ItemBlockLightSource = register(new ItemBlock(BlockLightSource), registry, "light_source")
    ItemLightRod = register(new ItemLightRod, registry, "light_rod")
  }

  def register[TInput <: TEntry, TEntry <: IForgeRegistryEntry.Impl[TEntry]](input: TInput, registry: IForgeRegistry[TEntry], name: String): TInput = {
    registry.register(input.asInstanceOf[TEntry].setRegistryName("invisiblights", name))
    input
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  def registerModels(event: ModelRegistryEvent) {
    ModelLoader.setCustomModelResourceLocation(ItemBlockLightSource, 0, new ModelResourceLocation(ItemBlockLightSource.getRegistryName, "normal"))
    ModelLoader.setCustomModelResourceLocation(ItemLightRod, 0, new ModelResourceLocation(ItemLightRod.getRegistryName, "normal"))
  }
}
