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

public class PekkaRageEffectLayer extends GeoRenderLayer<Pekka> {

    private static final ResourceLocation OVERLAY_DEFAULT =
            new ResourceLocation("mpekka", "textures/entity/pekka_rage_overlay.png");
    private static final ResourceLocation OVERLAY_STAR =
            new ResourceLocation("mpekka", "textures/entity/pekka_star_overlay.png");
    private static final ResourceLocation OVERLAY_EVO =
            new ResourceLocation("mpekka", "textures/entity/pekka_evo_overlay.png");

    private static final float R = 0.7F;
    private static final float G = 0.3F;
    private static final float B = 1.0F;

    private static final float ALPHA_BASE = 0.40F;

    public PekkaRageEffectLayer(GeoRenderer<Pekka> entityRenderer) {
        super(entityRenderer);
    }

    private ResourceLocation getOverlayTexture(Pekka animatable) {
        if (animatable.isEvoMode()) return OVERLAY_EVO;
        if (animatable.isStarMode()) return OVERLAY_STAR;
        return OVERLAY_DEFAULT;
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

        if (!animatable.isRaging()) {
            return;
        }

        float time = (float) animatable.tickCount + partialTick;
        float pulseFactor = (Mth.sin(time * 0.2F) + 1.0F) * 0.5F;

        float currentAlpha = ALPHA_BASE * (0.6F + pulseFactor * 0.4F);

        ResourceLocation overlayTex = getOverlayTexture(animatable);
        RenderType rageRenderType = RenderType.entityTranslucentEmissive(overlayTex);

        this.getRenderer().reRender(
                bakedModel,
                poseStack,
                bufferSource,
                animatable,
                rageRenderType,
                bufferSource.getBuffer(rageRenderType),
                partialTick,
                packedLight,
                packedOverlay,
                R, G, B,
                currentAlpha
        );
    }
}
