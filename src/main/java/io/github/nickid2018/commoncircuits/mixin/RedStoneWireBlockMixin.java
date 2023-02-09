package io.github.nickid2018.commoncircuits.mixin;

import io.github.nickid2018.commoncircuits.block.StrongRedStoneWireBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {

    @Redirect(
            method = "updateIndirectNeighbourShapes",
            //#if MC>=11903
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
            //#else
            //$$ at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0)
            //#endif
    )
    public boolean updateIndirectNeighbourShapesAdd(BlockState instance, Block block) {
        return instance.is(Blocks.REDSTONE_WIRE) || instance.getBlock() instanceof StrongRedStoneWireBlock;
    }

    @Inject(
            method = "getWireSignal",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getWireSignalAdd(BlockState blockState, CallbackInfoReturnable<Integer> cir) {
        if (blockState.getBlock() instanceof StrongRedStoneWireBlock)
            cir.setReturnValue(((StrongRedStoneWireBlock) blockState.getBlock()).getRawConvertedSignal(blockState));
    }

    @Redirect(
            method = "checkCornerChangeAt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    public boolean checkCornerChangeAtAdd(BlockState instance, Block block) {
        return instance.is(Blocks.REDSTONE_WIRE) || instance.getBlock() instanceof StrongRedStoneWireBlock;
    }

    @Inject(
            method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void shouldConnectToAdd(BlockState blockState, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.getBlock() instanceof StrongRedStoneWireBlock)
            cir.setReturnValue(true);
    }
}
