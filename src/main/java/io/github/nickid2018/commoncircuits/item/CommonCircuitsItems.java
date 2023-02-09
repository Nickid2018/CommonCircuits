package io.github.nickid2018.commoncircuits.item;

//#if MC>=11903
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

    //#if MC>=11903
    public static final CreativeModeTab COMMON_CIRCUITS = FabricItemGroup.builder(
            new ResourceLocation("commoncircuits", "commoncircuits")
    ).icon(() -> new ItemStack(CommonCircuitsBlocks.HIGH_POWER_REDSTONE_WIRE)).build();
    //#else
    //$$ public static final CreativeModeTab COMMON_CIRCUITS = FabricItemGroupBuilder.create(
    //$$        new ResourceLocation("commoncircuits", "commoncircuits")
    //$$ ).icon(() -> new ItemStack(CommonCircuitsBlocks.HIGH_POWER_REDSTONE_WIRE)).build();
    //#endif

    public static final List<Item> COMMON_CIRCUITS_ITEMS = new ArrayList<>();

    public static final Item HIGH_POWER_REDSTONE = itemNameBlockItem(CommonCircuitsBlocks.HIGH_POWER_REDSTONE_WIRE, new Item.Properties());
    public static final Item SUPER_POWER_REDSTONE = itemNameBlockItem(CommonCircuitsBlocks.SUPER_POWER_REDSTONE_WIRE, new Item.Properties());

    private static Item itemNameBlockItem(Block block, Item.Properties properties) {
        //#if MC>=11903
        return new ItemNameBlockItem(block, properties);
        //#else
        //$$ return new ItemNameBlockItem(block, properties.tab(COMMON_CIRCUITS));
        //#endif
    }

    private static void registerItem(String name, Item item) {
        registerItem(new ResourceLocation("commoncircuits", name), item);
    }

    private static void registerItem(ResourceLocation resourceLocation, Item item) {
        if (item instanceof BlockItem)
            ((BlockItem) item).registerBlocks(Item.BY_BLOCK, item);
        //#if MC>=11903
        Registry.register(BuiltInRegistries.ITEM, resourceLocation, item);
        //#else
        //$$ Registry.register(Registry.ITEM, resourceLocation, item);
        //#endif
        COMMON_CIRCUITS_ITEMS.add(item);
    }

    public static void registerItems() {
        registerItem("high_power_redstone", HIGH_POWER_REDSTONE);
        registerItem("super_power_redstone", SUPER_POWER_REDSTONE);

        //#if MC>=11903
        ItemGroupEvents.modifyEntriesEvent(COMMON_CIRCUITS).register(content -> COMMON_CIRCUITS_ITEMS.forEach(content::prepend));
        //#endif
    }
}
