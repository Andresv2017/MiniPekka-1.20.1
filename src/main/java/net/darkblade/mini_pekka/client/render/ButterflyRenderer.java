package net.darkblade.mini_pekka.client.render;

import net.darkblade.mini_pekka.client.model.ButterflyModel;
import net.darkblade.mini_pekka.server.entity.projectile.ButterflyEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ButterflyRenderer extends GeoEntityRenderer<ButterflyEntity> {
    public ButterflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ButterflyModel());
        this.shadowRadius = 0.0f;
    }

    @Override
    public RenderType getRenderType(ButterflyEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentEmissive(texture);
    }
}