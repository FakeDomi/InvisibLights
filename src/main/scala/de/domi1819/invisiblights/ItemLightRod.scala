package de.domi1819.invisiblights

import net.minecraft.client.Minecraft
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.{InventoryPlayer, EntityPlayer}
import net.minecraft.init.Items
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.world.World

class ItemLightRod extends Item
{
  setCreativeTab(CreativeTabs.tabTools)
  setMaxStackSize(1)
  setFull3D
  setUnlocalizedName("itemLightRod")
  setTextureName("invisiblights:rod")

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack =
  {
    if (player.isSneaking && world.isRemote)
    {
      if (InvisibLights.blockLightSource.visibleFlag) InvisibLights.blockLightSource.visibleFlag = false
      else InvisibLights.blockLightSource.visibleFlag = true

      Minecraft.getMinecraft.renderGlobal.loadRenderers

      world.playSound(player.posX + 0.5, player.posY + 0.5, player.posZ + 0.5, "random.orb", 1, if (InvisibLights.blockLightSource.visibleFlag) 1 else 0.9F, false)
    }

    stack
  }

  override def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean =
  {
    if (player.isSneaking)
    {
      onItemRightClick(stack, world, player)
      return true
    }

    var aX = x
    var aY = y
    var aZ = z

    if (side == 0) aY = y - 1
    if (side == 1) aY = y + 1
    if (side == 2) aZ = z - 1
    if (side == 3) aZ = z + 1
    if (side == 4) aX = x - 1
    if (side == 5) aX = x + 1

    if ((player.capabilities.isCreativeMode || getItemCount(player.inventory, Items.glowstone_dust) >= InvisibLights.glowstoneMinCost) && world.canPlaceEntityOnSide(InvisibLights.blockLightSource, aX, aY, aZ, false, side, player, stack))
    {
      val meta = InvisibLights.blockLightSource.onBlockPlaced(world, aX, aY, aZ, side, hitX, hitY, hitZ, 0)
      if (placeBlockAt(stack, player, world, aX, aY, aZ, hitX, hitY, hitZ, meta))
      {
        world.playSoundEffect(aX + 0.5F, aY + 0.5F, aZ + 0.5F, InvisibLights.blockLightSource.stepSound.getBreakSound, 1, InvisibLights.blockLightSource.stepSound.getPitch * 0.8F)
        if (!player.capabilities.isCreativeMode) removeItems(player.inventory, Items.glowstone_dust, InvisibLights.glowstoneMaxCost)
        player.inventory.inventoryChanged
        if (!world.isRemote) return true
      }
    }

    false
  }

  def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, hitX: Float, hitY: Float, hitZ: Float, meta: Int): Boolean =
  {
    if (!world.setBlock(x, y, z, InvisibLights.blockLightSource, meta, 3)) return false

    if (world.getBlock(x, y, z) == InvisibLights.blockLightSource)
    {
      InvisibLights.blockLightSource.onBlockPlacedBy(world, x, y, z, player, stack)
      InvisibLights.blockLightSource.onPostBlockPlaced(world, x, y, z, meta)
    }

    true
  }

  def getItemCount(inv: InventoryPlayer, item: Item): Int =
  {
    var count = 0

    for (stack <- inv.mainInventory)
      if (stack != null && stack.getItem == item) count += stack.stackSize

    count
  }

  def removeItems(inv: InventoryPlayer, item: Item, count: Int)
  {
    var itemsLeft = count

    for (i <- 0 until inv.mainInventory.length)
    {
      val stack = inv.mainInventory(i)
      if (itemsLeft > 0 && stack != null && stack.getItem == item)
      {
        if (itemsLeft == stack.stackSize)
        {
          itemsLeft = 0
          inv.mainInventory(i) = null
        }
        else if (itemsLeft < stack.stackSize)
        {
          stack.stackSize -= itemsLeft
          itemsLeft = 0
        }
        else
        {
          itemsLeft -= stack.stackSize
          inv.mainInventory(i) = null
        }
      }
    }
  }
}
