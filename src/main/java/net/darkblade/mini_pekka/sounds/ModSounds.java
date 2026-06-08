package net.darkblade.mini_pekka.sounds;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, MiniPekkaMod.MODID);

    private static DeferredHolder<SoundEvent, SoundEvent> reg(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, name)));
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> ANA = reg("entity.mini_pekka.ana");
    public static final DeferredHolder<SoundEvent, SoundEvent> PANCAKES = reg("entity.mini_pekka.pancakes");
    public static final DeferredHolder<SoundEvent, SoundEvent> STEPS = reg("entity.mini_pekka.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEATH = reg("entity.mini_pekka.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> RAGE_THROW = reg("rage.rage_throw");
    public static final DeferredHolder<SoundEvent, SoundEvent> RAGE_BREAK = reg("rage.rage_break");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_ATTACK = reg("entity.pekka.attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_ATTACK2 = reg("entity.pekka.attack2");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_STEP = reg("entity.pekka.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_DEATH = reg("entity.pekka.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_SPAWN = reg("entity.pekka.spawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_HURT = reg("entity.pekka.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_AMBIENT = reg("entity.pekka.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_EVO_ATTACK = reg("entity.pekka_evo.attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_EVO_ATTACK2 = reg("entity.pekka_evo.attack2");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_EVO_SPAWN = reg("entity.pekka_evo.spawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_EVO_STEP = reg("entity.pekka_evo.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> PEKKA_EVO_AMBIENT = reg("entity.pekka_evo.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> HERO_ABILITY = reg("entity.mini_pekka_hero.ability");
    public static final DeferredHolder<SoundEvent, SoundEvent> HERO_AMBIENT = reg("entity.mini_pekka_hero.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> HERO_DEATH = reg("entity.mini_pekka_hero.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> HERO_SWORD = reg("entity.mini_pekka_hero.sword");
    public static final DeferredHolder<SoundEvent, SoundEvent> HERO_SPATULA = reg("entity.mini_pekka_hero.spatula");
    public static final DeferredHolder<SoundEvent, SoundEvent> HERO_SPAWN = reg("entity.mini_pekka_hero.spawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> STAR_LEVEL_UP = reg("star_level_up");
    public static final DeferredHolder<SoundEvent, SoundEvent> HERO_BOX_OPEN = reg("hero_box_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> HERO_HURT = reg("entity.mini_pekka_hero.hurt");
}
