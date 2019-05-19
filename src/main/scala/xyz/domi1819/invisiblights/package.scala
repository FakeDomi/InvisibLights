package xyz.domi1819

import net.minecraft.block.properties.PropertyBool
import net.minecraft.item.Item
import net.minecraft.util.math.AxisAlignedBB

package object invisiblights {
  val PropertyHidden: PropertyBool = PropertyBool.create("hidden")
  val PropertyPowered: PropertyBool = PropertyBool.create("powered")
  val HiddenAABB = new AxisAlignedBB(0D, 0D, 0D, 0D, 0D, 0D)

  var BlockLightSource: BlockLightSource = _
  var ItemBlockLightSource: Item = _
  var ItemLightRod: Item = _
  var ItemLightRodPowered: Item = _

  var GlowstoneCost = 2
  var DisableBlockDrops = false

  var PoweredLightRodCapacity = 2560000
  var PoweredLightRodCost = 10000
}
