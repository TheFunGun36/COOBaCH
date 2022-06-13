package jora.coobach;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ThermalGeneratorScreenHandler extends ScreenHandler {
    private final Inventory _inventory;
    private final PropertyDelegate _propertyDelegate;
    public ThermalGeneratorScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1), new ArrayPropertyDelegate(4));
    }

    public ThermalGeneratorScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(COOBaCH.THERMAL_GENERATOR_SCREEN_HANDLER, syncId);
        checkSize(inventory, 1);
        _inventory = inventory;
        _propertyDelegate = propertyDelegate;
        inventory.onOpen(playerInventory.player);
        addProperties(propertyDelegate);

        // fuel slot
        addSlot(new Slot(inventory, 0, 74, 40));

        // player inventory
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public int getBurnTime() {
        return _propertyDelegate.get(0);
    }
    public int getBurnTimeTotal() {
        return _propertyDelegate.get(1);
    }
    public int getEnergy() {
        return _propertyDelegate.get(2);
    }
    public int getMaxEnergy() {
        return _propertyDelegate.get(3);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack destStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack sourceStack = slot.getStack();
            destStack = sourceStack.copy();

            if (index < _inventory.size()) {
                if (!insertItem(sourceStack, _inventory.size(), slots.size(), true))
                    return ItemStack.EMPTY;
            }
            else {
                Integer fuelTime = FuelRegistry.INSTANCE.get(sourceStack.getItem());
                boolean flammable = fuelTime != null && fuelTime > 0;
                if (!flammable || !insertItem(sourceStack, 0, _inventory.size(), false))
                    return ItemStack.EMPTY;
            }
    
            if (sourceStack.isEmpty())
                slot.setStack(ItemStack.EMPTY);
            else
                slot.markDirty();
            slot.onTakeItem(player, sourceStack);
        }
        return destStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return _inventory.canPlayerUse(player);
    }
}
