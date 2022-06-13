package jora.coobach.energy;

import net.minecraft.nbt.NbtCompound;

public class EnergyContainer {
    private int _energy;
    private int _energyMax;
    private boolean _activeConsumer = false;

    public EnergyContainer(int energyMax, boolean activeConsumer) {
        _energyMax = energyMax;
        _energy = 0;
        _activeConsumer = activeConsumer;
    }

    public int getEnergy() {
        return _energy;
    }

    public void setEnergy(int value) {
        _energy = value;
    }

    public int getEnergyMax() {
        return _energyMax;
    }

    public void setEnergyMax(int value) {
        _energyMax = value;
    }

    public boolean isActiveConsumer() {
        return _activeConsumer;
    }

    public void setActiveConsumer(boolean value) {
        _activeConsumer = value;
    }

    public int addEnergy(int value) {
        int result = 0;
        _energy += value;
        if (_energy > _energyMax) {
            result = _energy - _energyMax;
            _energy = _energyMax;
        }
        return result;
    }

    public int takeEnergy(int value) {
        int result = value;
        if (value <= _energy) {
            _energy -= value;
        }
        else {
            result = _energy;
            _energy = 0;
        }
        return result;
    }

    public boolean isFull() {
        return _energy >= _energyMax;
    }

    public boolean isEmpty() {
        return _energy <= 0;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("energy", _energy);
        nbt.putInt("energy_max", _energyMax);
        nbt.putBoolean("active_consumer", _activeConsumer);
        return nbt;
    }

    static public EnergyContainer fromNbt(NbtCompound nbt) {        
        EnergyContainer result = new EnergyContainer(nbt.getInt("energy_max"), nbt.getBoolean("active_consumer"));
        result._energy = nbt.getInt("energy");
        return result;
    }
}
