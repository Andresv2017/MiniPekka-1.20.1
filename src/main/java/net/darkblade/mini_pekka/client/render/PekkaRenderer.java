package net.darkblade.mini_pekka.client.render;

import net.darkblade.mini_pekka.client.model.PekkaModel;
import net.darkblade.mini_pekka.server.entity.Pekka;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PekkaRenderer extends GeoEntityRenderer<Pekka> {

    public PekkaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PekkaModel());
        this.addRenderLayer(new PekkaRageEffectLayer(this));
        this.addRenderLayer(new PekkaEvoAbilityLayer(this));
    }
}
