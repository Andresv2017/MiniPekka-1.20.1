package net.darkblade.mini_pekka.client;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MiniPekkaMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModSkullBlockEntity>> EFFECT_SKULL =
            BLOCK_ENTITY_TYPES.register("effect_skull",
                    () -> BlockEntityType.Builder.of(
                                    ModSkullBlockEntity::new,
                                    ModBlocks.MINI_PK_HEAD.get(),
                                    ModBlocks.MINI_PK_WALL_HEAD.get(),
                                    ModBlocks.PEKKA_HEAD.get(),
                                    ModBlocks.PEKKA_WALL_HEAD.get()
                            )
                            .build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
