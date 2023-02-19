package io.github.nickid2018.commoncircuits.block.entity;

import com.mojang.datafixers.util.Pair;
import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import io.github.nickid2018.commoncircuits.inventory.WireConnectorMenu;
import io.github.nickid2018.commoncircuits.util.CompatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class WireConnectorBlockEntity extends BlockEntityAdapter implements ChannelEnabled, MenuProvider {

    private List<ConnectEntry> connections;

    //#if MC>=11701
    public WireConnectorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonCircuitsBlocks.WIRE_CONNECTOR_BLOCK_ENTITY, blockPos, blockState);
    }
    //#else
    //$$ public WireConnectorBlockEntity() {
    //$$     super(CommonCircuitsBlocks.WIRE_CONNECTOR_BLOCK_ENTITY);
    //$$ }
    //#endif

    @Override
    public void readParsed(CompoundTag compoundTag) {
        connections = compoundTag.getList("connections", 2).stream()
                .map(CompoundTag.class::cast).map(ConnectEntry::fromNBT).collect(Collectors.toList());
    }

    @Override
    public void writeParsed(CompoundTag compoundTag) {
        ListTag connectionsTag = new ListTag();
        connections.stream().map(ConnectEntry::toNBT).forEach(connectionsTag::add);
        compoundTag.put("connections", connectionsTag);
    }

    @Override
    public int getOutputSignalForChannel(Direction direction, int channel) {
        for (ConnectEntry entry : connections) {
            for (Pair<Direction, Integer> pair : entry.outputs) {
                if (pair.getFirst() == direction && pair.getSecond() == channel)
                    return entry.outputLevelNow;
            }
        }
        return 0;
    }

    @Override
    public Component getDisplayName() {
        return CompatUtil.translated("item.commoncircuits.wire_connector");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncID, Inventory inventory, Player player) {
        return new WireConnectorMenu(syncID, inventory, connections);
    }

    public static class ConnectEntry {
        public List<Pair<Direction, Integer>> inputs;
        public List<Pair<Direction, Integer>> outputs;
        public int outputLevelNow;

        public static ConnectEntry fromNBT(CompoundTag tag) {
            ConnectEntry entry = new ConnectEntry();
            entry.inputs = tag.getList("inputs", 2).stream()
                    .map(CompoundTag.class::cast).map(ConnectEntry::readPair).collect(Collectors.toList());
            entry.outputs = tag.getList("outputs", 2).stream()
                    .map(CompoundTag.class::cast).map(ConnectEntry::readPair).collect(Collectors.toList());
            entry.outputLevelNow = tag.getInt("outputLevelNow");
            return entry;
        }

        public CompoundTag toNBT() {
            CompoundTag tag = new CompoundTag();
            ListTag inputsTag = new ListTag();
            ListTag outputsTag = new ListTag();
            inputs.stream().map(ConnectEntry::writePair).forEach(inputsTag::add);
            outputs.stream().map(ConnectEntry::writePair).forEach(outputsTag::add);
            tag.put("inputs", inputsTag);
            tag.put("outputs", outputsTag);
            tag.putInt("outputLevelNow", outputLevelNow);
            return tag;
        }

        private static CompoundTag writePair(Pair<Direction, Integer> directionIntegerPair) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("direction", directionIntegerPair.getFirst().get3DDataValue());
            tag.putInt("channel", directionIntegerPair.getSecond());
            return tag;
        }

        private static Pair<Direction, Integer> readPair(CompoundTag compoundTag) {
            return new Pair<>(Direction.from3DDataValue(compoundTag.getInt("direction")), compoundTag.getInt("channel"));
        }
    }
}
