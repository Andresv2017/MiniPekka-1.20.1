package net.darkblade.mini_pekka.server.entity;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.entity.projectile.ButterflyEntity;
import net.darkblade.mini_pekka.server.entity.projectile.RageThrownPotion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MPekkaEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_REGISTER =
            DeferredRegister.create(Registries.ENTITY_TYPE, MiniPekkaMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<MiniPekka>> MPEKKA =
            ENTITY_REGISTER.register("m_pk",
                    () -> EntityType.Builder.of(MiniPekka::new, MobCategory.CREATURE)
                            .sized(0.9f, 1.25f)
                            .build(key("m_pk")));

    public static final DeferredHolder<EntityType<?>, EntityType<Pekka>> PEKKA =
            ENTITY_REGISTER.register("pekka",
                    () -> EntityType.Builder.of(Pekka::new, MobCategory.CREATURE)
                            .sized(1.5f, 2.7f)
                            .build(key("pekka")));

    public static final DeferredHolder<EntityType<?>, EntityType<RageThrownPotion>> RAGE_POTION_PROJECTILE =
            ENTITY_REGISTER.register("rage_potion_projectile",
                    () -> EntityType.Builder.<RageThrownPotion>of(RageThrownPotion::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(key("rage_potion_projectile")));

    public static final DeferredHolder<EntityType<?>, EntityType<ButterflyEntity>> BUTTERFLY =
            ENTITY_REGISTER.register("butterfly",
                    () -> EntityType.Builder.<ButterflyEntity>of(ButterflyEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(key("butterfly")));

    private static String key(String name) {
        return ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, name).toString();
    }

    public static void register(IEventBus eventBus) {
        ENTITY_REGISTER.register(eventBus);
    }
}
