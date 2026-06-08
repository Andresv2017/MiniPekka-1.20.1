package net.darkblade.mini_pekka.server.block;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, MiniPekkaMod.MODID);

    public static final DeferredHolder<Block, Block> MINI_PK_HEAD = BLOCKS.register("mini_pk_head",
            () -> new ModSkullBlock(
                    ModSkullBlock.Types.MINI_PEKKA,
                    BlockBehaviour.Properties.of()
                            .strength(1.0F)
                            .pushReaction(PushReaction.DESTROY)
            ));

    public static final DeferredHolder<Block, Block> MINI_PK_WALL_HEAD = BLOCKS.register("mini_pk_wall_head",
            () -> new ModWallSkullBlock(
                    ModSkullBlock.Types.MINI_PEKKA,
                    BlockBehaviour.Properties.of()
                            .strength(1.0F)
                            .lootFrom(MINI_PK_HEAD)
                            .pushReaction(PushReaction.DESTROY)
            ));

    public static final DeferredHolder<Block, Block> PEKKA_HEAD = BLOCKS.register("pekka_head",
            () -> new ModSkullBlock(
                    ModSkullBlock.Types.PEKKA,
                    BlockBehaviour.Properties.of()
                            .strength(1.0F)
                            .pushReaction(PushReaction.DESTROY)
            ));

    public static final DeferredHolder<Block, Block> PEKKA_WALL_HEAD = BLOCKS.register("pekka_wall_head",
            () -> new ModWallSkullBlock(
                    ModSkullBlock.Types.PEKKA,
                    BlockBehaviour.Properties.of()
                            .strength(1.0F)
                            .lootFrom(PEKKA_HEAD)
                            .pushReaction(PushReaction.DESTROY)
            ));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
