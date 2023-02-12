package io.github.nickid2018.commoncircuits.block;

import io.github.nickid2018.commoncircuits.util.BlockTicksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class RedstonePulseGeneratorBlock extends Block {

    public static final IntegerProperty INTERVAL = IntegerProperty.create("interval", 1, 16);
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    private final boolean positivePulse;

    public RedstonePulseGeneratorBlock(Properties properties, boolean positivePulse) {
        super(properties);
        this.positivePulse = positivePulse;
        registerDefaultState(getStateDefinition().any().setValue(INTERVAL, 1).setValue(POWERED, positivePulse));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INTERVAL, POWERED);
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        blockState = blockState.cycle(POWERED);
        level.setBlockAndUpdate(blockPos, blockState);
        BlockTicksUtil.scheduleTick(level, blockPos, blockState.getBlock(),
                blockState.getValue(POWERED) == positivePulse ? 2 : blockState.getValue(INTERVAL));
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        BlockTicksUtil.scheduleTick(level, blockPos, blockState.getBlock(), blockState.getValue(INTERVAL));
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        //#if MC>=11701
        if (!player.getAbilities().mayBuild)
            //#else
            //$$ if (!player.abilities.mayBuild)
            //#endif
            return InteractionResult.PASS;
        else {
            level.setBlockAndUpdate(blockPos, blockState.cycle(INTERVAL));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
