package io.github.nickid2018.commoncircuits;

import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import io.github.nickid2018.commoncircuits.item.CommonCircuitsItems;
import net.fabricmc.api.ModInitializer;

public class CommonCircuitsMain implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonCircuitsBlocks.registerBlocks();
        CommonCircuitsItems.registerItems();
    }
}
