package io.github.nickid2018.commoncircuits.block;

//#if MC>=11903
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;
//#else
//$$ import java.util.Random;
//$$ import com.mojang.math.Vector3f;
//$$ import net.minecraft.world.phys.Vec3;
//#endif

import com.google.common.collect.Sets;
import io.github.nickid2018.commoncircuits.mixin.RedStoneWireBlockAccessor;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static net.minecraft.world.level.block.RedStoneWireBlock.*;

public abstract class StrongRedStoneWireBlock extends Block {

    private final IntegerProperty powerProperty;
    private final int maxPower;
    private final BlockState crossState;
    private final Map<BlockState, VoxelShape> shapeCache = new HashMap<>();
    //#if MC>=11903
    private final Vector3f[] COLORS;
    //#else
    //$$ private final Vec3[] COLORS;
    //#endif

    public StrongRedStoneWireBlock(Properties properties, int maxPower, IntegerProperty powerProperty, float baseG, float baseB) {
        super(properties);
        this.powerProperty = powerProperty;
        this.maxPower = maxPower;
        registerDefaultState(stateDefinition.any()
                .setValue(NORTH, RedstoneSide.NONE)
                .setValue(EAST, RedstoneSide.NONE)
                .setValue(SOUTH, RedstoneSide.NONE)
                .setValue(WEST, RedstoneSide.NONE)
                .setValue(powerProperty, 0));
        crossState = defaultBlockState()
                .setValue(NORTH, RedstoneSide.SIDE)
                .setValue(EAST, RedstoneSide.SIDE)
                .setValue(SOUTH, RedstoneSide.SIDE)
                .setValue(WEST, RedstoneSide.SIDE);
        for (BlockState blockState : getStateDefinition().getPossibleStates()) {
            if (blockState.getValue(powerProperty) != 0)
                continue;
            shapeCache.put(blockState, ((RedStoneWireBlockAccessor) Blocks.REDSTONE_WIRE).calculateRedstoneShape(blockState));
        }
        //#if MC>=11903
        COLORS = Util.make(new Vector3f[maxPower + 1], vec3s -> {
            for (int i = 0; i <= maxPower; ++i) {
                float f = (float) i / maxPower;
                float g = f * 0.6f + (f > 0.0f ? 0.4f : 0.3f);
                float h = Mth.clamp(f * f * 0.7f - 0.5f, baseG, baseB);
                float j = Mth.clamp(f * f * 0.6f - 0.7f, baseG, baseB);
                vec3s[i] = new Vector3f(g, h, j);
            }
        });
        //#else
        //$$ COLORS = Util.make(new Vec3[maxPower + 1], vec3s -> {
        //$$    for (int i = 0; i <= maxPower; ++i) {
        //$$        float f = (float) i / maxPower;
        //$$        float g = f * 0.6f + (f > 0.0f ? 0.4f : 0.3f);
        //$$        float h = Mth.clamp(f * f * 0.7f - 0.5f, baseG, baseB);
        //$$        float j = Mth.clamp(f * f * 0.6f - 0.7f, baseG, baseB);
        //$$        vec3s[i] = new Vec3(g, h, j);
        //$$    }
        //$$ });
        //#endif
    }

    public IntegerProperty getPowerProperty() {
        return powerProperty;
    }

    public static int powerConvert(int now, int sourceRange, int targetRange) {
        if (now == 0)
            return 0;
        if (sourceRange == targetRange)
            return now;
        else if (sourceRange > targetRange) {
            int extend = sourceRange / targetRange;
            return (now - 1) / extend + 1;
        } else {
            int extend = targetRange / sourceRange;
            return now * extend;
        }
    }

    public static boolean shouldSignal() {
        return ((RedStoneWireBlockAccessor) Blocks.REDSTONE_WIRE).getShouldSignal();
    }

