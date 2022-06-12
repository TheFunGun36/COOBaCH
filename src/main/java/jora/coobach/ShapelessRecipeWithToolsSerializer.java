package jora.coobach;

import com.google.gson.JsonObject;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;

public class ShapelessRecipeWithToolsSerializer extends ShapelessRecipe.Serializer {

    public static final ShapelessRecipeWithToolsSerializer INSTANCE = new ShapelessRecipeWithToolsSerializer();

    @Override
    public ShapelessRecipe read(Identifier identifier, JsonObject jsonObject) {
        return new ShapelessRecipeWithTools(super.read(identifier, jsonObject));
    }

    @Override
    public ShapelessRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
        return new ShapelessRecipeWithTools(super.read(identifier, packetByteBuf));
    }

}