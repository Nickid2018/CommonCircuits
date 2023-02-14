package io.github.nickid2018.commoncircuits.block.entity;

import io.github.nickid2018.commoncircuits.block.AdvancedRedstoneWireBlock;
import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class AdvancedRedstoneWireBlockEntity extends BlockEntity {

    private int[] channels;

    //#if MC>=11701
    public AdvancedRedstoneWireBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonCircuitsBlocks.ADVANCED_REDSTONE_WIRE_BLOCK_ENTITY, blockPos, blockState);
        channels = new int[((AdvancedRedstoneWireBlock) blockState.getBlock()).getChannels()];
    }
    //#else
    //$$ public AdvancedRedstoneWireBlockEntity() {
    //$$     super(CommonCircuitsBlocks.ADVANCED_REDSTONE_WIRE_BLOCK_ENTITY);
    //$$     channels = new int[8];
    //$$ }
    //$$
    //$$ public AdvancedRedstoneWireBlockEntity(int channelCount) {
    //$$    super(CommonCircuitsBlocks.ADVANCED_REDSTONE_WIRE_BLOCK_ENTITY);
    //$$    channels = new int[channelCount];
    //$$ }
    //#endif

    private void updateNeighbors() {
        setChanged();
        if (level != null)
            for (int i = 0; i < AdvancedRedstoneWireBlock.DIRECTIONS.length; i++) {
                if (getBlockState().getValue(AdvancedRedstoneWireBlock.DIRECTIONS[i])) {
                    BlockPos pos = worldPosition.relative(Direction.values()[i]);
                    BlockState state = level.getBlockState(pos);
                    Block block = state.getBlock();
                    if (!(block instanceof AdvancedRedstoneWireBlock))
                        level.neighborChanged(pos, block, worldPosition);
                }
            }
    }

    private void updateChannelForce(int channelNumber, int newValue) {
        if (channels[channelNumber] != newValue) {
            channels[channelNumber] = newValue;
            updateNeighbors();
        }
    }

    private void updateAllChannelForce(int[] newValue) {
        boolean needUpdate = false;
        for (int channelNumber = 0; channelNumber < channels.length; channelNumber++) {
            if (channels[channelNumber] != newValue[channelNumber]) {
                channels[channelNumber] = newValue[channelNumber];
                needUpdate = true;
            }
        }
        if (needUpdate)
            updateNeighbors();
    }

    private int chooseNearbyBestSignal(int channel) {
        int maxSignal = 0;
        if (level != null)
            for (int i = 0; i < AdvancedRedstoneWireBlock.DIRECTIONS.length; i++) {
                Direction direction = Direction.values()[i];
                if (level.getBlockState(worldPosition).getValue(AdvancedRedstoneWireBlock.DIRECTIONS[i])) {
                    BlockPos pos = worldPosition.relative(direction);
                    BlockState state = level.getBlockState(pos);
                    Block block = state.getBlock();
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof ChannelEnabled)
                        maxSignal = Math.max(maxSignal, ((ChannelEnabled) blockEntity).getOutputSignalForChannel(direction.getOpposite(), channel));
                    else if (!(block instanceof AdvancedRedstoneWireBlock) && !state.is(Blocks.REDSTONE_WIRE)) {
                        int signal = level.getSignal(pos, direction);
                        if (signal > maxSignal)
                            maxSignal = signal;
                    }
                }
            }
        return maxSignal;
    }

    public int getChannelCount() {
        return channels.length;
    }

    public void updateChannel(int channelNumber) {
        if (level != null) {
            Queue<BlockPos> queue = new ArrayDeque<>();
            Set<BlockPos> visited = new HashSet<>();
            queue.add(worldPosition);
            int maxSignal = chooseNearbyBestSignal(channelNumber);
            while (!queue.isEmpty()) {
                BlockPos pos = queue.poll();
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof AdvancedRedstoneWireBlock) {
                    AdvancedRedstoneWireBlockEntity entity = (AdvancedRedstoneWireBlockEntity) level.getBlockEntity(pos);
                    if (entity != null && entity.getChannelCount() == getChannelCount()) {
                        maxSignal = Math.max(maxSignal, entity.chooseNearbyBestSignal(channelNumber));
                        for (int i = 0; i < AdvancedRedstoneWireBlock.DIRECTIONS.length; i++) {
                            if (state.getValue(AdvancedRedstoneWireBlock.DIRECTIONS[i])) {
                                BlockPos pos1 = pos.relative(Direction.values()[i]);
                                if (!visited.contains(pos1))
                                    queue.add(pos1);
                            }
                        }
                    }
                }
                visited.add(pos);
            }
            int finalMaxSignal = maxSignal;
            for (BlockPos blockPos : visited) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof AdvancedRedstoneWireBlockEntity)
                    ((AdvancedRedstoneWireBlockEntity) blockEntity).updateChannelForce(channelNumber, finalMaxSignal);
            }
        }
    }

    public void updateAllChannels() {
        if (level != null) {
            Queue<BlockPos> queue = new ArrayDeque<>();
            Set<BlockPos> visited = new HashSet<>();
            queue.add(worldPosition);
            int[] signals = new int[channels.length];
            for (int i = 0; i < channels.length; i++)
                signals[i] = chooseNearbyBestSignal(i);
            while (!queue.isEmpty()) {
                BlockPos pos = queue.poll();
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof AdvancedRedstoneWireBlock) {
                    AdvancedRedstoneWireBlockEntity entity = (AdvancedRedstoneWireBlockEntity) level.getBlockEntity(pos);
                    if (entity != null && entity.getChannelCount() == getChannelCount()) {
                        for (int i = 0; i < channels.length; i++)
                            signals[i] = Math.max(signals[i], entity.chooseNearbyBestSignal(i));
                        for (int i = 0; i < AdvancedRedstoneWireBlock.DIRECTIONS.length; i++) {
                            if (state.getValue(AdvancedRedstoneWireBlock.DIRECTIONS[i])) {
                                BlockPos pos1 = pos.relative(Direction.values()[i]);
                                if (!visited.contains(pos1))
                                    queue.add(pos1);
                            }
                        }
                    }
                }
                visited.add(pos);
            }
            for (BlockPos blockPos : visited) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof AdvancedRedstoneWireBlockEntity)
                    ((AdvancedRedstoneWireBlockEntity) blockEntity).updateAllChannelForce(signals);
            }
        }
    }

    public int getSignalRaw(int channelNumber) {
        return channels[channelNumber];
    }

    public int getMaxSignalRaw() {
        int max = 0;
        for (int i : channels)
            if (i > max)
                max = i;
        return max;
    }

    @Override
    //#if MC>=11802
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
    //#else
    //$$ public CompoundTag save(CompoundTag compoundTag) {
    //$$     super.save(compoundTag);
    //#endif
        compoundTag.putIntArray("channels", channels);
        //#if MC<11802
        //$$ return compoundTag;
        //#endif
    }

    @Override
    //#if MC>=11701
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
    //#else
    //$$ public void load(BlockState state, CompoundTag compoundTag) {
    //$$     super.load(state, compoundTag);
    //#endif
        channels = compoundTag.getIntArray("channels");
    }
}
