package net.darkblade.mini_pekka.client.model;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.entity.Pekka;
import net.minecraft.resources.ResourceLocation;

public class PekkaModel extends CRModel<Pekka> {

    @Override
    public ResourceLocation getModelResource(Pekka pekka) {
        if (pekka.isEvoMode()) {
            return new ResourceLocation(MiniPekkaMod.MODID, "geo/entity/pekka_evo.geo.json");
        }
        return new ResourceLocation(MiniPekkaMod.MODID, "geo/entity/pekka.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Pekka pekka) {
        if (pekka.isEvoMode()) {
            return new ResourceLocation(MiniPekkaMod.MODID, "textures/entity/pekka_evo.png");
        }
        if (pekka.isStarMode()) {
            return new ResourceLocation(MiniPekkaMod.MODID, "textures/entity/pekka_star.png");
        }
        return new ResourceLocation(MiniPekkaMod.MODID, "textures/entity/pekka.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Pekka pekka) {
        if (pekka.isEvoMode()) {
            return new ResourceLocation(MiniPekkaMod.MODID, "animations/entity/pekka_evo.animation.json");
        }
        return new ResourceLocation(MiniPekkaMod.MODID, "animations/entity/pekka.animation.json");
    }
}
