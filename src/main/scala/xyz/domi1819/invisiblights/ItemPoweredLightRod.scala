package xyz.domi1819.invisiblights
import java.util

import net.minecraft.client.resources.I18n
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.MathHelper
import net.minecraft.util.{EnumFacing, NonNullList}
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.{Capability, ICapabilityProvider}
import net.minecraftforge.energy.{CapabilityEnergy, EnergyStorage}

class ItemPoweredLightRod extends ItemLightRod {
  setTranslationKey("invisiblights.light_rod_powered")

  override def initCapabilities(stack: ItemStack, nbt: NBTTagCompound): ICapabilityProvider = new ICapabilityProvider {
    override def hasCapability(capability: Capability[_], facing: EnumFacing): Boolean = capability == CapabilityEnergy.ENERGY
    override def getCapability[T](capability: Capability[T], facing: EnumFacing): T = {
      if (capability == CapabilityEnergy.ENERGY)
        new EnergyStorage(PoweredLightRodCapacity, Int.MaxValue, 0, getOrCreateTag(stack).getInteger("energy")) {
          override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
            val result = super.receiveEnergy(maxReceive, simulate)
            if (!simulate) {
              getOrCreateTag(stack).setInteger("energy", this.energy)
            }
            result
          }
        }.asInstanceOf[T]
      else
        null.asInstanceOf[T]
    }
  }

  override def addInformation(stack: ItemStack, worldIn: World, tooltip: util.List[String], flagIn: ITooltipFlag): Unit = {
    val energy = getOrCreateTag(stack).getInteger("energy")
    tooltip.add(I18n.format("tooltip.invisiblights.energy", energy.asInstanceOf[Integer], PoweredLightRodCapacity.asInstanceOf[Integer], (100 * energy / PoweredLightRodCapacity.toDouble).toInt.asInstanceOf[Integer]))
  }

  override def showDurabilityBar(stack: ItemStack): Boolean = true
  override def getDurabilityForDisplay(stack: ItemStack): Double = 1D - getOrCreateTag(stack).getInteger("energy") / PoweredLightRodCapacity.toDouble
  override def getRGBDurabilityForDisplay(stack: ItemStack): Int = {
    val durability = getDurabilityForDisplay(stack)
    MathHelper.rgb(255 - (durability * 127).toInt, 221 - (durability * 110).toInt, 119 - (durability * 59).toInt)
  }

  override def getSubItems(tab: CreativeTabs, items: NonNullList[ItemStack]): Unit = {
    if (isInCreativeTab(tab)) {
      items.add(new ItemStack(this))
      val stack = new ItemStack(this)
      getOrCreateTag(stack).setInteger("energy", PoweredLightRodCapacity)
      items.add(stack)
    }
  }

  override def canPlace(inv: InventoryPlayer, stack: ItemStack): Boolean = getOrCreateTag(stack).getInteger("energy") >= PoweredLightRodCost
  override def getPlaceMeta: Int = 1
  override def postPlace(inv: InventoryPlayer, stack: ItemStack): Unit = {
    val nbt = getOrCreateTag(stack)
    nbt.setInteger("energy", math.max(0, nbt.getInteger("energy") - PoweredLightRodCost))
  }

  def getOrCreateTag(stack: ItemStack): NBTTagCompound = {
    var nbt = stack.getTagCompound

    if (nbt == null) {
      nbt = new NBTTagCompound()
      nbt.setInteger("energy", 0)

      stack.setTagCompound(nbt)
    }

    nbt
  }
}
