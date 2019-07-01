package xyz.domi1819.invisiblights

import ic2.api.item.IElectricItem
import ic2.api.item.ElectricItem
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Optional

@Optional.Interface(iface = "ic2.api.item.IElectricItem", modid = "ic2", striprefs = true)
class ItemElectricLightRod extends ItemLightRod with IElectricItem {
  setTranslationKey("invisiblights.light_rod_electric")

  def isClassicLoaded: Boolean = Loader.isModLoaded("ic2-classic-spmod")

  override def showDurabilityBar(stack: ItemStack): Boolean = isClassicLoaded || ElectricItem.manager.getCharge(stack) != this.getMaxCharge(stack)
  override def getDurabilityForDisplay(stack: ItemStack): Double = 1D - ElectricItem.manager.getCharge(stack) / getMaxCharge(stack)
  override def getRGBDurabilityForDisplay(stack: ItemStack): Int = {
    val durability = getDurabilityForDisplay(stack)
    MathHelper.rgb(255 - (durability * 127).toInt, 221 - (durability * 110).toInt, 119 - (durability * 59).toInt)
  }

  override def getSubItems(tab: CreativeTabs, items: NonNullList[ItemStack]): Unit = {
    if (isInCreativeTab(tab)) {
      val empty = new ItemStack(this)
      val full = new ItemStack(this)
      ElectricItem.manager.discharge(empty, Double.MaxValue, Int.MaxValue, true, false, false)
      ElectricItem.manager.charge(full, Double.MaxValue, Int.MaxValue, true, false)
      items.add(empty)
      items.add(full)
    }
  }

  override def canPlace(inv: InventoryPlayer, stack: ItemStack): Boolean = ElectricItem.manager.getCharge(stack) >= ElectricLightRodCost
  override def getPlaceMeta: Int = 1
  override def postPlace(inv: InventoryPlayer, stack: ItemStack): Unit = {
    ElectricItem.manager.use(stack, ElectricLightRodCost, inv.player)
  }

  //IElectricItem
  @Optional.Method(modid = "ic2")
  override def canProvideEnergy(itemStack: ItemStack): Boolean = false

  @Optional.Method(modid = "ic2")
  override def getMaxCharge(itemStack: ItemStack): Double = ElectricLightRodCapacity

  @Optional.Method(modid = "ic2")
  override def getTier(itemStack: ItemStack): Int = 1

  @Optional.Method(modid = "ic2")
  override def getTransferLimit(itemStack: ItemStack): Double = 200
}
