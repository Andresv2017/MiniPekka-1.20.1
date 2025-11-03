package net.darkblade.mini_pekka.server.block;

import net.darkblade.mini_pekka.client.EffectSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EffectSkullBlock extends SkullBlock {

    protected static final VoxelShape MINI_PEKKA_SHAPE =
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 11.0D, 12.0D);

    public EffectSkullBlock(Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        // --- pegamos aquí tus if(...) existentes ---

        if (Types.MINI_PEKKA.equals(this.getType())) {
            return MINI_PEKKA_SHAPE;
        }

        // Fallback por defecto
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EffectSkullBlockEntity(pos, state);
    }

    @SuppressWarnings("unused")
    public enum Types implements SkullBlock.Type {
        MINI_PEKKA
    }
}
