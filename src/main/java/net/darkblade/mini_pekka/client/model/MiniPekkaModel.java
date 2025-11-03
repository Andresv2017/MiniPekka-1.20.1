package net.darkblade.mini_pekka.client.model;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MiniPekkaModel extends GeoModel<MiniPekka> {

    @Override
    public ResourceLocation getModelResource(MiniPekka miniPekka) {
        return new ResourceLocation(MiniPekkaMod.MODID, "geo/entity/mini_pk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MiniPekka miniPekka) {
        if (miniPekka.hasPancakesSkin()) {
            return new ResourceLocation(MiniPekkaMod.MODID, "textures/entity/mini_pk_pancake.png");
        } else {
            return new ResourceLocation(MiniPekkaMod.MODID, "textures/entity/mini_pk.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(MiniPekka miniPekka) {
        return new ResourceLocation(MiniPekkaMod.MODID,"animations/entity/mini_pk.animation.json");
    }
}