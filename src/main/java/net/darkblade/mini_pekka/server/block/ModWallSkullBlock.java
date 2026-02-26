package net.darkblade.mini_pekka.server.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.darkblade.mini_pekka.client.ModSkullBlockEntity;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class ModWallSkullBlock extends WallSkullBlock {

    private static final Map<Direction, VoxelShape> MINI_PEKKA_WALL_AABBS = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.box(0.0D, 3.0D, 7.0D, 16.0D, 13.0D, 16.0D),
            Direction.SOUTH, Block.box(0.0D, 3.0D, 0.0D, 16.0D, 13.0D, 9.0D),
            Direction.EAST,  Block.box(0.0D, 3.0D, 0.0D, 9.0D, 13.0D, 16.0D),
            Direction.WEST,  Block.box(7.0D, 3.0D, 0.0D, 16.0D, 13.0D, 16.0D)
    ));

    private static final Map<Direction, VoxelShape> PEKKA_WALL_AABBS = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.box(0.0D, 2.0D, 7.0D, 16.0D, 14.0D, 16.0D),
            Direction.SOUTH, Block.box(0.0D, 2.0D, 0.0D, 16.0D, 14.0D, 9.0D),
            Direction.EAST,  Block.box(0.0D, 2.0D, 0.0D, 9.0D, 14.0D, 16.0D),
            Direction.WEST,  Block.box(7.0D, 2.0D, 0.0D, 16.0D, 14.0D, 16.0D)
    ));

    public ModWallSkullBlock(SkullBlock.Type type, Properties props) {
        super(type, props);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        Direction facing = state.getValue(FACING);
        if (this.getType() == ModSkullBlock.Types.PEKKA) {
            return PEKKA_WALL_AABBS.get(facing);
        }
        return MINI_PEKKA_WALL_AABBS.get(facing);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModSkullBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type
    ) {
        if (level.isClientSide) {
            if (state.is(ModBlocks.MINI_PK_WALL_HEAD.get()) || state.is(ModBlocks.PEKKA_WALL_HEAD.get())) {
                return createTickerHelper(type, ModBlockEntities.EFFECT_SKULL.get(),
                        ModSkullBlockEntity::animation);
            }
        }
        return null;
    }
}
