package io.github.nickid2018.commoncircuits.inventory;

import io.github.nickid2018.commoncircuits.block.entity.WireConnectorBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WireConnectorMenu extends AbstractContainerMenu {

    private final List<WireConnectorBlockEntity.ConnectEntry> dataAccess;
    private final Inventory playerInventory;

    public WireConnectorMenu(int syncID, Inventory playerInventory) {
        this(syncID, playerInventory, new ArrayList<>());
    }

    public WireConnectorMenu(int syncID, Inventory playerInventory, List<WireConnectorBlockEntity.ConnectEntry> dataAccess) {
        super(CommonCircuitsMenus.WIRE_CONNECTOR, syncID);
        this.dataAccess = dataAccess;
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
}
