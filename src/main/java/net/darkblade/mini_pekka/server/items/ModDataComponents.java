package net.darkblade.mini_pekka.server.items;

import com.mojang.serialization.Codec;
import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MiniPekkaMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> LAST_PANCAKE_SOUND_TIME =
            COMPONENTS.registerComponentType("last_pancake_sound_time", builder -> builder
                    .persistent(Codec.LONG)
                    .networkSynchronized(ByteBufCodecs.VAR_LONG));

    public static void register(IEventBus eventBus) {
        COMPONENTS.register(eventBus);
    }
}
