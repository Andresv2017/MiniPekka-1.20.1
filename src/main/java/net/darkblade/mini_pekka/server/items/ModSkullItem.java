package net.darkblade.mini_pekka.server.items;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;

public abstract class ModSkullItem extends StandingAndWallBlockItem {
    public ModSkullItem(Block skull, Block wallSkull, Properties properties, Direction direction) {
        super(skull, wallSkull, properties, direction);
    }

    public abstract SoundEvent getSound();
}
