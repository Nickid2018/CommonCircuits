package io.github.nickid2018.commoncircuits.block;

import net.minecraft.core.Registry;
//#if MC>=11903
import net.minecraft.core.registries.BuiltInRegistries;
//#endif
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;

import static net.minecraft.world.level.block.RedStoneWireBlock.*;

public class CommonCircuitsBlocks {

    public static final Block HIGH_POWER_REDSTONE_WIRE = strongRedStoneWire(60);
    public static final Block SUPER_POWER_REDSTONE_WIRE = strongRedStoneWire(120);

    private static StrongRedStoneWireBlock strongRedStoneWire(int power) {
        IntegerProperty property = IntegerProperty.create("power", 0, power);
        return new StrongRedStoneWireBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak(), power, property) {
            protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
                builder.add(NORTH, EAST, SOUTH, WEST, property);
            }
        };
    }

    private static Block register(String name, Block block) {
        //#if MC>=11903
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation("commoncircuits", name), block);
        //#else
        //$$ return Registry.register(Registry.BLOCK, new ResourceLocation("commoncircuits", name), block);
        //#endif
    }

    public static void registerBlocks() {
        register("high_power_redstone_wire", HIGH_POWER_REDSTONE_WIRE);
        register("super_power_redstone_wire", SUPER_POWER_REDSTONE_WIRE);
    }
}
