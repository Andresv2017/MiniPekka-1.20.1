package net.darkblade.mini_pekka.server.entity;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MPekkaEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_REGISTER =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MiniPekkaMod.MODID);

    public static final RegistryObject<EntityType<MiniPekka>> MPEKKA =
            ENTITY_REGISTER.register("m_pk",
                    () -> EntityType.Builder.of(MiniPekka::new, MobCategory.CREATURE)
                            .sized(1.25f, 1.25f)
                            .build(new ResourceLocation(MiniPekkaMod.MODID, "m_pk").toString()));


    public static void register(IEventBus eventBus){
        ENTITY_REGISTER.register(eventBus);
    }
}
