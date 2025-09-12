package com.finderfeed.fdlib.systems.multiblock;

import com.finderfeed.fdlib.data_structures.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class MultiblockShape implements Iterable<Pair<BlockState,BlockPos>> {

    private List<BlockPos> partOffsets = new ArrayList<>(List.of(BlockPos.ZERO));
    private List<BlockState> states = new ArrayList<>();

    private int maxDistanceFromMainBlock;

    private MultiblockShape(){}

    public static Builder builder(BlockState mainstate){
        return new Builder(mainstate);
    }

    public BlockState getMainState(){
        return states.get(0);
    }

    public boolean isAPartOfShape(BlockState stateBeingChecked, BlockPos mainBlockPos, BlockPos checkPos){

        BlockPos offset = checkPos.subtract(mainBlockPos);

        Direction direction = stateBeingChecked.getValue(BlockStateProperties.HORIZONTAL_FACING);

        MultiblockBlock block = (MultiblockBlock) stateBeingChecked.getBlock();

        offset = block.rotateOffsetBack(offset, direction);

        return this.isAPartOfShape(offset);
    }

    public boolean isAPartOfShape(BlockPos offset){
        return partOffsets.contains(offset);
    }

    public int getMaxDistanceFromMainBlock() {
        return maxDistanceFromMainBlock;
    }

    @NotNull
    @Override
    public Iterator<Pair<BlockState,BlockPos>> iterator() {
        return new ShapeIterator(this);
    }

    public static class Builder{

        private MultiblockShape shape = new MultiblockShape();

        public Builder(BlockState mainState){
            this.shape.states.add(mainState);
        }

        public Builder addMultiblockPartOffset(BlockPos offset, BlockState state){

            if (!state.is(shape.getMainState().getBlock())){
                throw new RuntimeException("Cannot create a shape out of different blocks! Blowing up...");
            }

            shape.partOffsets.add(offset);
            shape.states.add(state.setValue(MultiblockBlock.MAIN_BLOCK,false));
            shape.maxDistanceFromMainBlock = Math.max(shape.maxDistanceFromMainBlock, Math.max(Math.abs(offset.getX()),Math.max(Math.abs(offset.getY()),Math.abs(offset.getZ()))));
            return this;
        }

        public MultiblockShape build(){
            return shape;
        }

    }

    public static class ShapeIterator implements Iterator<Pair<BlockState, BlockPos>>{

        private MultiblockShape shape;

        private int currentIndex = 0;

        public ShapeIterator(MultiblockShape multiblockShape){
            this.shape = multiblockShape;

        }

        @Override
        public boolean hasNext() {
            return currentIndex < shape.partOffsets.size();
        }

        @Override
        public Pair<BlockState, BlockPos> next() {

            BlockState blockState = shape.states.get(currentIndex);

            BlockPos offset = shape.partOffsets.get(currentIndex);

            var pair = new Pair<>(
                    blockState,
                    offset
            );

            currentIndex++;

            return pair;
        }
    }

}
