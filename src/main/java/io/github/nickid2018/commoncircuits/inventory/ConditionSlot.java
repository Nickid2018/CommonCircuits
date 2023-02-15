package io.github.nickid2018.commoncircuits.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ConditionSlot extends Slot {

    private final Predicate<ItemStack> condition;

    public ConditionSlot(Container container, int i, int j, int k, Predicate<ItemStack> condition) {
        super(container, i, j, k);
        this.condition = condition;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return condition.test(itemStack);
    }
}
