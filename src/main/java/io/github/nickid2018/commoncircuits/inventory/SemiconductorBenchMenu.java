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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SemiconductorBenchMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData containerData;

    public static final int[][][] RECIPES = new int[][][] {
            {{2, 1, 1, 1}},
            {{3, 1, 1, 2}, {3, 1, 2, 1}},
            {{3, 1, 2, 1}, {3, 1, 1, 2}},
            {{4, 1, 2, 1}, {4, 1, 1, 2}}
    };

    public static final Item[][] RESULT = new Item[][] {
            {CommonCircuitsItems.DIODE},
            {CommonCircuitsItems.NPN_BJT, CommonCircuitsItems.PNP_BJT},
            {CommonCircuitsItems.N_JFET, CommonCircuitsItems.P_JFET},
            {CommonCircuitsItems.NMOS, CommonCircuitsItems.PMOS}
    };

    private Slot[] slotData = new Slot[6];

    public SemiconductorBenchMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(6), new SimpleContainerData(2));
    }

    public SemiconductorBenchMenu(int syncId, Inventory inventory, Container container, ContainerData containerData) {
        super(CommonCircuitsScreens.SEMICONDUCTOR_BENCH, syncId);
        checkContainerSize(container, 6);
        checkContainerDataCount(containerData, 2);
        this.container = container;
        this.containerData = containerData;
        container.startOpen(inventory.player);
        addSlot(slotData[0] = new ConditionSlot(container, 0, 36, 23, itemStack -> CompatUtil.isSameItemTag(itemStack, CommonCircuitsTags.COPPER_DUSTS)));
        addSlot(slotData[1] = new ConditionSlot(container, 1, 36, 51, itemStack -> CompatUtil.isSameItem(itemStack, CommonCircuitsItems.BLACK_WAX)));
        addSlot(slotData[2] = new ConditionSlot(container, 2, 36, 79, itemStack -> CompatUtil.isSameItem(itemStack, CommonCircuitsItems.P_SEMICONDUCTOR)));
        addSlot(slotData[3] = new ConditionSlot(container, 3, 36, 107, itemStack -> CompatUtil.isSameItem(itemStack, CommonCircuitsItems.N_SEMICONDUCTOR)));
        addSlot(slotData[4] = new ConditionSlot(container, 4, 108, 107, itemStack -> CompatUtil.isSameItem(itemStack, Items.BLAZE_POWDER)));
        addSlot(slotData[5] = new ConditionSlot(container, 5, 180, 107, itemStack -> false));
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlot(new Slot(inventory, j + i * 9 + 9, 36 + j * 18, 137 + i * 18));
        for (int i = 0; i < 9; i++)
            addSlot(new Slot(inventory, i, 36 + i * 18, 195));
        addDataSlots(containerData);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
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

    @Override
    public boolean clickMenuButton(Player player, int buttonID) {
        if (buttonID < 4) {
            containerData.set(0, buttonID);
            return true;
        } else if (buttonID < 6 && getBlazeLevel() > 0) {
            int mode = getMode();
            if (mode == 0 && buttonID == 5)
                return false;
            for (int i = 0; i < 4; i++)
                if (slotData[i].getItem().getCount() - RECIPES[mode][buttonID - 4][i] < 0)
                    return false;
            if (slotData[5].hasItem() && !CompatUtil.isSameItem(slotData[5].getItem(), RESULT[mode][buttonID - 4]))
                return false;
            containerData.set(1, getBlazeLevel() - 1);
            for (int i = 0; i < 4; i++)
                slotData[i].getItem().shrink(RECIPES[mode][buttonID - 4][i]);
            if (!slotData[5].hasItem())
                slotData[5].set(new ItemStack(RESULT[mode][buttonID - 4]));
            else
                slotData[5].getItem().grow(1);
            return true;
        }
        return false;
    }

    public int getMode() {
        return containerData.get(0);
    }

    public int getBlazeLevel() {
        return containerData.get(1);
    }
}
