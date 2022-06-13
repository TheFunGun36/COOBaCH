package jora.coobach;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.item.Item;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class COOBaCH implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("coobach");

	public static final ItemGroup COOBACH_GROUP = FabricItemGroupBuilder.build(
		new Identifier("coobach", "coobach"),
		() -> new ItemStack(COOBaCH.THERMAL_GENERATOR_BLOCK));

	
	public static final Identifier THERMAL_GENERATOR_ID = new Identifier("coobach", "thermal_generator");
	public static final ThermalGeneratorBlock THERMAL_GENERATOR_BLOCK
		= new ThermalGeneratorBlock(FabricBlockSettings.of(Material.METAL)
			.strength(5, 8)
			.sounds(BlockSoundGroup.METAL)
			.requiresTool());
	public static final BlockEntityType<ThermalGeneratorBlockEntity> THERMAL_GENERATOR_BLOCK_ENTITY
		= FabricBlockEntityTypeBuilder.create(ThermalGeneratorBlockEntity::new, THERMAL_GENERATOR_BLOCK).build(null);
	public static final BlockItem THERMAL_GENERATOR_ITEM = new BlockItem(
		THERMAL_GENERATOR_BLOCK,
		new FabricItemSettings().group(COOBaCH.COOBACH_GROUP));
	public static final ScreenHandlerType<ThermalGeneratorScreenHandler> THERMAL_GENERATOR_SCREEN_HANDLER
		= new ScreenHandlerType<ThermalGeneratorScreenHandler>(ThermalGeneratorScreenHandler::new);

	public static final Item STEEL_INGOT = new Item(new FabricItemSettings().group(COOBaCH.COOBACH_GROUP));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Registry.register(Registry.ITEM,				"coobach:steel_ingot",				STEEL_INGOT);
		Registry.register(Registry.BLOCK,				THERMAL_GENERATOR_ID,				THERMAL_GENERATOR_BLOCK);
		Registry.register(Registry.BLOCK_ENTITY_TYPE,	THERMAL_GENERATOR_ID,   			THERMAL_GENERATOR_BLOCK_ENTITY);
		Registry.register(Registry.ITEM,				THERMAL_GENERATOR_ID,				THERMAL_GENERATOR_ITEM);
		LOGGER.info("Hello there!");
	}
}
