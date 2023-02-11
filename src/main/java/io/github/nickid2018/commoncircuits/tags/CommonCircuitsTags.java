package io.github.nickid2018.commoncircuits.tags;

//#if MC>=11903
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
//#elseif MC>=11802
//$$ import net.minecraft.tags.TagKey;
//$$ import net.minecraft.core.Registry;
//#elseif MC>=11701
//$$ import net.minecraft.tags.Tag;
//$$ import net.fabricmc.fabric.api.tag.TagFactory;
//$$ import net.fabricmc.fabric.api.tag.TagRegistry;
//#else
//$$ import net.minecraft.tags.Tag;
//$$ import net.fabricmc.fabric.api.tag.TagRegistry;
//#endif

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class CommonCircuitsTags {

    //#if MC>=11802
    public static TagKey<Item> createItemTag(ResourceLocation location) {
    //#else
    //$$ public static Tag<Item> createItemTag(ResourceLocation location) {
    //#endif
        //#if MC>=11903
        return TagKey.create(Registries.ITEM, location);
        //#elseif MC>=11802
        //$$ return TagKey.create(Registry.ITEM_REGISTRY, location);
        //#elseif MC>=11701
        //$$ return TagFactory.ITEM.create(location);
        //#else
        //$$ return TagRegistry.item(location);
        //#endif
    }
}
