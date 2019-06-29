package xyz.domi1819.invisiblights

import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.{Item, ItemBlock}
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.config.{Configuration, Property}
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.{Loader, Mod}
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

    GlowstoneCost = getConfigOption(config, "GlowstoneMinCost", GlowstoneCost, "The amount of Glowstone Dust needed to place a Light Source. Set to 0 to make it free.")
    DisableBlockDrops = getConfigOption(config, "DisableBlockDrops", DisableBlockDrops, "When this is enabled, Light Sources don't drop anything on removal.")

    PoweredLightRodCapacity = getConfigOption(config, "PoweredLightRodCapacity", PoweredLightRodCapacity, "How much Energy the powered Rod can hold.")
    PoweredLightRodCost = getConfigOption(config, "PoweredLightRodCost", PoweredLightRodCost, "The Energy required to place a light source.")

    if (config.hasChanged) {
      config.save()
    }
  }

  def getConfigOption[T](config: Configuration, name: String, default: T, desc: String): T = {
    var prop: Property = null
    var value: T = default

    default match {
      case v: Int => {
        prop = config.get("Tweaking", name, v)
        value = prop.getInt(v).asInstanceOf[T]
      }
      case v: Boolean => {
        prop = config.get("Tweaking", name, v)
        value = prop.getBoolean(v).asInstanceOf[T]
      }
      case _ => throw new RuntimeException("Property Type not supported")
    }

    prop.setComment(desc)
    value
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
    ItemLightRodPowered = register(new ItemPoweredLightRod, registry, "light_rod_powered")
    if(Loader.isModLoaded("ic2")){
      ItemLightRodElectric = register(new ItemElectricLightRod, registry, "light_rod_electric")
    }
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
    ModelLoader.setCustomModelResourceLocation(ItemLightRodPowered, 0, new ModelResourceLocation(ItemLightRodPowered.getRegistryName, "normal"))
    if(Loader.isModLoaded("ic2")){
      ModelLoader.setCustomModelResourceLocation(ItemLightRodElectric, 0, new ModelResourceLocation(ItemLightRodElectric.getRegistryName, "normal"))
    }
  }
}
