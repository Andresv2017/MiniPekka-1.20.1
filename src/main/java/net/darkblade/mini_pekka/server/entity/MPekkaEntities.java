package net.darkblade.mini_pekka.server.entity;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.entity.projectile.RageThrownPotion;
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

    public static final RegistryObject<EntityType<RageThrownPotion>> RAGE_POTION_PROJECTILE =
            ENTITY_REGISTER.register("rage_potion_projectile",
                    () -> EntityType.Builder.<RageThrownPotion>of(RageThrownPotion::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(new ResourceLocation(MiniPekkaMod.MODID, "rage_potion_projectile").toString()));


    public static void register(IEventBus eventBus){
        ENTITY_REGISTER.register(eventBus);
    }
}
