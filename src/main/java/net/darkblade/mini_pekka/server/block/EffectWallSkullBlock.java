package net.darkblade.mini_pekka.server.block;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class EffectWallSkullBlock extends WallSkullBlock {


    private static final Map<Direction, VoxelShape> MINI_PEKKA_WALL_SHAPES = Maps.newEnumMap(Map.of(
            Direction.NORTH, Block.box(4.0D, 4.0D, 8.0D, 12.0D, 11.0D, 16.0D), // pegado a la cara norte (z max)
            Direction.SOUTH, Block.box(4.0D, 4.0D, 0.0D, 12.0D, 11.0D, 8.0D),  // pegado a la cara sur (z min)
            Direction.EAST,  Block.box(0.0D, 4.0D, 4.0D, 8.0D, 11.0D, 12.0D),  // pegado a la cara este (x min)
            Direction.WEST,  Block.box(8.0D, 4.0D, 4.0D, 16.0D, 11.0D, 12.0D)  // pegado a la cara oeste (x max)
    ));

    public EffectWallSkullBlock(SkullBlock.Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (this.getType() == EffectSkullBlock.Types.MINI_PEKKA) {
            Direction facing = state.getValue(FACING);
            VoxelShape shape = MINI_PEKKA_WALL_SHAPES.get(facing);
            if (shape != null) return shape;
        }
        return super.getShape(state, level, pos, ctx);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SkullBlockEntity(pos, state);
    }
}
