package io.github.nickid2018.commoncircuits.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.ticks.TickPriority;

//#if MC>=11802
import net.minecraft.tags.TagKey;
//#else
//$$ import net.minecraft.tags.Tag;
//#endif
//#if MC<11904
//$$ import net.minecraft.network.chat.TranslatableComponent;
//$$ import net.minecraft.network.chat.TextComponent;
//#endif

public class CompatUtil {

    public static void scheduleTick(Level level, BlockPos pos, Block block, int delay) {
        scheduleTick(level, pos, block, delay, TickPriority.NORMAL);
    }

    public static void scheduleTick(Level level, BlockPos pos, Block block, int delay, TickPriority priority) {
        //#if MC>=11802
        level.scheduleTick(pos, block, delay, priority);
        //#else
        //$$ level.getBlockTicks().scheduleTick(pos, block, delay, priority);
        //#endif
    }

    public static Component translated(String key, Object... args) {
        //#if MC>=11904
        return Component.translatable(key, args);
        //#else
        //$$ return new TranslatableComponent(key, args);
        //#endif
    }

    public static Component literal(String text) {
        //#if MC>=11904
        return Component.literal(text);
        //#else
        //$$ return new TextComponent(text);
        //#endif
    }

    public static boolean isSameItem(ItemStack itemStack, Item item) {
        //#if MC>=11701
        return itemStack.is(item);
        //#else
        //$$ return itemStack.getItem() == item;
        //#endif
    }

    //#if MC>=11802
    public static boolean isSameItemTag(ItemStack itemStack, TagKey<Item> item) {
    //#else
    //$$ public static boolean isSameItemTag(ItemStack itemStack, Tag<Item> item) {
    //#endif
        //#if MC>=11701
        return itemStack.is(item);
        //#else
        //$$ return itemStack.getItem().is(item);
        //#endif
    }
}
