package net.darkblade.mini_pekka.client.particles;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, MiniPekkaMod.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RAGE_AURA =
            PARTICLE_TYPES.register("rage_aura", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ARROW_UP =
            PARTICLE_TYPES.register("arrow_up", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPATULA =
            PARTICLE_TYPES.register("spatula", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ELIXIR_DROP =
            PARTICLE_TYPES.register("elixir_drop", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STAR_PARTICLE =
            PARTICLE_TYPES.register("star_particle", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STAR_PARTICLE_EVO =
            PARTICLE_TYPES.register("star_particle_evo", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SWEEP_EVO =
            PARTICLE_TYPES.register("sweep_evo", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SWEEP_HERO =
            PARTICLE_TYPES.register("sweep_hero", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STAR_PEKKA_ABILITY =
            PARTICLE_TYPES.register("star_pekka_ability", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
