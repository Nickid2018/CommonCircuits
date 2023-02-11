package io.github.nickid2018.commoncircuits.block;

//#if MC>=11903
import net.minecraft.core.registries.BuiltInRegistries;
//#endif

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class CommonCircuitsBlocks {

    public static final StrongRedStoneWireBlock HIGH_POWER_REDSTONE_WIRE = StrongRedStoneWireBlock.strongRedStoneWire(30, 0.15f, 0.15f);
    public static final StrongRedStoneWireBlock SUPER_POWER_REDSTONE_WIRE = StrongRedStoneWireBlock.strongRedStoneWire(60, 0.2f, 0.2f);

    private static void register(String name, Block block) {
        //#if MC>=11903
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation("commoncircuits", name), block);
        //#else
        //$$ Registry.register(Registry.BLOCK, new ResourceLocation("commoncircuits", name), block);
        //#endif
    }

    public static void registerBlocks() {
        register("high_power_redstone_wire", HIGH_POWER_REDSTONE_WIRE);
        register("super_power_redstone_wire", SUPER_POWER_REDSTONE_WIRE);
    }
}
