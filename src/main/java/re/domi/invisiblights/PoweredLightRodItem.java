package re.domi.invisiblights;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import re.domi.invisiblights.config.Config;
import team.reborn.energy.api.base.SimpleEnergyItem;

import java.util.List;

public class PoweredLightRodItem extends LightRodItem implements SimpleEnergyItem
{
    @Override
    public boolean canAffordLightSource(PlayerInventory inv, ItemStack heldItemStack)
    {
        return this.getStoredEnergy(heldItemStack) >= Config.PoweredLightSourceCost;
    }

    @Override
    public BlockState getPlacementBlockState(BlockState original)
    {
        if (original.getBlock() == Blocks.LIGHT)
        {
            original = InvisibLights.LightSource.getStateWithProperties(original);
        }

        return original.with(InvisibLights.FROM_POWERED_ROD, true);
    }

    @Override
    public void postPlace(PlayerInventory inv, ItemStack heldItemStack)
    {
        this.tryUseEnergy(heldItemStack, Config.PoweredLightSourceCost);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        long stored = this.getStoredEnergy(stack);
        long cap = this.getEnergyCapacity(stack);

        tooltip.add(Text.translatable("item.invisiblights.powered_light_rod.tooltip", stored, cap, (int)(100.0 * stored / cap)).formatted(Formatting.GRAY));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack)
    {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack)
    {
        return Math.round(13.0F * this.getStoredEnergy(stack) / this.getEnergyCapacity(stack));
    }

    @Override
    public int getItemBarColor(ItemStack stack)
    {
        float fillLevel = this.getStoredEnergy(stack) / (float)this.getEnergyCapacity(stack);
        return ColorHelper.Argb.getArgb(0, 128 + (int)(fillLevel * 127), 111 + (int)(fillLevel * 110), 60 + (int)(fillLevel * 59));
    }

    @Override
    public long getEnergyCapacity(ItemStack stack)
    {
        return Config.PoweredLightRodCapacity;
    }

    @Override
    public long getEnergyMaxInput(ItemStack stack)
    {
        return Config.PoweredLightRodChargeRate;
    }

    @Override
    public long getEnergyMaxOutput(ItemStack stack)
    {
        return 0;
    }
}