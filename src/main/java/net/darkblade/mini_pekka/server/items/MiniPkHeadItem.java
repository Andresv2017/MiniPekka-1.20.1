package net.darkblade.mini_pekka.server.items;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * Head item del Mini Pekka (suelo/pared) sin sonido por ahora.
 * OJO: getSound() devuelve null; cualquier uso debe chequear null antes de reproducir.
 */
public class MiniPkHeadItem extends EffectSkullItem {

    public MiniPkHeadItem(Block skull, Block wallSkull, Item.Properties properties, Direction direction) {
        super(skull, wallSkull, properties, direction);
    }

    @Override
    public SoundEvent getSound() {
        // Sin sonido por ahora. Si en tu renderer/uso se llama playSound(getSound()),
        // asegúrate de hacer null-check antes de reproducir.
        return null;
    }
}
