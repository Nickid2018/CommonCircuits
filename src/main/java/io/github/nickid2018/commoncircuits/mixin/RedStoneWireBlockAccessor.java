package io.github.nickid2018.commoncircuits.mixin;

import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RedStoneWireBlock.class)
public interface RedStoneWireBlockAccessor {

    @Invoker("calculateShape")
    VoxelShape calculateRedstoneShape(BlockState blockState);

    @Accessor("shouldSignal")
    boolean getShouldSignal();

    @Accessor("shouldSignal")
    void setShouldSignal(boolean shouldSignal);
}
