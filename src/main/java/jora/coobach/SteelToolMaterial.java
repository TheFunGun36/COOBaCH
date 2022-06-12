package jora.coobach;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;

public class SteelToolMaterial implements ToolMaterial{

    public static final SteelToolMaterial INSTANCE = new SteelToolMaterial();

    @Override
    public int getDurability() {
        return ToolMaterials.IRON.getDurability() + 50;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 1.15f * ToolMaterials.IRON.getMiningSpeedMultiplier();
    }

    @Override
    public float getAttackDamage() {
        return 1.15f * ToolMaterials.IRON.getAttackDamage();
    }

    @Override
    public int getMiningLevel() {
        return ToolMaterials.IRON.getMiningLevel();
    }

    @Override
    public int getEnchantability() {
        return ToolMaterials.IRON.getEnchantability() - 2;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(COOBaCH.STEEL_INGOT);
    }
}
