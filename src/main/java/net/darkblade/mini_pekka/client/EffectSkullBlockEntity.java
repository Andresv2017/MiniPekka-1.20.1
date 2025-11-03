package net.darkblade.mini_pekka.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EffectSkullBlockEntity extends SkullBlockEntity {
    private int animationTickCount;
    private boolean isAnimating;

    public EffectSkullBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.EFFECT_SKULL.get();
    }

    /**
     * Tick de animación exclusivo para la cabeza del Mini Pekka (de pie y de pared).
     * Activa animación si hay redstone o si el bloque es MiniPekka Head.
     */

    /*
    public static void animation(Level level, BlockPos pos, BlockState state, EffectSkullBlockEntity be) {
        boolean isMiniPekkaHead =
                state.is(ModBlocks.MINI_PK_HEAD.get()) ||
                        state.is(ModBlocks.MINI_PK_WALL_HEAD.get());

        if (isMiniPekkaHead || level.hasNeighborSignal(pos)) {
            be.isAnimating = true;
            be.animationTickCount++;
        } else {
            be.isAnimating = false;
        }
    }

    public float getAnimation(float partialTicks) {
        return this.isAnimating ? (float) this.animationTickCount + partialTicks
                : (float) this.animationTickCount;
    }

     */
}
