package net.darkblade.mini_pekka.client;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MiniPekkaMod.MODID);

    public static final RegistryObject<BlockEntityType<ModSkullBlockEntity>> EFFECT_SKULL =
            BLOCK_ENTITY_TYPES.register("effect_skull",
                    () -> BlockEntityType.Builder.of(
                                    ModSkullBlockEntity::new,
                                    ModBlocks.MINI_PK_HEAD.get(),
                                    ModBlocks.MINI_PK_WALL_HEAD.get()
                            )
                            .build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
