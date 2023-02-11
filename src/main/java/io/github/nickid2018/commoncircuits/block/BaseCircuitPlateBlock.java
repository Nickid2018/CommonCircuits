package io.github.nickid2018.commoncircuits.block;

import io.github.nickid2018.commoncircuits.logic.LogicProvider;
import io.github.nickid2018.commoncircuits.logic.LogicType;
import io.github.nickid2018.commoncircuits.util.BlockTicksUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

import java.util.List;

public abstract class BaseCircuitPlateBlock extends Block {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    private static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    private static final List<EnumProperty<Direction>> INPUT_PROPERTIES = List.of(
            EnumProperty.create("input1", Direction.class, HORIZONTAL_DIRECTIONS),
            EnumProperty.create("input2", Direction.class, HORIZONTAL_DIRECTIONS),
            EnumProperty.create("input3", Direction.class, HORIZONTAL_DIRECTIONS)
    );

    private static final List<EnumProperty<Direction>> OUTPUT_PROPERTIES = List.of(
            EnumProperty.create("output1", Direction.class, HORIZONTAL_DIRECTIONS),
            EnumProperty.create("output2", Direction.class, HORIZONTAL_DIRECTIONS),
            EnumProperty.create("output3", Direction.class, HORIZONTAL_DIRECTIONS)
    );

    public static final Direction[][][] DEFAULT_STATE = new Direction[][][] {
            {{Direction.SOUTH, Direction.NORTH}, {Direction.SOUTH, Direction.WEST, Direction.EAST}, {Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST}},
            {{Direction.WEST, Direction.EAST, Direction.NORTH}, {Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH}},
            {{Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.NORTH}}
    };

