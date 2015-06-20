package net.domi1819.invisiblights

import java.util.Random

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.{IBlockAccess, World}

class BlockLightSource extends Block(Material.circuits)
{
  var visibleFlag = false
  setHardness(0.1F)
  setResistance(0.1F)
  setLightLevel(1)
  setCreativeTab(CreativeTabs.tabRedstone)
  setBlockName("blockLightSource")
  setBlockTextureName("invisiblights:light")
  setBlockUnbreakable()

  override def getSelectedBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB =
  {
    setBlockBoundsBasedOnState(world, x, y, z)
    if(visibleFlag) AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
    else AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0)
  }

  override def setBlockBoundsBasedOnState(world: IBlockAccess, x: Int, y: Int, z: Int)
  {
    if (visibleFlag) setBlockBounds(0, 0, 0, 1, 1, 1)
    else setBlockBounds(0, 0, 0, 0, 0, 0)
  }

  override def getCollisionBoundingBoxFromPool(world: World, x: Int, y: Int, z: Int): AxisAlignedBB =
  {
    setBlockBoundsBasedOnState(world, x, y, z)
    null
  }

  override def isReplaceable(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = true

  override def getItemDropped(i: Int, random: Random, j: Int): Item = Items.glowstone_dust

  override def getRenderBlockPass: Int = 1

  override def quantityDropped(rand: Random): Int = 0

  override def isOpaqueCube: Boolean = false

  override def shouldSideBeRendered(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean = world.getBlock(x, y, z) != this
}