package io.github.nickid2018.commoncircuits;

import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import io.github.nickid2018.commoncircuits.client.model.CommonCircuitsBlockModels;
import io.github.nickid2018.commoncircuits.item.CommonCircuitsItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class CommonCircuitsClientMain implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CommonCircuitsBlockModels.registerModels();
    }
}
