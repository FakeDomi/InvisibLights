package net.domi1819.invisiblights

import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
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
      world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.fizz", 0.5F, 1.5F)

      if (!world.isRemote)
      {
        world.setBlockToAir(x, y, z)
        if (!InvisibLights.disableBlockDrops && !player.capabilities.isCreativeMode)
        {
          val item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, new ItemStack(Items.glowstone_dust, InvisibLights.glowstoneMinCost, 0))
          item.delayBeforeCanPickup = 10
          world.spawnEntityInWorld(item)
        }
      }
    }

    !world.isRemote
  }
}
