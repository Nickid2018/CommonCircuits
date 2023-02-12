package io.github.nickid2018.commoncircuits.block;

//#if MC>=11903
import net.minecraft.core.registries.BuiltInRegistries;
//#endif

import io.github.nickid2018.commoncircuits.logic.LogicProvider;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class CommonCircuitsBlocks {

    public static final StrongRedStoneWireBlock HIGH_POWER_REDSTONE_WIRE =
            StrongRedStoneWireBlock.strongRedStoneWire(30, 0.15f, 0.15f);
    public static final StrongRedStoneWireBlock SUPER_POWER_REDSTONE_WIRE =
            StrongRedStoneWireBlock.strongRedStoneWire(60, 0.2f, 0.2f);
    public static final BaseCircuitPlateBlock AND_GATE_PLATE =
            BaseCircuitPlateBlock.baseCircuitPlateBlock(LogicProvider.AND);
    public static final BaseCircuitPlateBlock OR_GATE_PLATE =
            BaseCircuitPlateBlock.baseCircuitPlateBlock(LogicProvider.OR);
    public static final BaseCircuitPlateBlock XOR_GATE_PLATE =
            BaseCircuitPlateBlock.baseCircuitPlateBlock(LogicProvider.XOR);
    public static final BaseCircuitPlateBlock NOT_GATE_PLATE =
            BaseCircuitPlateBlock.baseCircuitPlateBlock(LogicProvider.NOT);
    public static final BaseCircuitPlateBlock NAND_GATE_PLATE =
            BaseCircuitPlateBlock.baseCircuitPlateBlock(LogicProvider.NAND);
    public static final BaseCircuitPlateBlock NOR_GATE_PLATE =
            BaseCircuitPlateBlock.baseCircuitPlateBlock(LogicProvider.NOR);
    public static final BaseCircuitPlateBlock XNOR_GATE_PLATE =
            BaseCircuitPlateBlock.baseCircuitPlateBlock(LogicProvider.XNOR);
    public static final RedstoneClockBlock REDSTONE_CLOCK =
            new RedstoneClockBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.FIRE).requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F).sound(SoundType.METAL).isRedstoneConductor((blockState, blockGetter, blockPos) -> false));
    public static final RedstonePulseGeneratorBlock POSITIVE_PULSE_GENERATOR =
            new RedstonePulseGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.FIRE).requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F).sound(SoundType.METAL).isRedstoneConductor((blockState, blockGetter, blockPos) -> false),
                    true);
    public static final RedstonePulseGeneratorBlock NEGATIVE_PULSE_GENERATOR =
            new RedstonePulseGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.FIRE).requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F).sound(SoundType.METAL).isRedstoneConductor((blockState, blockGetter, blockPos) -> false),
                    false);

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
        register("and_gate_plate", AND_GATE_PLATE);
        register("or_gate_plate", OR_GATE_PLATE);
        register("xor_gate_plate", XOR_GATE_PLATE);
        register("not_gate_plate", NOT_GATE_PLATE);
        register("nand_gate_plate", NAND_GATE_PLATE);
        register("nor_gate_plate", NOR_GATE_PLATE);
        register("xnor_gate_plate", XNOR_GATE_PLATE);
        register("redstone_clock", REDSTONE_CLOCK);
        register("positive_pulse_generator", POSITIVE_PULSE_GENERATOR);
        register("negative_pulse_generator", NEGATIVE_PULSE_GENERATOR);
    }
}
