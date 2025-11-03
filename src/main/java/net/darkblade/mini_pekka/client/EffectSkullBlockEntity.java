package net.darkblade.mini_pekka.client; // <-- usa tu paquete real (client / blockentity, etc.)

import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EffectSkullBlockEntity extends SkullBlockEntity {
    private int animationTickCount;
    private boolean isAnimating;

    public EffectSkullBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.EFFECT_SKULL.get();
    }

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

    public float getAnimation(float partial) {
        return this.isAnimating ? (float) this.animationTickCount + partial
                : (float) this.animationTickCount;
    }
}
