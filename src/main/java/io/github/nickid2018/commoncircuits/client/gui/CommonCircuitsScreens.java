package io.github.nickid2018.commoncircuits.client.gui;

import io.github.nickid2018.commoncircuits.inventory.SemiconductorBenchMenu;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.BiFunction;

//#if MC>=11903
import net.minecraft.core.registries.BuiltInRegistries;
//#else
//$$ import net.minecraft.core.Registry;
//$$ import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
//#endif

public class CommonCircuitsScreens {

    public static final MenuType<SemiconductorBenchMenu> SEMICONDUCTOR_BENCH = registerMenuTypes("semiconductor_bench", SemiconductorBenchMenu::new);

    public static <T extends AbstractContainerMenu> MenuType<T> registerMenuTypes(String name, BiFunction<Integer, Inventory, T> type) {
        //#if MC>=11903
        return Registry.register(BuiltInRegistries.MENU, new ResourceLocation("commoncircuits", name), new MenuType<>(type::apply));
        //#elseif MC>=11802
        //$$ MenuType<T> menuType = new MenuType<>(type::apply);
        //$$ return Registry.register(Registry.MENU, new ResourceLocation("commoncircuits", name), new MenuType<>(type::apply));
        //#else
        //$$ return ScreenHandlerRegistry.registerSimple(new ResourceLocation("commoncircuits", name), type::apply);
        //#endif
    }

    public static void registerScreens() {
        ScreenRegistry.<SemiconductorBenchMenu, SemiconductorBenchScreen>register(SEMICONDUCTOR_BENCH, SemiconductorBenchScreen::new);
    }
}
