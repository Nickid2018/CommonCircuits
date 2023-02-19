package io.github.nickid2018.commoncircuits.block;

import io.github.nickid2018.commoncircuits.block.entity.AdvancedRedstoneWireBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdvancedRedstoneWireBlock extends BaseEntityBlock {

    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");

    public static final BooleanProperty[] DIRECTIONS = new BooleanProperty[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};

    private final VoxelShape[] shapes = new VoxelShape[64];

    private final int channels;

    public static final List<Block> CONNECT_WHITELIST = new ArrayList<>();

    public AdvancedRedstoneWireBlock(Properties properties, int size, int channels) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
                .setValue(DOWN, false)
                .setValue(UP, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(EAST, false)
        );
        VoxelShape core = Block.box(8 - size, 8 - size, 8 - size, 8 + size, 8 + size, 8 + size);
        VoxelShape down = Block.box(8 - size, 0, 8 - size, 8 + size, 8 - size, 8 + size);
        VoxelShape up = Block.box(8 - size, 8 + size, 8 - size, 8 + size, 16, 8 + size);
        VoxelShape north = Block.box(8 - size, 8 - size, 0, 8 + size, 8 + size, 8 - size);
        VoxelShape south = Block.box(8 - size, 8 - size, 8 + size, 8 + size, 8 + size, 16);
        VoxelShape west = Block.box(0, 8 - size, 8 - size, 8 - size, 8 + size, 8 + size);
        VoxelShape east = Block.box(8 + size, 8 - size, 8 - size, 16, 8 + size, 8 + size);
        for (int i = 0; i < 64; i++) {
            VoxelShape shape = core;
            if ((i & 1) != 0)
                shape = Shapes.or(shape, down);
            if ((i & 2) != 0)
                shape = Shapes.or(shape, up);
            if ((i & 4) != 0)
                shape = Shapes.or(shape, north);
            if ((i & 8) != 0)
                shape = Shapes.or(shape, south);
            if ((i & 16) != 0)
                shape = Shapes.or(shape, west);
            if ((i & 32) != 0)
                shape = Shapes.or(shape, east);
            shapes[i] = shape;
        }
        this.channels = channels;
    }

    public int getChannels() {
        return channels;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        int i = 0;
        for (int j = 0; j < 6; j++) {
            if (blockState.getValue(DIRECTIONS[j]))
                i |= 1 << j;
        }
        return shapes[i];
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DOWN, UP, NORTH, SOUTH, WEST, EAST);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState state = defaultBlockState();
        for (int i = 0; i < DIRECTIONS.length; i++) {
            BooleanProperty property = DIRECTIONS[i];
            Direction direction = Direction.values()[i];
            if (shouldConnectTo(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos(), direction))
                state = state.setValue(property, true);
        }
        return state;
    }

    @Nullable
    @Override
    //#if MC>=11701
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AdvancedRedstoneWireBlockEntity(blockPos, blockState);
    }
    //#else
    //$$ public BlockEntity newBlockEntity(BlockGetter blockGetter) {
    //$$     return new AdvancedRedstoneWireBlockEntity(channels);
    //$$ }
    //#endif

    private boolean shouldConnectTo(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos.relative(direction));
        if (CONNECT_WHITELIST.contains(state.getBlock()))
            return true;
        if (state.getBlock() instanceof AdvancedRedstoneWireBlock) {
            BlockEntity entity = level.getBlockEntity(pos.relative(direction));
            BlockEntity thisEntity = level.getBlockEntity(pos);
            if (entity instanceof AdvancedRedstoneWireBlockEntity && thisEntity instanceof AdvancedRedstoneWireBlockEntity) {
                AdvancedRedstoneWireBlockEntity wire = (AdvancedRedstoneWireBlockEntity) entity;
                AdvancedRedstoneWireBlockEntity thisWire = (AdvancedRedstoneWireBlockEntity) thisEntity;
                return wire.getChannelCount() == thisWire.getChannelCount();
            }
        }
        if (state.is(CommonCircuitsBlocks.WIRE_CONNECTOR))
            return true;
        if (state.is(Blocks.REDSTONE_WIRE))
            return false;
        return state.isSignalSource() || state.getBlock() instanceof AdvancedCircuitBlock;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        Direction from = null;
        for (int i = 0; i < DIRECTIONS.length; i++) {
            Direction direction = Direction.values()[i];
            if (blockPos2.equals(blockPos.relative(direction))) {
                from = direction;
                break;
            }
        }
        if (from == null)
            return;
        boolean shouldBe = shouldConnectTo(level, blockPos, from);
        boolean now = blockState.getValue(DIRECTIONS[from.ordinal()]);
        if (shouldBe != now)
            level.setBlockAndUpdate(blockPos, blockState.setValue(DIRECTIONS[from.ordinal()], shouldBe));
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (entity instanceof AdvancedRedstoneWireBlockEntity)
            ((AdvancedRedstoneWireBlockEntity) entity).updateAllChannels();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        boolean needUpdate = true;
        for (int i = 0; i < DIRECTIONS.length; i++) {
            Direction direction = Direction.values()[i];
            if (level.getBlockState(blockPos.relative(direction)).getBlock() instanceof AdvancedRedstoneWireBlock)
                needUpdate = false;
        }
        if (needUpdate) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof AdvancedRedstoneWireBlockEntity)
                ((AdvancedRedstoneWireBlockEntity) entity).updateAllChannels();
        }
    }
}
