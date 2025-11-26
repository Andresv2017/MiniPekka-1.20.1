package net.darkblade.mini_pekka.client.render;

import net.darkblade.mini_pekka.client.model.MiniPekkaModel;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MiniPekkaRenderer extends GeoEntityRenderer<MiniPekka> {

    public MiniPekkaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MiniPekkaModel());
        this.addRenderLayer(new MiniPekkaRageEffectLayer(this));
    }

    @Override
    protected float getDeathMaxRotation(MiniPekka animatable) {
        return 0f;
    }

    @Override
    public int getPackedOverlay(MiniPekka anim, float u, float partialTick) {
        if (anim.deathTime > 0) {
            return OverlayTexture.pack(OverlayTexture.u(0f), OverlayTexture.v(false));
        }
        boolean showRed = anim.hurtTime > 0;
        return OverlayTexture.pack(OverlayTexture.u(u), OverlayTexture.v(showRed));
    }
}
