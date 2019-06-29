package xyz.domi1819.invisiblights

import ic2.api.item.{ElectricItem, IElectricItem}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fml.common.Loader;

class ItemElectricLightRod extends ItemLightRod with IElectricItem{
  setTranslationKey("invisiblights.light_rod_electric")

  def isClassicLoaded(): Boolean = Loader.isModLoaded("ic2-classic-spmod")

  override def showDurabilityBar(stack: ItemStack): Boolean = if (isClassicLoaded) true else ElectricItem.manager.getCharge(stack) ne this.getMaxCharge(stack)
  override def getDurabilityForDisplay(stack: ItemStack): Double = 1D - ElectricItem.manager.getCharge(stack) / getMaxCharge(stack)
  override def getRGBDurabilityForDisplay(stack: ItemStack): Int = {
    val durability = getDurabilityForDisplay(stack)
    MathHelper.rgb(255 - (durability * 127).toInt, 221 - (durability * 110).toInt, 119 - (durability * 59).toInt)
  }

  override def getSubItems(tab: CreativeTabs, items: NonNullList[ItemStack]): Unit = {
    if (isInCreativeTab(tab)) {
      val empty = new ItemStack(this, 1, 0)
      val full = new ItemStack(this, 1, 0)
      ElectricItem.manager.discharge(empty, 2.147483647E9D, 2147483647, true, false, false)
      ElectricItem.manager.charge(full, 2.147483647E9D, 2147483647, true, false)
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
  override def canProvideEnergy(itemStack: ItemStack): Boolean = false;

  override def getMaxCharge(itemStack: ItemStack): Double = ElectricLightRodCapacity

  override def getTier(itemStack: ItemStack): Int = 1

  override def getTransferLimit(itemStack: ItemStack): Double = 200


}
