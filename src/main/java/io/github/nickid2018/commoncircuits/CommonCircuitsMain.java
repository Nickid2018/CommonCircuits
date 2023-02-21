package io.github.nickid2018.commoncircuits;

import io.github.nickid2018.commoncircuits.inventory.CommonCircuitsMenus;
import io.github.nickid2018.commoncircuits.levelgen.*;

import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import io.github.nickid2018.commoncircuits.item.CommonCircuitsItems;
import io.github.nickid2018.commoncircuits.network.CommonCircuitsNetwork;
import net.fabricmc.api.ModInitializer;

public class CommonCircuitsMain implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonCircuitsBlocks.registerBlocks();
        CommonCircuitsItems.registerItems();
        CommonCircuitsMenus.registerMenus();
        CommonCircuitsNetwork.registerNetworkServer();
        //#if MC>=11802
        CommonCircuitsFeaturesV2.registerFeatures();
        //#elseif MC>=11701
        //$$ CommonCircuitsFeaturesV1.registerFeatures();
        //#else
        //$$ CommonCircuitsFeaturesV0.registerFeatures();
        //#endif
    }
}
