package de.domi1819.invisiblights

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemDarkRod extends ItemLightRod
{
  setUnlocalizedName("itemDarkRod")
  setTextureName("invisiblights:darkrod")

  override def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean =
  {
    if (player.isSneaking)
    {
      onItemRightClick(stack, world, player)
      return true
    }

    if (world.getBlock(x, y, z) == InvisibLights.blockLightSource)
    {
      world.setBlockToAir(x, y, z)
      world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.fizz", 0.5F, 1.5F)
    }

    !world.isRemote
  }
}
