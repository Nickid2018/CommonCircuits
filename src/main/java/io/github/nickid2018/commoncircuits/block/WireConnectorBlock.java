package io.github.nickid2018.commoncircuits.block;

import io.github.nickid2018.commoncircuits.block.entity.WireConnectorBlockEntity;
import io.github.nickid2018.commoncircuits.util.CompatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

//#if MC<11701
//$$ import net.minecraft.world.level.BlockGetter;
//#endif

public class WireConnectorBlock extends BaseEntityBlock {

    protected WireConnectorBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    //#if MC>=11701
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WireConnectorBlockEntity(blockPos, blockState);
    }
    //#else
    //$$ public BlockEntity newBlockEntity(BlockGetter blockPos) {
    //$$     return new WireConnectorBlockEntity();
    //$$ }
    //#endif

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            MenuProvider provider = blockState.getMenuProvider(level, blockPos);
            if (provider != null)
                player.openMenu(provider);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (entity instanceof WireConnectorBlockEntity) {
            if (((WireConnectorBlockEntity) entity).update())
                CompatUtil.scheduleTick(level, blockPos, blockState.getBlock(), 1);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        serverLevel.updateNeighborsAt(blockPos, blockState.getBlock());
    }
}
