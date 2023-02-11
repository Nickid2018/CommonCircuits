package io.github.nickid2018.commoncircuits.client.model;

import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import io.github.nickid2018.commoncircuits.block.StrongRedStoneWireBlock;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.RenderType;

public class CommonCircuitsBlockModels {

    public static void registerModels() {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> ((StrongRedStoneWireBlock) state.getBlock()).getColor(state),
                CommonCircuitsBlocks.HIGH_POWER_REDSTONE_WIRE, CommonCircuitsBlocks.SUPER_POWER_REDSTONE_WIRE);
        BlockRenderLayerMap.INSTANCE.putBlock(CommonCircuitsBlocks.HIGH_POWER_REDSTONE_WIRE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonCircuitsBlocks.SUPER_POWER_REDSTONE_WIRE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonCircuitsBlocks.AND_GATE_PLATE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonCircuitsBlocks.OR_GATE_PLATE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonCircuitsBlocks.XOR_GATE_PLATE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonCircuitsBlocks.NOT_GATE_PLATE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonCircuitsBlocks.NAND_GATE_PLATE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonCircuitsBlocks.NOR_GATE_PLATE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CommonCircuitsBlocks.XNOR_GATE_PLATE, RenderType.cutout());

    }
}
