package jora.coobach.item;

import jora.coobach.COOBaCH;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;

public class BronzeToolMaterial implements ToolMaterial{

    public static final BronzeToolMaterial INSTANCE = new BronzeToolMaterial();

    @Override
    public int getDurability() {
        return 350;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return ToolMaterials.IRON.getMiningSpeedMultiplier();
    }

    @Override
    public float getAttackDamage() {
        return ToolMaterials.IRON.getAttackDamage();
    }

    @Override
    public int getMiningLevel() {
        return ToolMaterials.IRON.getMiningLevel();
    }

    @Override
    public int getEnchantability() {
        return ToolMaterials.IRON.getEnchantability();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(COOBaCH.BRONZE_INGOT);
    }
}
