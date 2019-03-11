package xyz.domi1819.invisiblights

import scala.collection.JavaConverters._
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.{EntityPlayer, InventoryPlayer}
import net.minecraft.init.{Items, SoundEvents}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util._
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemLightRod extends Item {
  setCreativeTab(CreativeTabs.TOOLS)
  setMaxStackSize(1)
  setUnlocalizedName("invisiblights.light_rod")

  override def onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult[ItemStack] = {
    if (world.isRemote && player.isSneaking) {
      BlockLightSource.hidden = !BlockLightSource.hidden
      Minecraft.getMinecraft.renderGlobal.loadRenderers()
      world.playSound(player.posX + 0.5, player.posY + 0.5, player.posZ + 0.5, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1, if (BlockLightSource.hidden) 0.9F else 1, false)
    }

    new ActionResult[ItemStack](EnumActionResult.SUCCESS, player.getHeldItem(hand))
  }

  override def onItemUseFirst(player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, hand: EnumHand): EnumActionResult = {
    if (world.isRemote || player.isSneaking) {
      return EnumActionResult.FAIL
    }

    val newPos = pos.offset(side)
    val stack = player.getHeldItem(hand)

    if ((player.isCreative || hasEnoughItems(player.inventory, Items.GLOWSTONE_DUST, GlowstoneCost))
      && player.canPlayerEdit(newPos, side, stack) && world.mayPlace(BlockLightSource, newPos, false, side, player)) {
      var state = BlockLightSource.getStateForPlacement(world, newPos, side, hitX, hitY, hitZ, 0, player, hand)
      if (placeBlockAt(stack, player, world, newPos, side, hitX, hitY, hitZ, state)) {
        state = world.getBlockState(newPos)
        val soundType = state.getBlock.getSoundType(state, world, newPos, player)
        world.playSound(null, newPos, soundType.getPlaceSound, SoundCategory.BLOCKS, (soundType.getVolume + 1) / 2, soundType.getPitch * 0.8F)

        if (!player.isCreative && GlowstoneCost > 0) {
          player.inventory.clearMatchingItems(Items.GLOWSTONE_DUST, 0, GlowstoneCost, null)
        }

        return EnumActionResult.SUCCESS
      }
    }

    EnumActionResult.FAIL
  }

  def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, newState: IBlockState): Boolean = {
    if (!world.setBlockState(pos, newState, 11)) return false

    val state = world.getBlockState(pos)
    if (state.getBlock == BlockLightSource) {
      BlockLightSource.onBlockPlacedBy(world, pos, state, player, stack)
    }

    true
  }

  def hasEnoughItems(inv: InventoryPlayer, item: Item, count: Int): Boolean = {
    if (count <= 0) return true

    var invCount = 0
    for (stack: ItemStack <- inv.mainInventory.iterator().asScala) {
      if (stack != null && stack.getItem == item) {
        invCount += stack.getCount
        if (invCount >= count) {
          return true
        }
      }
    }

    false
  }
}
