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

}
