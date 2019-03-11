package xyz.domi1819.invisiblights

import java.util.Random

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.{BlockStateContainer, IBlockState}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{BlockRenderLayer, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}

class BlockLightSource extends Block(Material.CIRCUITS) {
  setHardness(0.2F)
  setResistance(0.2F)
  setLightLevel(1)
  setCreativeTab(CreativeTabs.REDSTONE)
  setUnlocalizedName("invisiblights.light_source")

  var hidden = true

  override def getItemDropped(state: IBlockState, random: Random, fortune: Int): Item = Items.GLOWSTONE_DUST
  override def quantityDropped(rand: Random): Int = if (DisableBlockDrops) 0 else GlowstoneCost

  override def isReplaceable(world: IBlockAccess, pos: BlockPos): Boolean = true

  override def isOpaqueCube(state: IBlockState): Boolean = false
  override def getBlockLayer: BlockRenderLayer = BlockRenderLayer.TRANSLUCENT
  override def shouldSideBeRendered(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = world.getBlockState(pos.offset(side)).getBlock != this

  override def getSelectedBoundingBox(state: IBlockState, world: World, pos: BlockPos): AxisAlignedBB = {
    if (hidden) HiddenAABB
    else Block.FULL_BLOCK_AABB.offset(pos)
  }

  override def getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB = {
    if (hidden) HiddenAABB
    else Block.FULL_BLOCK_AABB
  }

  override def getCollisionBoundingBox(state: IBlockState, world: IBlockAccess, pos: BlockPos): AxisAlignedBB = Block.NULL_AABB

  override def createBlockState(): BlockStateContainer = new BlockStateContainer(this, PropertyHidden)
  override def getMetaFromState(state: IBlockState): Int = 0
  override def getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState = state.withProperty(PropertyHidden, hidden: java.lang.Boolean)
}
