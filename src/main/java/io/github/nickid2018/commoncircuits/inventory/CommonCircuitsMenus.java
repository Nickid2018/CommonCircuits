package io.github.nickid2018.commoncircuits.inventory;

import io.github.nickid2018.commoncircuits.util.TriFunction;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiFunction;

public class CommonCircuitsMenus {

    public static final MenuType<SemiconductorBenchMenu> SEMICONDUCTOR_BENCH = registerMenuType("semiconductor_bench", SemiconductorBenchMenu::new);
    public static final MenuType<WireConnectorMenu> WIRE_CONNECTOR = registerExtendedMenuType("wire_connector", WireConnectorMenu::new);

    public static <T extends AbstractContainerMenu> MenuType<T> registerMenuType(
            String name, BiFunction<Integer, Inventory, T> type) {
        return ScreenHandlerRegistry.registerSimple(new ResourceLocation("commoncircuits", name), type::apply);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> registerExtendedMenuType(
            String name, TriFunction<Integer, Inventory, FriendlyByteBuf, T> type) {
        return ScreenHandlerRegistry.registerExtended(new ResourceLocation("commoncircuits", name), type::apply);
    }

    public static void registerMenus() {
    }
}
