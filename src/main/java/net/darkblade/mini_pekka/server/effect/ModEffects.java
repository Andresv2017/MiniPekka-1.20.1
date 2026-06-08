package net.darkblade.mini_pekka.server.effect;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, MiniPekkaMod.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> RAGE =
            EFFECTS.register("rage", RageEffect::new);

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
