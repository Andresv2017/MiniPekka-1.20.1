package net.darkblade.mini_pekka.server.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public abstract class EffectSkullItem extends StandingAndWallBlockItem {
    public EffectSkullItem(Block skull, Block wallSkull, Properties properties, Direction direction) {
        super(skull, wallSkull, properties, direction);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return EffectSkullItemRenderer.getInstance();
            }
        });
    }

    public abstract SoundEvent getSound();
}
