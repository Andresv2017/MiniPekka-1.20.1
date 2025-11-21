package net.darkblade.mini_pekka.server.block;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, MiniPekkaMod.MODID);

    public static final RegistryObject<Block> MINI_PK_HEAD = BLOCKS.register("mini_pk_head",
            () -> new ModSkullBlock(
                    ModSkullBlock.Types.MINI_PEKKA,
                    BlockBehaviour.Properties.of()
                            .strength(1.0F)
                            .pushReaction(PushReaction.DESTROY)
            ));

    public static final RegistryObject<Block> MINI_PK_WALL_HEAD = BLOCKS.register("mini_pk_wall_head",
            () -> new ModWallSkullBlock(
                    ModSkullBlock.Types.MINI_PEKKA,
                    BlockBehaviour.Properties.of()
                            .strength(1.0F)
                            .lootFrom(MINI_PK_HEAD)
                            .pushReaction(PushReaction.DESTROY)
            ));

}
