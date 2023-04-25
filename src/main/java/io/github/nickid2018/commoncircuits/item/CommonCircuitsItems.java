package io.github.nickid2018.commoncircuits.item;

//#if MC>=11904
import net.minecraft.core.registries.BuiltInRegistries;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
//#else
//$$ import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
//#endif

import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class CommonCircuitsItems {

    //#if MC>=11904
    public static final CreativeModeTab COMMON_CIRCUITS = FabricItemGroup.builder(
            new ResourceLocation("commoncircuits", "commoncircuits")
    ).icon(() -> new ItemStack(CommonCircuitsBlocks.HIGH_POWER_REDSTONE_WIRE)).build();
    //#else
    //$$ public static final CreativeModeTab COMMON_CIRCUITS = FabricItemGroupBuilder.create(
    //$$        new ResourceLocation("commoncircuits", "commoncircuits")
    //$$ ).icon(() -> new ItemStack(CommonCircuitsBlocks.HIGH_POWER_REDSTONE_WIRE)).build();
    //#endif

    public static final List<Item> COMMON_CIRCUITS_ITEMS = new ArrayList<>();

    // Blocks --------------------------------------------------------------------------------------
    public static final Item HIGH_POWER_REDSTONE = itemNameBlockItem(CommonCircuitsBlocks.HIGH_POWER_REDSTONE_WIRE, new Item.Properties());
    public static final Item SUPER_POWER_REDSTONE = itemNameBlockItem(CommonCircuitsBlocks.SUPER_POWER_REDSTONE_WIRE, new Item.Properties());
    public static final Item ADVANCED_REDSTONE_WIRE_1 = itemNameBlockItem(CommonCircuitsBlocks.ADVANCED_REDSTONE_WIRE_BLOCK_1, new Item.Properties());
    public static final Item ADVANCED_REDSTONE_WIRE_2 = itemNameBlockItem(CommonCircuitsBlocks.ADVANCED_REDSTONE_WIRE_BLOCK_2, new Item.Properties());
    public static final Item ADVANCED_REDSTONE_WIRE_4 = itemNameBlockItem(CommonCircuitsBlocks.ADVANCED_REDSTONE_WIRE_BLOCK_4, new Item.Properties());
    public static final Item ADVANCED_REDSTONE_WIRE_8 = itemNameBlockItem(CommonCircuitsBlocks.ADVANCED_REDSTONE_WIRE_BLOCK_8, new Item.Properties());
    public static final Item AND_GATE_PLATE = itemNameBlockItem(CommonCircuitsBlocks.AND_GATE_PLATE, new Item.Properties());
    public static final Item OR_GATE_PLATE = itemNameBlockItem(CommonCircuitsBlocks.OR_GATE_PLATE, new Item.Properties());
    public static final Item XOR_GATE_PLATE = itemNameBlockItem(CommonCircuitsBlocks.XOR_GATE_PLATE, new Item.Properties());
    public static final Item NOT_GATE_PLATE = itemNameBlockItem(CommonCircuitsBlocks.NOT_GATE_PLATE, new Item.Properties());
    public static final Item NAND_GATE_PLATE = itemNameBlockItem(CommonCircuitsBlocks.NAND_GATE_PLATE, new Item.Properties());
    public static final Item NOR_GATE_PLATE = itemNameBlockItem(CommonCircuitsBlocks.NOR_GATE_PLATE, new Item.Properties());
    public static final Item XNOR_GATE_PLATE = itemNameBlockItem(CommonCircuitsBlocks.XNOR_GATE_PLATE, new Item.Properties());
    public static final Item REDSTONE_CLOCK = itemNameBlockItem(CommonCircuitsBlocks.REDSTONE_CLOCK, new Item.Properties());
    public static final Item POSITIVE_PULSE_GENERATOR = itemNameBlockItem(CommonCircuitsBlocks.POSITIVE_PULSE_GENERATOR, new Item.Properties());
    public static final Item NEGATIVE_PULSE_GENERATOR = itemNameBlockItem(CommonCircuitsBlocks.NEGATIVE_PULSE_GENERATOR, new Item.Properties());
    public static final Item SILVER_ORE = itemNameBlockItem(CommonCircuitsBlocks.SILVER_ORE, new Item.Properties());
    public static final Item SILVER_BLOCK = itemNameBlockItem(CommonCircuitsBlocks.SILVER_BLOCK, new Item.Properties());
    public static final Item SEMICONDUCTOR_BENCH = itemNameBlockItem(CommonCircuitsBlocks.SEMICONDUCTOR_BENCH, new Item.Properties());
    public static final Item WIRE_CONNECTOR = itemNameBlockItem(CommonCircuitsBlocks.WIRE_CONNECTOR, new Item.Properties());

    // Items -----------------------------------------------------------------------------------------------
    public static final Item COPPER_DUST = item(new Item.Properties());
    public static final Item SILVER_DUST = item(new Item.Properties());
    public static final Item RAW_SILVER = item(new Item.Properties());
    public static final Item SILVER_INGOT = item(new Item.Properties());
    public static final Item P_SEMICONDUCTOR = item(new Item.Properties());
    public static final Item N_SEMICONDUCTOR = item(new Item.Properties());
    public static final Item BLACK_WAX = item(new Item.Properties());
    public static final Item DIODE = item(new Item.Properties());
    public static final Item PMOS = item(new Item.Properties());
    public static final Item NMOS = item(new Item.Properties());
    public static final Item NPN_BJT = item(new Item.Properties());
    public static final Item PNP_BJT = item(new Item.Properties());
    public static final Item N_JFET = item(new Item.Properties());
    public static final Item P_JFET = item(new Item.Properties());
    public static final Item AND_GATE = item(new Item.Properties());
    public static final Item OR_GATE = item(new Item.Properties());
    public static final Item XOR_GATE = item(new Item.Properties());
    public static final Item NOT_GATE = item(new Item.Properties());
    public static final Item NAND_GATE = item(new Item.Properties());
    public static final Item NOR_GATE = item(new Item.Properties());
    public static final Item XNOR_GATE = item(new Item.Properties());
    public static final Item BASE_PLATE = item(new Item.Properties());

    public static final Item SILVER_WRENCH = item(new Item.Properties().stacksTo(1));

    // Item Groups ----------------------------------------------------------------------------------------
    private static Item itemNameBlockItem(Block block, Item.Properties properties) {
        //#if MC>=11904
        return new ItemNameBlockItem(block, properties);
        //#else
        //$$ return new ItemNameBlockItem(block, properties.tab(COMMON_CIRCUITS));
        //#endif
    }

    private static Item item(Item.Properties properties) {
        //#if MC>=11904
        return new Item(properties);
        //#else
        //$$ return new Item(properties.tab(COMMON_CIRCUITS));
        //#endif
    }

    private static Item.Properties tabProperties() {
        //#if MC>=11904
        return new Item.Properties();
        //#else
        //$$ return new Item.Properties().tab(COMMON_CIRCUITS);
        //#endif
    }

    // Register Items -------------------------------------------------------------------------------------
    private static void registerItem(String name, Item item) {
        registerItem(new ResourceLocation("commoncircuits", name), item);
    }

    private static void registerItem(ResourceLocation resourceLocation, Item item) {
        if (item instanceof BlockItem)
            ((BlockItem) item).registerBlocks(Item.BY_BLOCK, item);
        //#if MC>=11904
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
        //#else
        //$$ Registry.register(Registry.ITEM, resourceLocation, item);
        //#endif
        COMMON_CIRCUITS_ITEMS.add(item);
    }

    public static void registerItems() {
        registerItem("high_power_redstone", HIGH_POWER_REDSTONE);
        registerItem("super_power_redstone", SUPER_POWER_REDSTONE);
        registerItem("advanced_redstone_wire_1", ADVANCED_REDSTONE_WIRE_1);
        registerItem("advanced_redstone_wire_2", ADVANCED_REDSTONE_WIRE_2);
        registerItem("advanced_redstone_wire_4", ADVANCED_REDSTONE_WIRE_4);
        registerItem("advanced_redstone_wire_8", ADVANCED_REDSTONE_WIRE_8);
        registerItem("and_gate_plate", AND_GATE_PLATE);
        registerItem("or_gate_plate", OR_GATE_PLATE);
        registerItem("xor_gate_plate", XOR_GATE_PLATE);
        registerItem("not_gate_plate", NOT_GATE_PLATE);
        registerItem("nand_gate_plate", NAND_GATE_PLATE);
        registerItem("nor_gate_plate", NOR_GATE_PLATE);
        registerItem("xnor_gate_plate", XNOR_GATE_PLATE);
        registerItem("redstone_clock", REDSTONE_CLOCK);
        registerItem("positive_pulse_generator", POSITIVE_PULSE_GENERATOR);
        registerItem("negative_pulse_generator", NEGATIVE_PULSE_GENERATOR);
        registerItem("silver_ore", SILVER_ORE);
        registerItem("silver_block", SILVER_BLOCK);
        registerItem("semiconductor_bench", SEMICONDUCTOR_BENCH);
        registerItem("wire_connector", WIRE_CONNECTOR);

        registerItem("copper_dust", COPPER_DUST);
        registerItem("silver_dust", SILVER_DUST);
        registerItem("raw_silver", RAW_SILVER);
        registerItem("silver_ingot", SILVER_INGOT);
        registerItem("p_semiconductor", P_SEMICONDUCTOR);
        registerItem("n_semiconductor", N_SEMICONDUCTOR);
        registerItem("black_wax", BLACK_WAX);
        registerItem("diode", DIODE);
        registerItem("pmos", PMOS);
        registerItem("nmos", NMOS);
        registerItem("npn_bjt", NPN_BJT);
        registerItem("pnp_bjt", PNP_BJT);
        registerItem("n_jfet", N_JFET);
        registerItem("p_jfet", P_JFET);
        registerItem("and_gate", AND_GATE);
        registerItem("or_gate", OR_GATE);
        registerItem("xor_gate", XOR_GATE);
        registerItem("not_gate", NOT_GATE);
        registerItem("nand_gate", NAND_GATE);
        registerItem("nor_gate", NOR_GATE);
        registerItem("xnor_gate", XNOR_GATE);
        registerItem("base_plate", BASE_PLATE);

        registerItem("silver_wrench", SILVER_WRENCH);

        //#if MC>=11904
        ItemGroupEvents.modifyEntriesEvent(COMMON_CIRCUITS).register(content -> COMMON_CIRCUITS_ITEMS.forEach(content::prepend));
        //#endif
    }
}
