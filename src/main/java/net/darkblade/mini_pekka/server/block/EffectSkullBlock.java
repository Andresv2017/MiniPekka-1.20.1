package net.darkblade.mini_pekka.server.block;

import net.darkblade.mini_pekka.client.EffectSkullBlockEntity;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EffectSkullBlock extends SkullBlock {

    @SuppressWarnings("unused")
    public enum Types implements SkullBlock.Type {
        MINI_PEKKA
    }

    public EffectSkullBlock(Type type, Properties props) {
        super(type, props);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE; // si la quieres más pequeña, cambia aquí.
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EffectSkullBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type
    ) {
        if (level.isClientSide) {
            if (state.is(ModBlocks.MINI_PK_HEAD.get())) {
                return createTickerHelper(type, ModBlockEntities.EFFECT_SKULL.get(),
                        EffectSkullBlockEntity::animation);
            }
        }
        return null;
    }
}
