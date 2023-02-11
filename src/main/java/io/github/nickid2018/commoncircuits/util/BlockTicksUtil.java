package io.github.nickid2018.commoncircuits.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.ticks.TickPriority;

public class BlockTicksUtil {

    public static void scheduleTick(Level level, BlockPos pos, Block block, int delay) {
        scheduleTick(level, pos, block, delay, TickPriority.NORMAL);
    }

    public static void scheduleTick(Level level, BlockPos pos, Block block, int delay, TickPriority priority) {
        //#if MC>=11802
        level.scheduleTick(pos, block, delay, priority);
        //#else
        //$$ level.getBlockTicks().scheduleTick(pos, block, delay, priority);
        //#endif
    }
}
