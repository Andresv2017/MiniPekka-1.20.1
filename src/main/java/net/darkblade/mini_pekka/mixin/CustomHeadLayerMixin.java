package net.darkblade.mini_pekka.mixin;

import net.darkblade.mini_pekka.server.block.ModSkullBlock;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CustomHeadLayer.class)
public class CustomHeadLayerMixin {

    @Redirect(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;")
    )
    private Block mpekka$bypassSkullRendererForCustomHeads(BlockItem instance) {
        Block block = instance.getBlock();

        if (block instanceof ModSkullBlock) {
            return Blocks.DIRT;
        }

        return block;
    }
}