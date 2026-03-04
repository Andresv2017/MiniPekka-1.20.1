package net.darkblade.mini_pekka.sounds;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MiniPekkaMod.MODID);

    public static final RegistryObject<SoundEvent> ANA =
            SOUNDS.register("entity.mini_pekka.ana",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka.ana")));

    public static final RegistryObject<SoundEvent> PANCAKES =
            SOUNDS.register("entity.mini_pekka.pancakes",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka.pancakes")));

    public static final RegistryObject<SoundEvent> STEPS =
            SOUNDS.register("entity.mini_pekka.step",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka.step")));

    public static final RegistryObject<SoundEvent> DEATH =
            SOUNDS.register("entity.mini_pekka.death",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka.death")));

    public static final RegistryObject<SoundEvent> RAGE_THROW =
            SOUNDS.register("rage.rage_throw",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "rage.rage_throw")));

    public static final RegistryObject<SoundEvent> RAGE_BREAK =
            SOUNDS.register("rage.rage_break",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "rage.rage_break")));

    public static final RegistryObject<SoundEvent> PEKKA_ATTACK =
            SOUNDS.register("entity.pekka.attack",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka.attack")));

    public static final RegistryObject<SoundEvent> PEKKA_ATTACK2 =
            SOUNDS.register("entity.pekka.attack2",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka.attack2")));

    public static final RegistryObject<SoundEvent> PEKKA_STEP =
            SOUNDS.register("entity.pekka.step",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka.step")));

    public static final RegistryObject<SoundEvent> PEKKA_DEATH =
            SOUNDS.register("entity.pekka.death",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka.death")));

    public static final RegistryObject<SoundEvent> PEKKA_SPAWN =
            SOUNDS.register("entity.pekka.spawn",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka.spawn")));

    public static final RegistryObject<SoundEvent> PEKKA_AMBIENT =
            SOUNDS.register("entity.pekka.ambient",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka.ambient")));

    public static final RegistryObject<SoundEvent> PEKKA_EVO_ATTACK =
            SOUNDS.register("entity.pekka_evo.attack",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka_evo.attack")));

    public static final RegistryObject<SoundEvent> PEKKA_EVO_ATTACK2 =
            SOUNDS.register("entity.pekka_evo.attack2",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka_evo.attack2")));

    public static final RegistryObject<SoundEvent> PEKKA_EVO_SPAWN =
            SOUNDS.register("entity.pekka_evo.spawn",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka_evo.spawn")));

    public static final RegistryObject<SoundEvent> PEKKA_EVO_STEP =
            SOUNDS.register("entity.pekka_evo.step",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka_evo.step")));

    public static final RegistryObject<SoundEvent> PEKKA_EVO_AMBIENT =
            SOUNDS.register("entity.pekka_evo.ambient",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.pekka_evo.ambient")));

    public static final RegistryObject<SoundEvent> HERO_ABILITY =
            SOUNDS.register("entity.mini_pekka_hero.ability",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka_hero.ability")));

    public static final RegistryObject<SoundEvent> HERO_AMBIENT =
            SOUNDS.register("entity.mini_pekka_hero.ambient",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka_hero.ambient")));

    public static final RegistryObject<SoundEvent> HERO_DEATH =
            SOUNDS.register("entity.mini_pekka_hero.death",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka_hero.death")));

    public static final RegistryObject<SoundEvent> HERO_SWORD =
            SOUNDS.register("entity.mini_pekka_hero.sword",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka_hero.sword")));

    public static final RegistryObject<SoundEvent> HERO_SPATULA =
            SOUNDS.register("entity.mini_pekka_hero.spatula",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka_hero.spatula")));

    public static final RegistryObject<SoundEvent> HERO_SPAWN =
            SOUNDS.register("entity.mini_pekka_hero.spawn",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MiniPekkaMod.MODID, "entity.mini_pekka_hero.spawn")));


}
