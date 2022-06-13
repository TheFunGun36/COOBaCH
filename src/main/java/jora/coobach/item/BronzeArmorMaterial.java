package jora.coobach.item;

import jora.coobach.COOBaCH;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class BronzeArmorMaterial implements ArmorMaterial {
	private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
	private static final int[] PROTECTION_VALUES = new int[] {2, 5, 6, 2};
 
	@Override
	public int getDurability(EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getEntitySlotId()] * 22;
	}
 
	@Override
	public int getProtectionAmount(EquipmentSlot slot) {
		return PROTECTION_VALUES[slot.getEntitySlotId()];
	}
 
	@Override
	public int getEnchantability() {
		return ArmorMaterials.IRON.getEnchantability();
	}
 
	@Override
	public SoundEvent getEquipSound() {
		return SoundEvents.ITEM_ARMOR_EQUIP_GOLD;
	}
 
	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(COOBaCH.BRONZE_INGOT);
	}
 
	@Override
	public String getName() {
		// Must be all lowercase
		return "name";
	}
 
	@Override
	public float getToughness() {
		return 0.0F;
	}
 
	@Override
	public float getKnockbackResistance() {
		return 0.0F;
	}
}