    public static BaseCircuitPlateBlock baseCircuitPlateBlock(LogicProvider provider) {

        return new BaseCircuitPlateBlock(Properties.of(Material.DECORATION).instabreak().sound(SoundType.WOOD), provider) {
            @Override
            @SuppressWarnings("unchecked")
            protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
                int inputCount = provider.getRequiredInputs();
                INPUT_PROPERTIES.subList(0, inputCount).toArray(inputProperties = new EnumProperty[inputCount]);
                for (int i = 0; i < inputCount; i++)
                    builder.add(INPUT_PROPERTIES.get(i));
                int outputCount = provider.getRequiredOutputs();
                OUTPUT_PROPERTIES.subList(0, outputCount).toArray(outputProperties = new EnumProperty[outputCount]);
                outputCachedProperties = new Property[outputCount];
                for (int i = 0; i < outputCount; i++) {
                    builder.add(OUTPUT_PROPERTIES.get(i));
                    builder.add(outputCachedProperties[i] = provider.getOutputs().get(i).createProperty("cache" + (i + 1)));
                }
                int stateCount = provider.getStores().size();
                storedProperties = new Property[stateCount];
                for (int i = 0; i < stateCount; i++)
                    builder.add(storedProperties[i] = provider.getStores().get(i).createProperty("extra_stored" + (i + 1)));
            }
        };
    }

    private final LogicProvider provider;
    protected EnumProperty<Direction>[] inputProperties;
    protected EnumProperty<Direction>[] outputProperties;
    @SuppressWarnings("rawtypes")
    protected Property[] storedProperties;
    @SuppressWarnings("rawtypes")
    protected Property[] outputCachedProperties;

    @SuppressWarnings({"unchecked"})
    public BaseCircuitPlateBlock(Properties properties, LogicProvider provider) {
        super(properties);
        this.provider = provider;
        BlockState defaultState = getStateDefinition().any();
        Direction[] defaultDirections = DEFAULT_STATE[provider.getRequiredInputs() - 1][provider.getRequiredOutputs() - 1];
        for (int i = 0; i < provider.getRequiredInputs(); i++)
            defaultState = defaultState.setValue(inputProperties[i], defaultDirections[i]);
        for (int i = 0; i < provider.getRequiredOutputs(); i++) {
            defaultState = defaultState.setValue(outputProperties[i], defaultDirections[i + provider.getRequiredInputs()]);
            defaultState = defaultState.setValue(outputCachedProperties[i], provider.getOutputs().get(i)
                    .toValue(provider.runLogic(i, input -> 0)));
        }
        for (int i = 0; i < provider.getStores().size(); i++)
            defaultState = defaultState.setValue(storedProperties[i], provider.getOutputs().get(i)
                    .toValue(provider.runLogic(i + provider.getRequiredOutputs(), input -> 0)));
        registerDefaultState(defaultState);
    }

    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return canSupportRigidBlock(levelReader, blockPos.below());
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (canSurvive(blockState, level, blockPos)) {
            if (!level.getBlockTicks().willTickThisTick(blockPos, this) && needRefresh(level, blockPos, blockState)) {
                TickPriority tickPriority = shouldPrioritize(level, blockPos, blockState) ? TickPriority.HIGH : TickPriority.NORMAL;
                BlockTicksUtil.scheduleTick(level, blockPos, this, 1, tickPriority);
            }
        } else {
            dropResources(blockState, level, blockPos);
            level.removeBlock(blockPos, false);
            for (Direction direction : Direction.values())
                level.updateNeighborsAt(blockPos.relative(direction), this);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        calculateOutputSignalAndRefresh(serverLevel, blockPos, blockState);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState state = defaultBlockState();
        Direction placeDir = blockPlaceContext.getHorizontalDirection();
        int clockwise = 0;
        while (placeDir != Direction.NORTH) {
            placeDir = placeDir.getCounterClockWise();
            clockwise++;
        }
        for (EnumProperty<Direction> inputProperty : inputProperties) {
            Direction sourceDir = state.getValue(inputProperty);
            for (int j = 0; j < clockwise; j++)
                sourceDir = sourceDir.getClockWise();
            state = state.setValue(inputProperty, sourceDir);
        }
        for (EnumProperty<Direction> outputProperty : outputProperties) {
            Direction sourceDir = state.getValue(outputProperty);
            for (int j = 0; j < clockwise; j++)
                sourceDir = sourceDir.getClockWise();
            state = state.setValue(outputProperty, sourceDir);
        }
        return state;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        updateNeighborsInOutputs(level, blockPos, blockState);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!bl && !blockState.is(blockState2.getBlock())) {
            super.onRemove(blockState, level, blockPos, blockState2, false);
            updateNeighborsInOutputs(level, blockPos, blockState);
        }
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getSignal(blockGetter, blockPos, direction);
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        Direction checkOutput = direction.getOpposite();
        for (int i = 0; i < provider.getRequiredOutputs(); i++)
            if (blockState.getValue(outputProperties[i]) == checkOutput)
                return provider.getOutputs().get(i).getValue(outputCachedProperties[i], blockState);
        return 0;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        BlockTicksUtil.scheduleTick(level, blockPos, this, 1);
    }

    private void updateNeighborsInOutputs(Level level, BlockPos blockPos, BlockState blockState) {
        for (EnumProperty<Direction> outputProperty : outputProperties) {
            Direction direction = blockState.getValue(outputProperty);
            BlockPos blockPos2 = blockPos.relative(direction);
            level.neighborChanged(blockPos2, this, blockPos);
            level.updateNeighborsAtExceptFromFacing(blockPos2, this, direction.getOpposite());
        }
    }

    private boolean inputThisWay(BlockState state, Direction direction) {
        for (int i = 0; i < provider.getRequiredInputs(); i++)
            if (state.getValue(inputProperties[i]) == direction)
                return true;
        return false;
    }

    private boolean shouldPrioritize(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        for (EnumProperty<Direction> outputProperty : outputProperties) {
            Direction direction = blockState.getValue(outputProperty);
            BlockState blockState2 = blockGetter.getBlockState(blockPos.relative(direction));
            if (blockState2.getBlock() instanceof BaseCircuitPlateBlock) {
                BaseCircuitPlateBlock plate = (BaseCircuitPlateBlock) blockState2.getBlock();
                if (plate.inputThisWay(blockState2, direction.getOpposite()))
                    return true;
            }
        }
        return false;
    }

    private boolean needRefresh(Level level, BlockPos blockPos, BlockState blockState) {
        int[] inputs = new int[provider.getRequiredInputs()];
        for (int i = 0; i < provider.getRequiredInputs(); i++) {
            Direction direction = blockState.getValue(inputProperties[i]);
            inputs[i] = level.getSignal(blockPos.relative(direction), direction);
        }
        for (int i = 0; i < provider.getRequiredOutputs(); i++) {
            int output = provider.runLogic(i, input -> inputs[input]);
            LogicType outputType = provider.getOutputs().get(i);
            if (outputType.getValue(outputCachedProperties[i], blockState) != output)
                return true;
        }
        for (int i = 0; i < provider.getStores().size(); i++) {
            int output = provider.runLogic(i + provider.getRequiredOutputs(), input -> inputs[input]);
            LogicType outputType = provider.getStores().get(i);
            if (outputType.getValue(storedProperties[i], blockState) != output)
                return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void calculateOutputSignalAndRefresh(Level level, BlockPos blockPos, BlockState blockState) {
        int[] inputs = new int[provider.getRequiredInputs()];
        for (int i = 0; i < provider.getRequiredInputs(); i++) {
            Direction direction = blockState.getValue(inputProperties[i]);
            inputs[i] = level.getSignal(blockPos.relative(direction), direction);
        }
        BlockState newState = blockState;
        for (int i = 0; i < provider.getRequiredOutputs(); i++) {
            int output = provider.runLogic(i, input -> inputs[input]);
            LogicType outputType = provider.getOutputs().get(i);
            newState = blockState.setValue(outputCachedProperties[i], outputType.toValue(output));
        }
        for (int i = 0; i < provider.getStores().size(); i++) {
            int output = provider.runLogic(i + provider.getRequiredOutputs(), input -> inputs[input]);
            LogicType outputType = provider.getStores().get(i);
            newState = blockState.setValue(storedProperties[i], outputType.toValue(output));
        }
        if (newState != blockState)
            level.setBlock(blockPos, newState, 2);
        updateNeighborsInOutputs(level, blockPos, blockState);
    }
}
