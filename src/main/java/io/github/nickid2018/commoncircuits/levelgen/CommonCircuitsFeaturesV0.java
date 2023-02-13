package io.github.nickid2018.commoncircuits.levelgen;

//#if MC<11701
//$$ import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
//$$ import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
//$$ import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
//$$ import net.minecraft.core.Registry;
//$$ import net.minecraft.data.BuiltinRegistries;
//$$ import net.minecraft.data.worldgen.Features;
//$$ import net.minecraft.resources.ResourceKey;
//$$ import net.minecraft.resources.ResourceLocation;
//$$ import net.minecraft.world.level.levelgen.GenerationStep;
//$$ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//$$ import net.minecraft.world.level.levelgen.feature.Feature;
//$$ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
//#endif

public class CommonCircuitsFeaturesV0 {
    //#if MC<11701
    //$$
    //$$ public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER_CONFIGURED = configuredFeature("ore_silver",
    //$$          Feature.ORE.configured(new OreConfiguration(
    //$$                  OreConfiguration.Predicates.NATURAL_STONE, CommonCircuitsBlocks.SILVER_ORE.defaultBlockState(), 9
    //$$          )).range(32).squared().count(5));
    //$$
    //$$ public static ResourceKey<ConfiguredFeature<?, ?>> configuredFeature(String name, ConfiguredFeature<?, ?> feature) {
    //$$    ResourceKey<ConfiguredFeature<?, ?>> key = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation("commoncircuits", name));
    //$$    Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key.location(), feature);
    //$$    return key;
    //$$ }
    //$$
    //$$ public static void registerFeatures() {
    //$$    BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SILVER_CONFIGURED);
    //$$ }
    //$$
    //#endif
}
