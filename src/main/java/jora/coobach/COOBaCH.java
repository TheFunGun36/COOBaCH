package jora.coobach;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.item.Item;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.BlockSoundGroup;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class COOBaCH implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.


	//JORA
	public static final ItemGroup COOBACH_GROUP = FabricItemGroupBuilder.build(
		new Identifier("coobach", "coobach"),
		() -> new ItemStack(COOBaCH.THERMAL_GENERATOR));

	public static final Logger LOGGER = LoggerFactory.getLogger("coobach");
	public static final ThermalGenerator THERMAL_GENERATOR = new ThermalGenerator(
		FabricBlockSettings.of(Material.METAL)
			.strength(5, 8)
			.sounds(BlockSoundGroup.STONE)
			.luminance(ThermalGenerator::getLightLevel));
	public static final BlockItem THERMAL_GENERATOR_ITEM = new BlockItem(
		THERMAL_GENERATOR,
		new FabricItemSettings().group(COOBaCH.COOBACH_GROUP));
	public static final Item STEEL_INGOT = new Item(new FabricItemSettings().group(COOBaCH.COOBACH_GROUP));

	//Tools
	public static final ToolItem IRON_HAMMER = new ToolItem(ToolMaterials.IRON,  
		new FabricItemSettings().group(COOBaCH.COOBACH_GROUP).maxDamage(40));

	public static final ToolItem STEEL_HAMMER = new ToolItem(SteelToolMaterial.INSTANCE,
		new FabricItemSettings().group(COOBaCH.COOBACH_GROUP).maxDamage(64));

	//Materials
	public static final Item IRON_PLATE = new Item(new FabricItemSettings().group(COOBaCH.COOBACH_GROUP));
	public static final Item STEEL_PLATE = new Item(new FabricItemSettings().group(COOBaCH.COOBACH_GROUP));
	public static final Item COPPER_PLATE = new Item(new FabricItemSettings().group(COOBaCH.COOBACH_GROUP));


	//World Gen
	private static ConfiguredFeature<?, ?> OVERWORLD_WOOL_ORE_CONFIGURED_FEATURE = new ConfiguredFeature(
		Feature.ORE, 
		new OreFeatureConfig(
			OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
			THERMAL_GENERATOR.getDefaultState(),
			9)); // vein size
 
  public static PlacedFeature OVERWORLD_WOOL_ORE_PLACED_FEATURE = new PlacedFeature(
	RegistryEntry.of(OVERWORLD_WOOL_ORE_CONFIGURED_FEATURE),
	Arrays.asList(
		CountPlacementModifier.of(20), // number of veins per chunk
		SquarePlacementModifier.of(), // spreading horizontally
		HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64))
	)); // height
	

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//Recipes
		Registry.register(Registry.RECIPE_SERIALIZER, 
		"coobach:crafting_shapeless_with_tools", 
			ShapelessRecipeWithToolsSerializer.INSTANCE);

		//JORA
		Registry.register(Registry.ITEM, new Identifier("coobach", "steel_ingot"), STEEL_INGOT);
		Registry.register(Registry.BLOCK, new Identifier("coobach", "thermal_generator"), THERMAL_GENERATOR);
		Registry.register(Registry.ITEM, new Identifier("coobach", "thermal_generator"), THERMAL_GENERATOR_ITEM);

		//Tools
		Registry.register(Registry.ITEM, "coobach:iron_hammer", IRON_HAMMER);
		Registry.register(Registry.ITEM, "coobach:steel_hammer", STEEL_HAMMER);

		//Materials
		Registry.register(Registry.ITEM, "coobach:iron_plate", IRON_PLATE);
		Registry.register(Registry.ITEM, "coobach:steel_plate", STEEL_PLATE);
		Registry.register(Registry.ITEM, "coobach:copper_plate", COPPER_PLATE);
		
		LOGGER.info("Hello Fabric world!");


		// World Gen

		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
     	"coobach:overworld_wool_ore", OVERWORLD_WOOL_ORE_CONFIGURED_FEATURE);

    	Registry.register(BuiltinRegistries.PLACED_FEATURE, "coobach:overworld_wool_ore",
        	OVERWORLD_WOOL_ORE_PLACED_FEATURE);
		
    	BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), 
			net.minecraft.world.gen.GenerationStep.Feature.UNDERGROUND_ORES,
        	RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("coobach:overworld_wool_ore")));
	}
	
}
