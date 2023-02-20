package io.github.nickid2018.commoncircuits.inventory;

import io.github.nickid2018.commoncircuits.block.entity.WireConnectorBlockEntity;
import io.github.nickid2018.commoncircuits.network.CommonCircuitsNetwork;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.impl.screenhandler.client.ClientNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WireConnectorMenu extends AbstractContainerMenu {

    private final List<WireConnectorBlockEntity.ConnectEntry> dataAccess;
    private final List<WireConnectorBlockEntity.ConnectEntry> prevDataAccess = new ArrayList<>();
    private final Inventory playerInventory;
    private final BlockPos pos;

    public WireConnectorMenu(int syncID, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncID, playerInventory, buf.readBlockPos(), WireConnectorBlockEntity.readConnections(buf));
    }

    public WireConnectorMenu(int syncID, Inventory playerInventory, BlockPos pos, List<WireConnectorBlockEntity.ConnectEntry> dataAccess) {
        super(CommonCircuitsMenus.WIRE_CONNECTOR, syncID);
        this.pos = pos;
        this.dataAccess = dataAccess;
        prevDataAccess.addAll(dataAccess);
        this.playerInventory = playerInventory;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return playerInventory.stillValid(player);
    }

    public void onClose() {
        if (prevDataAccess.size() == dataAccess.size()) {
            boolean isChanged = false;
            for (int i = 0; i < prevDataAccess.size(); i++) {
                if (!prevDataAccess.get(i).equals(dataAccess.get(i))) {
                    isChanged = true;
                    break;
                }
            }
            if (!isChanged)
                return;
        }
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeNbt(WireConnectorBlockEntity.writeConnections(dataAccess));
        ClientPlayNetworking.send(CommonCircuitsNetwork.WIRE_CONNECTOR_UPDATE, buf);
    }
}
