package net.darkblade.mini_pekka.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;

import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class MiniPekkaHeroChargeLayer extends GeoRenderLayer<MiniPekka> {

    private static final float BAR_WIDTH = 0.8F;
    private static final float BAR_HEIGHT = 0.08F;
    private static final float BAR_Y_OFFSET = 1.65F;

    private static final float BG_R = 0.15F, BG_G = 0.15F, BG_B = 0.15F, BG_A = 0.7F;

    private static final float CHARGE_R = 1.0F, CHARGE_G = 0.85F, CHARGE_B = 0.1F, CHARGE_A = 0.9F;

    private static final float READY_R = 1.0F, READY_G = 0.65F, READY_B = 0.1F;

    private static final float ACTIVE_R = 1.0F, ACTIVE_G = 0.1F, ACTIVE_B = 0.1F, ACTIVE_A = 0.95F;

    public MiniPekkaHeroChargeLayer(GeoRenderer<MiniPekka> renderer) {
        super(renderer);
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

        if (!animatable.isHeroMode()) return;

        int charge = animatable.getHeroCharge();
        boolean abilityActive = animatable.isHeroAbilityActive();

        if (charge <= 0 && !abilityActive) return;

        float fillPercent;
        float r, g, b, a;

        if (abilityActive) {
            fillPercent = 1.0F;
            float time = (float) animatable.tickCount + partialTick;
            float pulse = (float) (Math.sin(time * 0.45F) + 1.0F) * 0.5F;

            r = ACTIVE_R * (0.6F + pulse * 0.4F);
            g = ACTIVE_G * pulse;
            b = ACTIVE_B * pulse;
            a = 0.5F + pulse * 0.5F;

        } else if (charge >= MiniPekka.HERO_CHARGE_MAX) {
            fillPercent = 1.0F;
            float time = (float) animatable.tickCount + partialTick;
            float pulse = (float) (Math.sin(time * 0.5F) + 1.0F) * 0.5F;

            r = READY_R;
            g = READY_G * (0.4F + pulse * 0.6F);
            b = READY_B * (0.2F + pulse * 0.8F);
            a = 0.5F + pulse * 0.5F;

        } else {
            fillPercent = (float) charge / MiniPekka.HERO_CHARGE_MAX;
            r = CHARGE_R;
            g = CHARGE_G;
            b = CHARGE_B;
            a = CHARGE_A;
        }

        poseStack.pushPose();

        var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        poseStack.mulPose(Axis.YP.rotationDegrees(-animatable.getViewYRot(partialTick)));
        poseStack.translate(0.0F, BAR_Y_OFFSET, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(animatable.getViewYRot(partialTick)));
        poseStack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));

        poseStack.scale(1.0F, 1.0F, 1.0F);

        RenderType rt = RenderType.entityTranslucentEmissive(
                new net.minecraft.resources.ResourceLocation("minecraft", "textures/misc/white.png")
        );
        VertexConsumer vc = bufferSource.getBuffer(rt);
        Matrix4f matrix = poseStack.last().pose();

        float halfW = BAR_WIDTH / 2.0F;
        float halfH = BAR_HEIGHT / 2.0F;

        drawQuad(matrix, vc, -halfW, -halfH, halfW, halfH,
                BG_R, BG_G, BG_B, BG_A, packedLight);

        float fillW = -halfW + BAR_WIDTH * fillPercent;
        if (fillPercent > 0) {
            float inset = 0.005F;
            drawQuad(matrix, vc, -halfW + inset, -halfH + inset, fillW - inset, halfH - inset,
                    r, g, b, a, packedLight);
        }

        float borderT = 0.01F;
        float borderA = 0.5F;
        drawQuad(matrix, vc, -halfW, halfH - borderT, halfW, halfH,
                1F, 1F, 1F, borderA, packedLight);
        drawQuad(matrix, vc, -halfW, -halfH, halfW, -halfH + borderT,
                1F, 1F, 1F, borderA, packedLight);
        drawQuad(matrix, vc, -halfW, -halfH, -halfW + borderT, halfH,
                1F, 1F, 1F, borderA, packedLight);
        drawQuad(matrix, vc, halfW - borderT, -halfH, halfW, halfH,
                1F, 1F, 1F, borderA, packedLight);

        poseStack.popPose();
    }

    private void drawQuad(Matrix4f matrix, VertexConsumer vc,
                          float x1, float y1, float x2, float y2,
                          float r, float g, float b, float a,
                          int packedLight) {
        vc.vertex(matrix, x1, y1, 0).color(r, g, b, a).uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        vc.vertex(matrix, x1, y2, 0).color(r, g, b, a).uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        vc.vertex(matrix, x2, y2, 0).color(r, g, b, a).uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
        vc.vertex(matrix, x2, y1, 0).color(r, g, b, a).uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0, 0, 1).endVertex();
    }
}
