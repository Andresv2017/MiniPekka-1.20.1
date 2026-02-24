package net.darkblade.mini_pekka.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class MiniPekkaRageEffectLayer extends GeoRenderLayer<MiniPekka> {

    private static final ResourceLocation OVERLAY_DEFAULT =
            new ResourceLocation("mpekka", "textures/entity/mini_pekka_rage_overlay.png");
    private static final ResourceLocation OVERLAY_PANCAKE =
            new ResourceLocation("mpekka", "textures/entity/mini_pk_pancake_overlay.png");
    private static final ResourceLocation OVERLAY_HERO =
            new ResourceLocation("mpekka", "textures/entity/mini_pk_hero_overlay.png");
    private static final ResourceLocation OVERLAY_HERO_PANCAKE =
            new ResourceLocation("mpekka", "textures/entity/mini_pk_hero_pancake_overlay.png");

    private static final float R = 0.7F;
    private static final float G = 0.3F;
    private static final float B = 1.0F;

    private static final float ALPHA_BASE = 0.40F;

    public MiniPekkaRageEffectLayer(GeoRenderer<MiniPekka> entityRenderer) {
        super(entityRenderer);
    }

    private ResourceLocation getOverlayTexture(MiniPekka animatable) {
        boolean isHero = animatable.isHeroMode();
        boolean isPancake = animatable.hasPancakesSkin();

        if (isHero && isPancake) return OVERLAY_HERO_PANCAKE;
        if (isHero) return OVERLAY_HERO;
        if (isPancake) return OVERLAY_PANCAKE;
        return OVERLAY_DEFAULT;
    }

    @Override
    public void render(PoseStack poseStack,
                       MiniPekka animatable,
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
