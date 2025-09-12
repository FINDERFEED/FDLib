package com.finderfeed.fdlib.systems.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class MultiblockBlock extends Block {

    public static final BooleanProperty MAIN_BLOCK = BooleanProperty.create("main_block");

    protected boolean cacheShape = true;

    private MultiblockShape cachedShape = null;

    private Supplier<MultiblockShape> firstTimeShapeGetter;

    public MultiblockBlock(Properties properties, Supplier<MultiblockShape> multiblockShape) {
        super(properties);
        this.firstTimeShapeGetter = multiblockShape;
        this.registerDefaultState(this.getStateDefinition().any().setValue(MAIN_BLOCK, true));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {

        BlockPos mainblock = this.findMainBlock(level, pos);

        if (mainblock != null){

            BlockState mainBlock = level.getBlockState(mainblock);
            Direction direction = mainBlock.getValue(BlockStateProperties.HORIZONTAL_FACING);

            for (var pair : this.getMultiblockShape()){
                BlockPos offset = pair.second;
                offset = this.rotateBaseOffset(offset, direction);
                BlockPos setpos = mainblock.offset(offset);
                level.setBlock(setpos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            }

        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {

        Level level = ctx.getLevel();

        BlockPos setPos = ctx.getClickedPos();

        Direction horizontalDirection = ctx.getHorizontalDirection();

        BlockState mainstate = this.getMultiblockShape().getMainState().setValue(BlockStateProperties.HORIZONTAL_FACING, horizontalDirection);

        for (var pair : this.getMultiblockShape()){
            BlockPos offset = this.rotateBaseOffset(pair.second, horizontalDirection);
            BlockPos pos = setPos.offset(offset);
            BlockState stateAtPos = level.getBlockState(pos);
            if (!stateAtPos.canBeReplaced()){
                return null;
            }
        }

        return mainstate;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean idk) {
        super.onPlace(state, level, pos, state2, idk);
        if (this.isMainBlock(state)) {
            Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            for (var pair : this.getMultiblockShape()){
                BlockPos offset = pair.second;
                if (offset.equals(BlockPos.ZERO)) continue;
                offset = this.rotateBaseOffset(offset, direction);
                BlockState stateToPlace = pair.first.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
                BlockPos setPos = pos.offset(offset);
                level.setBlock(setPos, stateToPlace, Block.UPDATE_CLIENTS);
            }
        }
    }

    public BlockPos findMainBlock(Level level, BlockPos currentPos){
        BlockState current = level.getBlockState(currentPos);
        if (current.is(this) && this.isMainBlock(current)){
            return currentPos;
        }
        int maxDist = this.getMultiblockShape().getMaxDistanceFromMainBlock();
        for (int x = -maxDist; x <= maxDist; x++){
            for (int y = -maxDist; y <= maxDist; y++){
                for (int z = -maxDist; z <= maxDist; z++){
                    BlockPos pos = currentPos.offset(x,y,z);
                    BlockState state = level.getBlockState(pos);
                    if (state.is(this)){
                        if (this.isMainBlock(state)){
                            if (this.getMultiblockShape().isAPartOfShape(current, pos, currentPos)){
                                return pos;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public BlockPos rotateBaseOffset(BlockPos pos, Direction direction){
        int yRot = -(int) direction.toYRot();
        Vec3 v = new Vec3(pos.getX(), 0, pos.getZ()).yRot((float)Math.toRadians(yRot));

        BlockPos offsetPos = new BlockPos(
                (int) Math.floor(v.x + 0.5f),
                pos.getY(),
                (int) Math.floor(v.z + 0.5f)
        );
        return offsetPos;
    }

    public BlockPos rotateOffsetBack(BlockPos pos, Direction direction){
        int yRot = (int)direction.toYRot();
        Vec3 v = new Vec3(pos.getX(), 0, pos.getZ()).yRot((float)Math.toRadians(yRot));
        BlockPos offsetPos = new BlockPos(
                (int) Math.floor(v.x + 0.5f),
                pos.getY(),
                (int) Math.floor(v.z + 0.5f)
        );
        return offsetPos;
    }



    public boolean isMainBlock(BlockState blockState){
        return blockState.getValue(MAIN_BLOCK);
    }

    public MultiblockShape getMultiblockShape() {

        if (!cacheShape){
            return firstTimeShapeGetter.get();
        }

        if (cachedShape == null){
            cachedShape = firstTimeShapeGetter.get();
        }
        return cachedShape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MAIN_BLOCK);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

}
