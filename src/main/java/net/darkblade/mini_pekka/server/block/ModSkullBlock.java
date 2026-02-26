package net.darkblade.mini_pekka.server.block;

import net.darkblade.mini_pekka.client.ModSkullBlockEntity;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModSkullBlock extends SkullBlock {

    protected static final VoxelShape MINI_PEKKA_SHAPE =
            Block.box(0.0D, 0.0D, 3.5D, 16.0D, 10.0D, 12.5D);

    protected static final VoxelShape PEKKA_SHAPE =
            Block.box(0.0D, 0.0D, 3.5D, 16.0D, 12.0D, 12.5D);

    @SuppressWarnings("unused")
    public enum Types implements SkullBlock.Type {
        MINI_PEKKA,
        PEKKA
    }

    public ModSkullBlock(Type type, Properties props) {
        super(type, props);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (this.getType() == Types.PEKKA) {
            return PEKKA_SHAPE;
        }
        return MINI_PEKKA_SHAPE;
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
            if (state.is(ModBlocks.MINI_PK_HEAD.get()) || state.is(ModBlocks.PEKKA_HEAD.get())) {
                return createTickerHelper(type, ModBlockEntities.EFFECT_SKULL.get(),
                        ModSkullBlockEntity::animation);
            }
        }
        return null;
    }
}
