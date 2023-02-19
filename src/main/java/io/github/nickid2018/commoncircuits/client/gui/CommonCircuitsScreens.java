package io.github.nickid2018.commoncircuits.client.gui;

import io.github.nickid2018.commoncircuits.inventory.CommonCircuitsMenus;
import io.github.nickid2018.commoncircuits.inventory.SemiconductorBenchMenu;
import io.github.nickid2018.commoncircuits.inventory.WireConnectorMenu;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class CommonCircuitsScreens {

    public static void registerScreens() {
        ScreenRegistry.<SemiconductorBenchMenu, SemiconductorBenchScreen>register(CommonCircuitsMenus.SEMICONDUCTOR_BENCH, SemiconductorBenchScreen::new);
        ScreenRegistry.<WireConnectorMenu, WireConnectorScreen>register(CommonCircuitsMenus.WIRE_CONNECTOR, WireConnectorScreen::new);
    }
}
