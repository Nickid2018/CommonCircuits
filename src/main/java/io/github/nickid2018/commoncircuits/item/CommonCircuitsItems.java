package io.github.nickid2018.commoncircuits.item;

import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import net.minecraft.core.Registry;
//#if MC>=11903
import net.minecraft.core.registries.BuiltInRegistries;
//#endif
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class CommonCircuitsItems {

    public static final Item HIGH_POWER_REDSTONE = new ItemNameBlockItem(CommonCircuitsBlocks.HIGH_POWER_REDSTONE_WIRE, new Item.Properties());
    public static final Item SUPER_POWER_REDSTONE = new ItemNameBlockItem(CommonCircuitsBlocks.SUPER_POWER_REDSTONE_WIRE, new Item.Properties());

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
    }

    public static void registerItems() {
        registerItem("high_power_redstone", HIGH_POWER_REDSTONE);
        registerItem("super_power_redstone", SUPER_POWER_REDSTONE);
    }
}
