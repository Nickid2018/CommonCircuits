package io.github.nickid2018.commoncircuits.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityAdapter extends BlockEntity {

    //#if MC>=11701
    public BlockEntityAdapter(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    //#else
    //$$ public BlockEntityAdapter(BlockEntityType<?> blockEntityType) {
    //$$     super(blockEntityType);
    //$$ }
    //#endif

    public void readParsed(CompoundTag compoundTag) {
    }

    public void writeParsed(CompoundTag compoundTag) {
    }

    @Override
    //#if MC>=11802
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
    //#else
    //$$ public CompoundTag save(CompoundTag compoundTag) {
    //$$     super.save(compoundTag);
    //#endif
        writeParsed(compoundTag);
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
        readParsed(compoundTag);
    }
}
