package xyz.domi1819.invisiblights

import java.util.Random

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.{BlockFaceShape, BlockStateContainer, IBlockState}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{BlockRenderLayer, EnumFacing}
import net.minecraft.world.{IBlockAccess, World, WorldServer}

class BlockLightSource extends Block(Material.CIRCUITS) {
  setHardness(0.2F)
  setResistance(0.2F)
  setLightLevel(1)
  setCreativeTab(CreativeTabs.REDSTONE)
  setTranslationKey("invisiblights.light_source")

  var hidden = true

  override def getItemDropped(state: IBlockState, random: Random, fortune: Int): Item = Items.GLOWSTONE_DUST

  override def quantityDropped(state: IBlockState, fortune: Int, random: Random): Int = if (DisableBlockDrops || state.getValue(PropertyPowered)) 0 else GlowstoneCost
  override def quantityDropped(rand: Random): Int = if (DisableBlockDrops) 0 else GlowstoneCost

  override def isReplaceable(world: IBlockAccess, pos: BlockPos): Boolean = true

  override def isOpaqueCube(state: IBlockState): Boolean = false
  override def getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing): BlockFaceShape = BlockFaceShape.UNDEFINED

  override def getRenderLayer: BlockRenderLayer = BlockRenderLayer.TRANSLUCENT
  override def shouldSideBeRendered(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = world.getBlockState(pos.offset(side)).getBlock != this

  override def getSelectedBoundingBox(state: IBlockState, world: World, pos: BlockPos): AxisAlignedBB = {
    if (hidden) HiddenAABB
    else Block.FULL_BLOCK_AABB.offset(pos)
  }

  override def getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB = {
    if (hidden || source.isInstanceOf[WorldServer]) HiddenAABB
    else Block.FULL_BLOCK_AABB
  }

  override def getCollisionBoundingBox(state: IBlockState, world: IBlockAccess, pos: BlockPos): AxisAlignedBB = Block.NULL_AABB

  override def createBlockState(): BlockStateContainer = new BlockStateContainer(this, PropertyHidden, PropertyPowered)
  override def getMetaFromState(state: IBlockState): Int = if (state.getValue(PropertyPowered)) 1 else 0
  override def getStateFromMeta(meta: Int): IBlockState = getDefaultState.withProperty(PropertyPowered, meta == 1: java.lang.Boolean)
  override def getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState = state.withProperty(PropertyHidden, hidden: java.lang.Boolean)
}
