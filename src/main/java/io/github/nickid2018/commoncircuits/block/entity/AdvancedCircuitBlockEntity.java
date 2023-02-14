package io.github.nickid2018.commoncircuits.block.entity;

import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedCircuitBlockEntity extends BlockEntity implements ChannelEnabled {

    //#if MC>=11701
    public AdvancedCircuitBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonCircuitsBlocks.ADVANCED_CIRCUIT_BLOCK_ENTITY, blockPos, blockState);
    }
    //#else
    //$$ public AdvancedCircuitBlockEntity() {
    //$$     super(CommonCircuitsBlocks.ADVANCED_CIRCUIT_BLOCK_ENTITY);
    //$$ }
    //#endif

    public int getOutputSignalForChannel(Direction direction, int channel) {
        return 0;
    }
}
