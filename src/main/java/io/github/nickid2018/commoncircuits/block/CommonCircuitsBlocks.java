package io.github.nickid2018.commoncircuits.block;

import io.github.nickid2018.commoncircuits.block.entity.AdvancedCircuitBlockEntity;
import io.github.nickid2018.commoncircuits.block.entity.AdvancedRedstoneWireBlockEntity;
import io.github.nickid2018.commoncircuits.block.entity.SemiconductorBenchBlockEntity;
import io.github.nickid2018.commoncircuits.logic.LogicProvider;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

//#if MC>=11903
import net.minecraft.core.registries.BuiltInRegistries;
//#endif

//#if MC>=11701
import net.minecraft.util.valueproviders.UniformInt;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
//#else
//$$ import java.util.function.Supplier;
//#endif

public class CommonCircuitsBlocks {

    public static final StrongRedStoneWireBlock HIGH_POWER_REDSTONE_WIRE =
            StrongRedStoneWireBlock.strongRedStoneWire(30, 0.15f, 0.15f);
    public static final StrongRedStoneWireBlock SUPER_POWER_REDSTONE_WIRE =
            StrongRedStoneWireBlock.strongRedStoneWire(60, 0.2f, 0.2f);
    public static final AdvancedRedstoneWireBlock ADVANCED_REDSTONE_WIRE_BLOCK_1
            = new AdvancedRedstoneWireBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.FIRE)
            .strength(0.5f, 0.5f).sound(SoundType.WOOL).isRedstoneConductor(((blockState, blockGetter, blockPos) -> false)), 1, 1);
    public static final AdvancedRedstoneWireBlock ADVANCED_REDSTONE_WIRE_BLOCK_2
            = new AdvancedRedstoneWireBlock(BlockBehaviour.Properties.copy(ADVANCED_REDSTONE_WIRE_BLOCK_1), 2, 2);
    public static final AdvancedRedstoneWireBlock ADVANCED_REDSTONE_WIRE_BLOCK_4
            = new AdvancedRedstoneWireBlock(BlockBehaviour.Properties.copy(ADVANCED_REDSTONE_WIRE_BLOCK_1), 3, 4);
    public static final AdvancedRedstoneWireBlock ADVANCED_REDSTONE_WIRE_BLOCK_8
            = new AdvancedRedstoneWireBlock(BlockBehaviour.Properties.copy(ADVANCED_REDSTONE_WIRE_BLOCK_1), 4, 8);
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
    public static final Block SILVER_BLOCK = new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
            .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL));
    public static final SemiconductorBenchBlock SEMICONDUCTOR_BENCH =
            new SemiconductorBenchBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL));

    // -------------------------------------------------------------------------------------------------
    //#if MC>=11903
    public static final Block SILVER_ORE = new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
            .requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(2, 5));
    //#else
    //$$ public static final Block SILVER_ORE = new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F));
    //#endif

    public static final BlockEntityType<AdvancedRedstoneWireBlockEntity> ADVANCED_REDSTONE_WIRE_BLOCK_ENTITY =
            createBlockEntity(AdvancedRedstoneWireBlockEntity::new,
                    ADVANCED_REDSTONE_WIRE_BLOCK_1, ADVANCED_REDSTONE_WIRE_BLOCK_2,
                    ADVANCED_REDSTONE_WIRE_BLOCK_4, ADVANCED_REDSTONE_WIRE_BLOCK_8);

    public static final BlockEntityType<AdvancedCircuitBlockEntity> ADVANCED_CIRCUIT_BLOCK_ENTITY =
            createBlockEntity(AdvancedCircuitBlockEntity::new);

    public static final BlockEntityType<SemiconductorBenchBlockEntity> SEMICONDUCTOR_BENCH_BLOCK_ENTITY =
            createBlockEntity(SemiconductorBenchBlockEntity::new, SEMICONDUCTOR_BENCH);


    //#if MC>=11701
    private static <T extends BlockEntity> BlockEntityType<T> createBlockEntity(FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory, blocks).build(null);
    }
    //#else
    //$$ private static <T extends BlockEntity> BlockEntityType<T> createBlockEntity(Supplier<T> factory, Block... blocks) {
    //$$     return BlockEntityType.Builder.of(factory, blocks).build(null);
    //$$ }
    //#endif


    private static void registerBlock(String name, Block block) {
        //#if MC>=11903
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation("commoncircuits", name), block);
        //#else
        //$$ Registry.register(Registry.BLOCK, new ResourceLocation("commoncircuits", name), block);
        //#endif
    }

    private static void registerBlockEntity(String name, BlockEntityType<?> blockEntityType) {
        //#if MC>=11903
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation("commoncircuits", name), blockEntityType);
        //#else
        //$$ Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation("commoncircuits", name), blockEntityType);
        //#endif
    }

    public static void registerBlocks() {
        registerBlock("high_power_redstone_wire", HIGH_POWER_REDSTONE_WIRE);
        registerBlock("super_power_redstone_wire", SUPER_POWER_REDSTONE_WIRE);
        registerBlock("advanced_redstone_wire_block_1", ADVANCED_REDSTONE_WIRE_BLOCK_1);
        registerBlock("advanced_redstone_wire_block_2", ADVANCED_REDSTONE_WIRE_BLOCK_2);
        registerBlock("advanced_redstone_wire_block_4", ADVANCED_REDSTONE_WIRE_BLOCK_4);
        registerBlock("advanced_redstone_wire_block_8", ADVANCED_REDSTONE_WIRE_BLOCK_8);
        registerBlock("and_gate_plate", AND_GATE_PLATE);
        registerBlock("or_gate_plate", OR_GATE_PLATE);
        registerBlock("xor_gate_plate", XOR_GATE_PLATE);
        registerBlock("not_gate_plate", NOT_GATE_PLATE);
        registerBlock("nand_gate_plate", NAND_GATE_PLATE);
        registerBlock("nor_gate_plate", NOR_GATE_PLATE);
        registerBlock("xnor_gate_plate", XNOR_GATE_PLATE);
        registerBlock("redstone_clock", REDSTONE_CLOCK);
        registerBlock("positive_pulse_generator", POSITIVE_PULSE_GENERATOR);
        registerBlock("negative_pulse_generator", NEGATIVE_PULSE_GENERATOR);
        registerBlock("silver_ore", SILVER_ORE);
        registerBlock("silver_block", SILVER_BLOCK);
        registerBlock("semiconductor_bench", SEMICONDUCTOR_BENCH);

        registerBlockEntity("advanced_redstone_wire", ADVANCED_REDSTONE_WIRE_BLOCK_ENTITY);
        registerBlockEntity("advanced_circuit", ADVANCED_CIRCUIT_BLOCK_ENTITY);
        registerBlockEntity("semiconductor_bench", SEMICONDUCTOR_BENCH_BLOCK_ENTITY);
    }
}
