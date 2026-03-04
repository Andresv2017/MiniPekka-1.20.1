package net.darkblade.mini_pekka.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.darkblade.mini_pekka.server.entity.Pekka;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class PekkaEvoAbilityLayer extends GeoRenderLayer<Pekka> {

    private static final ResourceLocation MASK_TEX =
            ResourceLocation.fromNamespaceAndPath("mpekka", "textures/entity/pekka_evo_ability.png");

    private static final RenderType GLOW_TYPE = RenderHelper.entityOverlayEmissive(MASK_TEX);

    public PekkaEvoAbilityLayer(GeoRenderer<Pekka> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack,
                       Pekka animatable,
                       BakedGeoModel bakedModel,
                       RenderType renderType,
                       MultiBufferSource bufferSource,
                       VertexConsumer buffer,
                       float partialTick,
                       int packedLight,
                       int packedOverlay) {

        int flashTicks = animatable.getEvoAbilityFlashTicks();
        if (flashTicks <= 0) return;

        float remaining = flashTicks - partialTick;

        float raw;
        if (remaining > 30.0F) {
            raw = (40.0F - remaining) / 10.0F;
        } else if (remaining > 10.0F) {
            raw = 1.0F;
        } else {
            raw = remaining / 10.0F;
        }

        float t = Mth.clamp(raw, 0.0F, 1.0F);
        float alpha = t * t * t * (t * (t * 6.0F - 15.0F) + 10.0F);

        this.getRenderer().reRender(
                bakedModel,
                poseStack,
                bufferSource,
                animatable,
                GLOW_TYPE,
                bufferSource.getBuffer(GLOW_TYPE),
                partialTick,
                packedLight,
                packedOverlay,
                0.7F, 0.3F, 1.0F,
                alpha
        );
    }
}