    public static void setShouldSignal(boolean shouldSignal) {
        ((RedStoneWireBlockAccessor) Blocks.REDSTONE_WIRE).setShouldSignal(shouldSignal);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return shapeCache.get(blockState.setValue(powerProperty, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return getConnectionState(blockPlaceContext.getLevel(), crossState, blockPlaceContext.getClickedPos());
    }

    private BlockState getConnectionState(BlockGetter blockGetter, BlockState blockState, BlockPos blockPos) {
        boolean bl = isDot(blockState);
        blockState = this.getMissingConnections(blockGetter, this.defaultBlockState().setValue(powerProperty, blockState.getValue(powerProperty)), blockPos);
        if (bl && isDot(blockState)) {
            return blockState;
        }
        boolean bl2 = blockState.getValue(NORTH).isConnected();
        boolean bl3 = blockState.getValue(SOUTH).isConnected();
        boolean bl4 = blockState.getValue(EAST).isConnected();
        boolean bl5 = blockState.getValue(WEST).isConnected();
        boolean bl6 = !bl2 && !bl3;
        boolean bl7 = !bl4 && !bl5;
        if (!bl5 && bl6) {
            blockState = blockState.setValue(WEST, RedstoneSide.SIDE);
        }
        if (!bl4 && bl6) {
            blockState = blockState.setValue(EAST, RedstoneSide.SIDE);
        }
        if (!bl2 && bl7) {
            blockState = blockState.setValue(NORTH, RedstoneSide.SIDE);
        }
        if (!bl3 && bl7) {
            blockState = blockState.setValue(SOUTH, RedstoneSide.SIDE);
        }
        return blockState;
    }

    private BlockState getMissingConnections(BlockGetter blockGetter, BlockState blockState, BlockPos blockPos) {
        boolean bl = !blockGetter.getBlockState(blockPos.above()).isRedstoneConductor(blockGetter, blockPos);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (blockState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected())
                continue;
            RedstoneSide redstoneSide = getConnectingSide(blockGetter, blockPos, direction, bl);
            blockState = blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneSide);
        }
        return blockState;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (direction == Direction.DOWN)
            return blockState;
        if (direction == Direction.UP)
            return getConnectionState(levelAccessor, blockState, blockPos);
        RedstoneSide redstoneSide = this.getConnectingSide(levelAccessor, blockPos, direction);
        if (redstoneSide.isConnected() == blockState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() && !isCross(blockState))
            return blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneSide);
        return getConnectionState(levelAccessor, crossState.setValue(powerProperty, blockState.getValue(powerProperty)).setValue(PROPERTY_BY_DIRECTION.get(direction), redstoneSide), blockPos);
    }

    private static boolean isCross(BlockState blockState) {
        return blockState.getValue(NORTH).isConnected() && blockState.getValue(SOUTH).isConnected() && blockState.getValue(EAST).isConnected() && blockState.getValue(WEST).isConnected();
    }

    private static boolean isDot(BlockState blockState) {
        return !blockState.getValue(NORTH).isConnected() && !blockState.getValue(SOUTH).isConnected() && !blockState.getValue(EAST).isConnected() && !blockState.getValue(WEST).isConnected();
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, int i, int j) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneSide = blockState.getValue(PROPERTY_BY_DIRECTION.get(direction));
            BlockState offsetState = levelAccessor.getBlockState(mutableBlockPos.setWithOffset(blockPos, direction));
            if (redstoneSide != RedstoneSide.NONE && !(offsetState.is(Blocks.REDSTONE_WIRE) || offsetState.getBlock() instanceof StrongRedStoneWireBlock)) {
                mutableBlockPos.move(Direction.DOWN);
                BlockState blockState2 = levelAccessor.getBlockState(mutableBlockPos);
                if (blockState2.is(Blocks.REDSTONE_WIRE) || blockState2.getBlock() instanceof StrongRedStoneWireBlock) {
                    BlockPos blockPos2 = mutableBlockPos.relative(direction.getOpposite());
                    //#if MC>=11903
                    levelAccessor.neighborShapeChanged(direction.getOpposite(), levelAccessor.getBlockState(blockPos2), mutableBlockPos, blockPos2, i, j);
                    //#else
                    //$$ BlockState blockState3 = blockState2.updateShape(direction.getOpposite(), levelAccessor.getBlockState(blockPos2), levelAccessor, mutableBlockPos, blockPos2);
                    //$$ updateOrDestroy(blockState2, blockState3, levelAccessor, mutableBlockPos, i, j);
                    //#endif
                }
                mutableBlockPos.setWithOffset(blockPos, direction).move(Direction.UP);
                BlockState blockState3 = levelAccessor.getBlockState(mutableBlockPos);
                if (blockState3.is(Blocks.REDSTONE_WIRE) || blockState3.getBlock() instanceof StrongRedStoneWireBlock) {
                    BlockPos blockPos3 = mutableBlockPos.relative(direction.getOpposite());
                    //#if MC>=11903
                    levelAccessor.neighborShapeChanged(direction.getOpposite(), levelAccessor.getBlockState(blockPos3), mutableBlockPos, blockPos3, i, j);
                    //#else
                    //$$ BlockState blockState5 = blockState3.updateShape(direction.getOpposite(), levelAccessor.getBlockState(blockPos3), levelAccessor, mutableBlockPos, blockPos3);
                    //$$ updateOrDestroy(blockState3, blockState5, levelAccessor, mutableBlockPos, i, j);
                    //#endif
                }
            }
        }
    }

    private RedstoneSide getConnectingSide(BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return getConnectingSide(blockGetter, blockPos, direction, !blockGetter.getBlockState(blockPos.above()).isRedstoneConductor(blockGetter, blockPos));
    }

    private RedstoneSide getConnectingSide(BlockGetter blockGetter, BlockPos blockPos, Direction direction, boolean bl) {
        BlockPos blockPos2 = blockPos.relative(direction);
        BlockState blockState = blockGetter.getBlockState(blockPos2);
        if (bl && canSurviveOn(blockGetter, blockPos2, blockState) && shouldConnectTo(blockGetter.getBlockState(blockPos2.above()))) {
            if (blockState.isFaceSturdy(blockGetter, blockPos2, direction.getOpposite()))
                return RedstoneSide.UP;
            return RedstoneSide.SIDE;
        }
        if (shouldConnectTo(blockState, direction) || !blockState.isRedstoneConductor(blockGetter, blockPos2) && shouldConnectTo(blockGetter.getBlockState(blockPos2.below())))
            return RedstoneSide.SIDE;
        return RedstoneSide.NONE;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.below();
        BlockState blockState2 = levelReader.getBlockState(blockPos2);
        return canSurviveOn(levelReader, blockPos2, blockState2);
    }

    private boolean canSurviveOn(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return blockState.isFaceSturdy(blockGetter, blockPos, Direction.UP) || blockState.is(Blocks.HOPPER);
    }

    private void updatePowerStrength(Level level, BlockPos blockPos, BlockState blockState) {
        int i = calculateTargetStrength(level, blockPos);
        if (blockState.getValue(powerProperty) != i) {
            if (level.getBlockState(blockPos) == blockState)
                level.setBlock(blockPos, blockState.setValue(powerProperty, i), 2);
            HashSet<BlockPos> set = Sets.newHashSet();
            set.add(blockPos);
            for (Direction direction : Direction.values())
                set.add(blockPos.relative(direction));
            for (BlockPos blockPos2 : set)
                level.updateNeighborsAt(blockPos2, this);
        }
    }

    private int getBestNeighborSignal(Level level, BlockPos blockPos) {
        int i = 0;
        for (Direction direction : Direction.values()) {
            BlockPos pos = blockPos.relative(direction);
            int nowSignal;
            BlockState blockState = level.getBlockState(pos);
            if (blockState.getBlock() instanceof StrongRedStoneWireBlock) {
                StrongRedStoneWireBlock wire = (StrongRedStoneWireBlock) blockState.getBlock();
                nowSignal = powerConvert(wire.getNoConvertSignal(blockState, level, pos, direction), wire.maxPower, maxPower);
            } else
                nowSignal = powerConvert(level.getSignal(pos, direction), 15, maxPower);
            i = Math.max(i, nowSignal);
        }
        return i;
    }

    private int calculateTargetStrength(Level level, BlockPos blockPos) {
        setShouldSignal(false);
        int i = getBestNeighborSignal(level, blockPos);
        setShouldSignal(true);
        int j = 0;
        if (i < maxPower) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos blockPos2 = blockPos.relative(direction);
                BlockState blockState = level.getBlockState(blockPos2);
                j = Math.max(j, getWireSignal(blockState));
                BlockPos blockPos3 = blockPos.above();
                if (blockState.isRedstoneConductor(level, blockPos2) && !level.getBlockState(blockPos3).isRedstoneConductor(level, blockPos3)) {
                    j = Math.max(j, getWireSignal(level.getBlockState(blockPos2.above())));
                    continue;
                }
                if (blockState.isRedstoneConductor(level, blockPos2))
                    continue;
                j = Math.max(j, getWireSignal(level.getBlockState(blockPos2.below())));
            }
        }
        return Math.max(i, j - 1);
    }

    private int getWireSignal(BlockState blockState) {
        if (blockState.is(Blocks.REDSTONE_WIRE))
            return powerConvert(blockState.getValue(RedStoneWireBlock.POWER), 15, maxPower);
        if (blockState.getBlock() instanceof StrongRedStoneWireBlock) {
            StrongRedStoneWireBlock wire = (StrongRedStoneWireBlock) blockState.getBlock();
            return powerConvert(blockState.getValue(wire.powerProperty), wire.maxPower, maxPower);
        }
        return 0;
    }

    private void checkCornerChangeAt(Level level, BlockPos blockPos) {
        if (!level.getBlockState(blockPos).is(Blocks.REDSTONE_WIRE) &&
                !(level.getBlockState(blockPos).getBlock() instanceof StrongRedStoneWireBlock))
            return;
        level.updateNeighborsAt(blockPos, this);
        for (Direction direction : Direction.values())
            level.updateNeighborsAt(blockPos.relative(direction), this);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState2.is(blockState.getBlock()) || level.isClientSide)
            return;
        updatePowerStrength(level, blockPos, blockState);
        for (Direction direction : Direction.Plane.VERTICAL)
            level.updateNeighborsAt(blockPos.relative(direction), this);
        updateNeighborsOfNeighboringWires(level, blockPos);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (bl || blockState.is(blockState2.getBlock()))
            return;
        super.onRemove(blockState, level, blockPos, blockState2, bl);
        if (level.isClientSide)
            return;
        for (Direction direction : Direction.values())
            level.updateNeighborsAt(blockPos.relative(direction), this);
        updatePowerStrength(level, blockPos, blockState);
        updateNeighborsOfNeighboringWires(level, blockPos);
    }

    private void updateNeighborsOfNeighboringWires(Level level, BlockPos blockPos) {
        for (Direction direction : Direction.Plane.HORIZONTAL)
            checkCornerChangeAt(level, blockPos.relative(direction));
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockPos2 = blockPos.relative(direction);
            if (level.getBlockState(blockPos2).isRedstoneConductor(level, blockPos2))
                checkCornerChangeAt(level, blockPos2.above());
            else
                checkCornerChangeAt(level, blockPos2.below());
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (level.isClientSide)
            return;
        if (blockState.canSurvive(level, blockPos))
            updatePowerStrength(level, blockPos, blockState);
        else {
            dropResources(blockState, level, blockPos);
            level.removeBlock(blockPos, false);
        }
    }

    public int getRawConvertedSignal(BlockState blockState) {
        return powerConvert(blockState.getValue(powerProperty), maxPower, 15);
    }

    public int getNoConvertSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        if (!shouldSignal() || direction == Direction.DOWN)
            return 0;
        int i = blockState.getValue(powerProperty);
        if (i == 0)
            return 0;
        if (direction == Direction.UP || getConnectionState(blockGetter, blockState, blockPos).getValue(PROPERTY_BY_DIRECTION.get(direction.getOpposite())).isConnected())
            return i;
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        if (!shouldSignal())
            return 0;
        return blockState.getSignal(blockGetter, blockPos, direction);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return powerConvert(getNoConvertSignal(blockState, blockGetter, blockPos, direction), maxPower, 15);
    }

    protected static boolean shouldConnectTo(BlockState blockState) {
        return shouldConnectTo(blockState, null);
    }

    protected static boolean shouldConnectTo(BlockState blockState, @Nullable Direction direction) {
        if (blockState.is(Blocks.REDSTONE_WIRE))
            return true;
        if (blockState.getBlock() instanceof StrongRedStoneWireBlock)
            return true;
        if (blockState.is(Blocks.REPEATER)) {
            Direction direction2 = blockState.getValue(RepeaterBlock.FACING);
            return direction2 == direction || direction2.getOpposite() == direction;
        }
        if (blockState.is(Blocks.OBSERVER))
            return direction == blockState.getValue(ObserverBlock.FACING);
        return blockState.isSignalSource() && direction != null;
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return shouldSignal();
    }

    //#if MC>=11903
    private void spawnParticlesAlongLine(Level level, RandomSource random, BlockPos blockPos, Vector3f vec3, Direction direction, Direction direction2, float f, float g) {
    //#else
    //$$ private void spawnParticlesAlongLine(Level level, Random random, BlockPos blockPos, Vec3 vec3, Direction direction, Direction direction2, float f, float g) {
    //#endif
        float h = g - f;
        if (random.nextFloat() >= 0.2f * h)
            return;
        float j = f + h * random.nextFloat();
        double d = 0.5 + 0.4375f * direction.getStepX() + j * direction2.getStepX();
        double e = 0.5 + 0.4375f * direction.getStepY() + j * direction2.getStepY();
        double k = 0.5 + 0.4375f * direction.getStepZ() + j * direction2.getStepZ();
        //#if MC>=11903
        level.addParticle(new DustParticleOptions(vec3, 1.0f), blockPos.getX() + d, blockPos.getY() + e, blockPos.getZ() + k, 0.0, 0.0, 0.0);
        //#elseif MC>=11701
        //$$ level.addParticle(new DustParticleOptions(new Vector3f(vec3), 1.0F), blockPos.getX() + d, blockPos.getY() + e, blockPos.getZ() + k, 0.0, 0.0, 0.0);
        //#else
        //$$ level.addParticle(new DustParticleOptions((float) vec3.x(), (float) vec3.y(), (float) vec3.z(), 1.0F), blockPos.getX() + d, blockPos.getY() + e, blockPos.getZ() + k, 0.0, 0.0, 0.0);
        //#endif
    }

    @Override
    //#if MC>=11903
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
    //#else
    //$$ public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
    //#endif
        int i = blockState.getValue(powerProperty);
        if (i == 0)
            return;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneSide = blockState.getValue(PROPERTY_BY_DIRECTION.get(direction));
            switch (redstoneSide) {
                case UP:
                    spawnParticlesAlongLine(level, random, blockPos, COLORS[i], direction, Direction.UP, -0.5f, 0.5f);
                    break;
                case SIDE: {
                    spawnParticlesAlongLine(level, random, blockPos, COLORS[i], Direction.DOWN, direction, 0.0f, 0.5f);
                    break;
                }
            }
            spawnParticlesAlongLine(level, random, blockPos, COLORS[i], Direction.DOWN, direction, 0.0f, 0.3f);
        }
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(EAST, blockState.getValue(WEST)).setValue(SOUTH, blockState.getValue(NORTH)).setValue(WEST, blockState.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return blockState.setValue(NORTH, blockState.getValue(EAST)).setValue(EAST, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(NORTH));
            case CLOCKWISE_90:
                return blockState.setValue(NORTH, blockState.getValue(WEST)).setValue(EAST, blockState.getValue(NORTH)).setValue(SOUTH, blockState.getValue(EAST)).setValue(WEST, blockState.getValue(SOUTH));
            default:
                return blockState;
        }
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(NORTH));
            case FRONT_BACK:
                return blockState.setValue(EAST, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(EAST));
            default:
                return super.mirror(blockState, mirror);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        //#if MC>=11701
        if (!player.getAbilities().mayBuild)
        //#else
        //$$ if (!player.abilities.mayBuild)
        //#endif
            return InteractionResult.PASS;
        if (isCross(blockState) || isDot(blockState)) {
            BlockState blockState2 = isCross(blockState) ? defaultBlockState() : crossState;
            blockState2 = blockState2.setValue(powerProperty, blockState.getValue(powerProperty));
            if ((blockState2 = getConnectionState(level, blockState2, blockPos)) != blockState) {
                level.setBlock(blockPos, blockState2, 3);
                updatesOnShapeChange(level, blockPos, blockState, blockState2);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private void updatesOnShapeChange(Level level, BlockPos blockPos, BlockState blockState, BlockState blockState2) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockPos2 = blockPos.relative(direction);
            if (blockState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() != blockState2.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() && level.getBlockState(blockPos2).isRedstoneConductor(level, blockPos2))
                level.updateNeighborsAtExceptFromFacing(blockPos2, blockState2.getBlock(), direction.getOpposite());
        }
    }

    public int getColor(BlockState blockState) {
        //#if MC>=11903
        Vector3f color = COLORS[blockState.getValue(powerProperty)];
        return Mth.color(color.x(), color.y(), color.z());
        //#else
        //$$ Vec3 color = COLORS[blockState.getValue(powerProperty)];
        //$$ return Mth.color((float) color.x(), (float) color.y(), (float) color.z());
        //#endif
    }
}
