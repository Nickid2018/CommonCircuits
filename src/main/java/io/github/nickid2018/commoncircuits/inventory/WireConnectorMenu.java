package io.github.nickid2018.commoncircuits.inventory;

import com.mojang.datafixers.util.Pair;
import io.github.nickid2018.commoncircuits.block.entity.WireConnectorBlockEntity;
import io.github.nickid2018.commoncircuits.network.CommonCircuitsNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class WireConnectorMenu extends AbstractContainerMenu {

    private final List<WireConnectorBlockEntity.ConnectEntry> dataAccess;
    private final Map<Direction, DisplayChannelEntry[]> displayChannelEntries;
    private final Inventory playerInventory;
    private final BlockPos pos;

    public WireConnectorMenu(int syncID, Inventory playerInventory, FriendlyByteBuf buf) {
        super(CommonCircuitsMenus.WIRE_CONNECTOR, syncID);
        this.pos = buf.readBlockPos();
        this.dataAccess = WireConnectorBlockEntity.readConnections(buf);
        this.playerInventory = playerInventory;
        displayChannelEntries = new HashMap<>();
        for (Direction direction : Direction.values()) {
            DisplayChannelEntry[] entries = new DisplayChannelEntry[8];
            for (int i = 0; i < 8; i++)
                entries[i] = new DisplayChannelEntry();
            displayChannelEntries.put(direction, entries);
        }
        for (int i = 0; i < dataAccess.size(); i++) {
            WireConnectorBlockEntity.ConnectEntry entry = dataAccess.get(i);
            for (Pair<Direction, Integer> pair : entry.inputs) {
                displayChannelEntries.get(pair.getFirst())[pair.getSecond()].input = true;
                displayChannelEntries.get(pair.getFirst())[pair.getSecond()].connectionIndex = i;
            }
            for (Pair<Direction, Integer> pair : entry.outputs) {
                displayChannelEntries.get(pair.getFirst())[pair.getSecond()].output = true;
                displayChannelEntries.get(pair.getFirst())[pair.getSecond()].connectionIndex = i;
            }
        }
    }

    public WireConnectorMenu(int syncID, Inventory playerInventory) {
        super(CommonCircuitsMenus.WIRE_CONNECTOR, syncID);
        this.playerInventory = playerInventory;
        dataAccess = null;
        pos = null;
        displayChannelEntries = null;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return playerInventory.stillValid(player);
    }

    public Map<Direction, DisplayChannelEntry[]> getDisplayChannelEntries() {
        return displayChannelEntries;
    }

    public List<WireConnectorBlockEntity.ConnectEntry> getDataAccess() {
        return dataAccess;
    }

    public void confirmAndClose() {
        dataAccess.removeIf(entry -> entry.inputs.isEmpty() && entry.outputs.isEmpty());
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeNbt(WireConnectorBlockEntity.writeConnections(dataAccess));
        ClientPlayNetworking.send(CommonCircuitsNetwork.WIRE_CONNECTOR_UPDATE, buf);
    }

    public boolean toggleInput(Direction direction, int index) {
        DisplayChannelEntry entry = displayChannelEntries.get(direction)[index];
        if (entry.connectionIndex == -1)
            return false;
        WireConnectorBlockEntity.ConnectEntry connectEntry = dataAccess.get(entry.connectionIndex);
        boolean nowInput = entry.input = !entry.input;
        connectEntry.inputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        if (nowInput)
            connectEntry.inputs.add(Pair.of(direction, index));
        if (nowInput && entry.output) {
            entry.output = false;
            connectEntry.outputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        }
        return true;
    }

    public boolean toggleOutput(Direction direction, int index) {
        DisplayChannelEntry entry = displayChannelEntries.get(direction)[index];
        if (entry.connectionIndex == -1)
            return false;
        WireConnectorBlockEntity.ConnectEntry connectEntry = dataAccess.get(entry.connectionIndex);
        boolean nowOutput = entry.output = !entry.output;
        connectEntry.outputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        if (nowOutput)
            connectEntry.outputs.add(Pair.of(direction, index));
        if (nowOutput && entry.input) {
            entry.input = false;
            connectEntry.inputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        }
        return true;
    }

    public boolean removeConnection(Direction direction, int index) {
        DisplayChannelEntry entry = displayChannelEntries.get(direction)[index];
        if (entry.connectionIndex == -1)
            return false;
        WireConnectorBlockEntity.ConnectEntry connectEntry = dataAccess.get(entry.connectionIndex);
        connectEntry.inputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        connectEntry.outputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        entry.input = false;
        entry.output = false;
        entry.connectionIndex = -1;
        return true;
    }

    public boolean nextConnection(Direction direction, int index) {
        if (dataAccess.isEmpty())
            return false;
        DisplayChannelEntry entry = displayChannelEntries.get(direction)[index];
        if (entry.connectionIndex != -1) {
            WireConnectorBlockEntity.ConnectEntry connectEntry = dataAccess.get(entry.connectionIndex);
            connectEntry.inputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
            connectEntry.outputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        }
        entry.connectionIndex = (entry.connectionIndex + 1) % dataAccess.size();
        WireConnectorBlockEntity.ConnectEntry connectEntry = dataAccess.get(entry.connectionIndex);
        connectEntry.inputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        connectEntry.outputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        if (entry.input)
            connectEntry.inputs.add(Pair.of(direction, index));
        if (entry.output)
            connectEntry.outputs.add(Pair.of(direction, index));
        return true;
    }

    public boolean createConnection(Direction direction, int index) {
        DisplayChannelEntry entry = displayChannelEntries.get(direction)[index];
        if (entry.connectionIndex != -1) {
            WireConnectorBlockEntity.ConnectEntry connectEntry = dataAccess.get(entry.connectionIndex);
            connectEntry.inputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
            connectEntry.outputs.removeIf(pair -> pair.getFirst() == direction && pair.getSecond() == index);
        }
        entry.connectionIndex = dataAccess.size();
        dataAccess.add(new WireConnectorBlockEntity.ConnectEntry());
        WireConnectorBlockEntity.ConnectEntry connectEntry = dataAccess.get(entry.connectionIndex);
        if (entry.input)
            connectEntry.inputs.add(Pair.of(direction, index));
        if (entry.output)
            connectEntry.outputs.add(Pair.of(direction, index));
        return true;
    }

    public static class DisplayChannelEntry {
        public boolean input;
        public boolean output;
        public int connectionIndex = -1;
    }
}
