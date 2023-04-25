package io.github.nickid2018.commoncircuits.levelgen;

//#if MC>=11904
import net.minecraft.core.registries.Registries;
//#else
//$$ import net.minecraft.core.Registry;
//#endif

//#if MC>=11802
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
//#endif

public class CommonCircuitsFeaturesV2 {
    //#if MC>=11802

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER_CONFIGURED = configuredFeature("ore_silver");
    public static final ResourceKey<PlacedFeature> ORE_SILVER_PLACED = placedFeature("ore_silver");

    public static ResourceKey<ConfiguredFeature<?, ?>> configuredFeature(String name) {
        //#if MC>=11904
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("commoncircuits", name));
        //#else
        //$$ return ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation("commoncircuits", name));
        //#endif
    }

    public static ResourceKey<PlacedFeature> placedFeature(String name) {
        //#if MC>=11904
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("commoncircuits", name));
        //#else
        //$$ return ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation("commoncircuits", name));
        //#endif
    }

    public static void registerFeatures() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SILVER_PLACED);
    }

    //#endif
}
