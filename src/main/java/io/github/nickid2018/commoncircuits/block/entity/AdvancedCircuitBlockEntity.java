package io.github.nickid2018.commoncircuits.block.entity;

import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedCircuitBlockEntity extends BlockEntity implements ChannelEnabled {

    public AdvancedCircuitBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonCircuitsBlocks.ADVANCED_CIRCUIT_BLOCK_ENTITY, blockPos, blockState);
    }

    public int getOutputSignalForChannel(Direction direction, int channel) {
        return 0;
    }
}
