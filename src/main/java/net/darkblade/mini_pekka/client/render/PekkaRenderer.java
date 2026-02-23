package net.darkblade.mini_pekka.client.render;

import net.darkblade.mini_pekka.client.model.PekkaModel;
import net.darkblade.mini_pekka.server.entity.Pekka;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PekkaRenderer extends GeoEntityRenderer<Pekka> {

    public PekkaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PekkaModel());
        this.addRenderLayer(new PekkaRageEffectLayer(this));
    }

    @Override
    protected float getDeathMaxRotation(Pekka animatable) {
        return 0f;
    }

    @Override
    public int getPackedOverlay(Pekka anim, float u, float partialTick) {
        if (anim.deathTime > 0) {
            return OverlayTexture.pack(OverlayTexture.u(0f), OverlayTexture.v(false));
        }
        boolean showRed = anim.hurtTime > 0;
        return OverlayTexture.pack(OverlayTexture.u(u), OverlayTexture.v(showRed));
    }
}
