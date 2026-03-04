package net.darkblade.mini_pekka.client.render;

import net.darkblade.mini_pekka.client.model.MiniPekkaModel;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MiniPekkaRenderer extends GeoEntityRenderer<MiniPekka> {

    public MiniPekkaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MiniPekkaModel());
        this.addRenderLayer(new MiniPekkaRageEffectLayer(this));
        this.addRenderLayer(new MiniPekkaHeroChargeLayer(this));
    }
}
