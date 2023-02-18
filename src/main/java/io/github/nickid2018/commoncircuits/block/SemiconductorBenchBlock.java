package io.github.nickid2018.commoncircuits.block;

import io.github.nickid2018.commoncircuits.block.entity.SemiconductorBenchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

//#if MC>=11701
import net.minecraft.world.level.block.entity.BlockEntityTicker;
//#else
//$$ import net.minecraft.world.level.BlockGetter;
//#endif

public class SemiconductorBenchBlock extends BaseEntityBlock {

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    protected SemiconductorBenchBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction direction = blockPlaceContext.getHorizontalDirection();
        return defaultBlockState().setValue(FACING, direction.getOpposite());
    }

    @Nullable
    @Override
    //#if MC>=11701
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SemiconductorBenchBlockEntity(blockPos, blockState);
    }
    //#else
    //$$ public BlockEntity newBlockEntity(BlockGetter blockPos) {
    //$$     return new SemiconductorBenchBlockEntity();
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
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getBlock() != blockState2.getBlock()) {
            if (!blockState.is(blockState2.getBlock())) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof SemiconductorBenchBlockEntity)
                    Containers.dropContents(level, blockPos, ((SemiconductorBenchBlockEntity) blockEntity).getItems());
            }
            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    //#if MC>=11701
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ?
                null :
                createTickerHelper(blockEntityType, CommonCircuitsBlocks.SEMICONDUCTOR_BENCH_BLOCK_ENTITY, SemiconductorBenchBlockEntity::serverTick);
    }
    //#endif
}
