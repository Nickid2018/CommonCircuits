package io.github.nickid2018.commoncircuits.inventory;

import io.github.nickid2018.commoncircuits.client.gui.CommonCircuitsScreens;
import io.github.nickid2018.commoncircuits.item.CommonCircuitsItems;
import io.github.nickid2018.commoncircuits.tags.CommonCircuitsTags;
import io.github.nickid2018.commoncircuits.util.CompatUtil;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SemiconductorBenchMenu extends AbstractContainerMenu {

    private final Container container;

    public SemiconductorBenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(6));
    }

    public SemiconductorBenchMenu(int syncId, Inventory inventory, Container container) {
        super(CommonCircuitsScreens.SEMICONDUCTOR_BENCH, syncId);
        checkContainerSize(container, 6);
        this.container = container;
        container.startOpen(inventory.player);
        addSlot(new ConditionSlot(container, 0, 36, 23, itemStack -> CompatUtil.isSameItemTag(itemStack, CommonCircuitsTags.COPPER_DUSTS)));
        addSlot(new ConditionSlot(container, 1, 36, 51, itemStack -> CompatUtil.isSameItem(itemStack, CommonCircuitsItems.BLACK_WAX)));
        addSlot(new ConditionSlot(container, 2, 36, 79, itemStack -> CompatUtil.isSameItem(itemStack, CommonCircuitsItems.P_SEMICONDUCTOR)));
        addSlot(new ConditionSlot(container, 3, 36, 107, itemStack -> CompatUtil.isSameItem(itemStack, CommonCircuitsItems.N_SEMICONDUCTOR)));
        addSlot(new ConditionSlot(container, 4, 108, 107, itemStack -> CompatUtil.isSameItem(itemStack, Items.BLAZE_POWDER)));
        addSlot(new ConditionSlot(container, 5, 180, 107, itemStack -> false));
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlot(new Slot(inventory, j + i * 9 + 9, 36 + j * 18, 137 + i * 18));
        for (int i = 0; i < 9; i++)
            addSlot(new Slot(inventory, i, 36 + i * 18, 195));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            Item item = originalStack.getItem();
            newStack = originalStack.copy();
            if (invSlot < container.getContainerSize() && !moveItemStackTo(originalStack, container.getContainerSize(), slots.size(), true))
                return ItemStack.EMPTY;
            else if (!moveItemStackTo(originalStack, 0, container.getContainerSize(), false))
                return ItemStack.EMPTY;

            if (originalStack.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
