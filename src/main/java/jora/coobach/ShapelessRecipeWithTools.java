package jora.coobach;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.collection.DefaultedList;

public class ShapelessRecipeWithTools extends ShapelessRecipe {

    public ShapelessRecipeWithTools(ShapelessRecipe original) {
        super(
                original.getId(),
                original.getGroup(),
                original.getOutput(),
                original.getIngredients()
        );
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ShapelessRecipeWithToolsSerializer.INSTANCE;
    }


    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory) 
    {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < defaultedList.size(); ++i) {
            ItemStack stack = inventory.getStack(i);
            Item item = stack.getItem();
            
/////////////////////////// THIS IS THE NEW STUFF ///////////////////////////
            if (item instanceof ToolItem) {
                int newDamage = stack.getDamage() + 1;
                if (newDamage < stack.getMaxDamage()) {
                    stack = stack.copy();
                    stack.setDamage(newDamage);
                    defaultedList.set(i, stack);
                }
/////////////////////////// END OF THE NEW STUFF ///////////////////////////
            } else if (item.hasRecipeRemainder()) {
                defaultedList.set(i, new ItemStack(item.getRecipeRemainder()));
            }
        }

        return defaultedList;
    }

}