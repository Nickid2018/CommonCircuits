package io.github.nickid2018.commoncircuits.block.entity;

import net.minecraft.core.Direction;

public interface ChannelEnabled {

    int getOutputSignalForChannel(Direction direction, int channel);

    int channelCount();
}
