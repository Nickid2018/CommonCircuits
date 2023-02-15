package io.github.nickid2018.commoncircuits.block.entity;

import io.github.nickid2018.commoncircuits.block.CommonCircuitsBlocks;
import io.github.nickid2018.commoncircuits.inventory.SemiconductorBenchMenu;
import io.github.nickid2018.commoncircuits.item.CommonCircuitsItems;
import io.github.nickid2018.commoncircuits.tags.CommonCircuitsTags;
import io.github.nickid2018.commoncircuits.util.CompatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class SemiconductorBenchBlockEntity extends BlockEntityAdapter implements ImplementedContainer, WorldlyContainer, MenuProvider {

    private NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);

    private static final int[] SLOT_DOWN = new int[] {5};
    private static final  int[] SLOT_UP = new int[] {0, 1, 2, 3, 4};

    //#if MC>=11701
    public SemiconductorBenchBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonCircuitsBlocks.SEMICONDUCTOR_BENCH_BLOCK_ENTITY, blockPos, blockState);
    }
    //#else
    //$$ public SemiconductorBenchBlockEntity() {
    //$$     super(CommonCircuitsBlocks.SEMICONDUCTOR_BENCH_BLOCK_ENTITY);
    //$$ }
    //#endif

    @Override
    public Component getDisplayName() {
        return CompatUtil.translated("container.commoncircuits.semiconductor_bench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncID, Inventory inventory, Player player) {
        return new SemiconductorBenchMenu(syncID, inventory, this);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void readParsed(CompoundTag compoundTag) {
        ContainerHelper.loadAllItems(compoundTag, items);
    }

    @Override
    public void writeParsed(CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, items);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN)
            return SLOT_DOWN;
        return SLOT_UP;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return canPlaceItem(i, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN && i == 5;
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        if (i == 5)
            return false;
        if (i == 0)
            return CompatUtil.isSameItemTag(itemStack, CommonCircuitsTags.COPPER_DUSTS);
        if (i == 1)
            return CompatUtil.isSameItem(itemStack, CommonCircuitsItems.BLACK_WAX);
        if (i == 2)
            return CompatUtil.isSameItem(itemStack, CommonCircuitsItems.P_SEMICONDUCTOR);
        if (i == 3)
            return CompatUtil.isSameItem(itemStack, CommonCircuitsItems.N_SEMICONDUCTOR);
        if (i == 4)
            return CompatUtil.isSameItem(itemStack, Items.BLAZE_POWDER);
        return false;
    }
}
