package jora.coobach.block.entity;

import javax.annotation.Nullable;

import jora.coobach.COOBaCH;
import jora.coobach.energy.EnergyContainer;
import jora.coobach.screen.ThermalGeneratorScreenHandler;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThermalGeneratorBlockEntity
extends BlockEntity
implements Inventory,
NamedScreenHandlerFactory,
Nameable {
    private ItemStack _fuelSlot;
    private int _burnTime;
    private int _burnTimeTotal;
    private boolean _activeConsumer;
    private EnergyContainer _energyContainer;

    public ThermalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(COOBaCH.THERMAL_GENERATOR_BLOCK_ENTITY, pos, state);
        _energyContainer = new EnergyContainer(1000, true);
        _fuelSlot = ItemStack.EMPTY;
        _burnTime = 0;
        _burnTimeTotal = 0;
    }

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0: return _burnTime;
                case 1: return _burnTimeTotal;
                case 2: return _energyContainer.getEnergy();
                case 3: return _energyContainer.getEnergyMax();
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0: _burnTime = value; break;
                case 1: _burnTimeTotal = value; break;
                case 2: _energyContainer.setEnergy(value); break;
                case 3: _energyContainer.setEnergyMax(value); break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public Text getName() {
        return Text.translatable("container.coobach.thermal_generator");
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ThermalGeneratorScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("burn_time", _burnTime);
        nbt.putInt("burn_time_total", _burnTimeTotal);
        nbt.putBoolean("active_consumer", _activeConsumer);
        nbt.put("energy", _energyContainer.writeNbt(new NbtCompound()));
        nbt.put("fuel_slot", _fuelSlot.writeNbt(new NbtCompound()));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt); 
        _burnTime = nbt.getInt("burn_time");
        _burnTimeTotal = nbt.getInt("burn_time_total");
        _activeConsumer = nbt.getBoolean("active_consumer");
        _energyContainer = EnergyContainer.fromNbt(nbt.getCompound("energy"));
        _fuelSlot = ItemStack.fromNbt(nbt.getCompound("fuel_slot"));
    }

    @Override
    public boolean isEmpty() {
        return _fuelSlot.isEmpty();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot == 0 ? _fuelSlot : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return slot == 0 ? _fuelSlot.split(amount) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot != 0)
            return ItemStack.EMPTY;
        var result = _fuelSlot;
        _fuelSlot = ItemStack.EMPTY;
        return result;
    }

    private void tryStartBurning() {
        if (!isEmpty() && !_energyContainer.isFull()) {
            Integer fuelTime = getFuelTime(_fuelSlot.getItem());
            if (fuelTime != null && fuelTime > 0) {
                _burnTime = fuelTime;
                _burnTimeTotal = fuelTime;

                if (_fuelSlot.getItem() instanceof BucketItem)
                    _fuelSlot = new ItemStack(Items.BUCKET, 1);
                else
                    _fuelSlot.decrement(1);

                World world = getWorld();
                BlockState state = world.getBlockState(pos);
                world.setBlockState(pos, state.with(Properties.LIT, true));
                markDirty(world, pos, state);
            }
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {        
        if (slot != 0)
            throw new IndexOutOfBoundsException("only stack 0 exists");
        _fuelSlot = stack;
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }

        if (!isBurning())
            tryStartBurning();
    }

    public boolean isBurning() {
        return _burnTime > 0;
    }

    public static Integer getFuelTime(Item item) {
        return FuelRegistry.INSTANCE.get(item);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ThermalGeneratorBlockEntity blockEntity) {
        boolean burnedBefore = blockEntity.isBurning();
        if (!burnedBefore)
            return;

        --blockEntity._burnTime;
        blockEntity._energyContainer.addEnergy(1);

        if (!blockEntity.isBurning()) {;
            blockEntity.tryStartBurning();
            if (burnedBefore && !blockEntity.isBurning())
                world.setBlockState(pos, state.with(Properties.LIT, false));
            markDirty(world, pos, state);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this)
            return false;
        return player.squaredDistanceTo((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clear() {
        _fuelSlot = ItemStack.EMPTY;
    }

    @Override
    public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return createScreenHandler(i, playerInventory);
    }

    @Override
    public Text getDisplayName() {
        return getName();
    }
}
