package io.github.nickid2018.commoncircuits.block;

import io.github.nickid2018.commoncircuits.block.entity.WireConnectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
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
}
