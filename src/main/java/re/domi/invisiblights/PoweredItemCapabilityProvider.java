package re.domi.invisiblights;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public class PoweredItemCapabilityProvider implements ICapabilityProvider
{
    private LazyOptional<IEnergyStorage> energyStorage;

    public PoweredItemCapabilityProvider(int capacity, ItemStack stack)
    {
        this.energyStorage = LazyOptional.of(() -> new ItemEnergyStorage(capacity, stack));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return cap == CapabilityEnergy.ENERGY ? this.energyStorage.cast() : LazyOptional.empty();
    }

    private static class ItemEnergyStorage implements IEnergyStorage
    {
        private int capacity;
        private ItemStack stack;

        private ItemEnergyStorage(int capacity, ItemStack stack)
        {
            this.capacity = capacity;
            this.stack = stack;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            int energyStored = this.getEnergyStored();
            int energyReceived = Math.min(this.capacity - energyStored, maxReceive);

            if (!simulate)
            {
                this.stack.getOrCreateTag().putInt("energy", energyStored + energyReceived);
            }

            return energyReceived;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            return 0;
        }

        @Override
        public int getEnergyStored()
        {
            return this.stack.getOrCreateTag().getInt("energy");
        }

        @Override
        public int getMaxEnergyStored()
        {
            return this.capacity;
        }

        @Override
        public boolean canReceive()
        {
            return true;
        }

        @Override
        public boolean canExtract()
        {
            return false;
        }
    }
}
