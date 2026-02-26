package net.darkblade.mini_pekka.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkblade.mini_pekka.server.items.ModSkullItem;
import net.darkblade.mini_pekka.server.items.ModSkullItemRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mpekka$renderSkullEverywhereExceptHead(ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {

        if (stack.getItem() instanceof ModSkullItem) {

            if (displayContext != ItemDisplayContext.HEAD) {

                poseStack.pushPose();

                model.getTransforms().getTransform(displayContext).apply(leftHand, poseStack);

                poseStack.translate(-0.5F, -0.5F, -0.5F);

                ModSkullItemRenderer.getInstance().renderByItem(stack, displayContext, poseStack, buffer, combinedLight, combinedOverlay);
                poseStack.popPose();
                ci.cancel();
            }
        }
    }
}