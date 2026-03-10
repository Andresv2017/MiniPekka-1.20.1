package net.darkblade.mini_pekka.client.render;

import net.darkblade.mini_pekka.client.model.ButterflyModel;
import net.darkblade.mini_pekka.server.entity.projectile.ButterflyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ButterflyRenderer extends GeoEntityRenderer<ButterflyEntity> {
    public ButterflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ButterflyModel());
        this.shadowRadius = 0.2f;
    }
}