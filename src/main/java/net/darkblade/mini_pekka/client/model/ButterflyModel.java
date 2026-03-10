package net.darkblade.mini_pekka.client.model;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.entity.projectile.ButterflyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ButterflyModel extends GeoModel<ButterflyEntity> {
    @Override
    public ResourceLocation getModelResource(ButterflyEntity object) {
        return new ResourceLocation(MiniPekkaMod.MODID, "geo/entity/butterfly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ButterflyEntity object) {
        return new ResourceLocation(MiniPekkaMod.MODID, "textures/entity/butterfly.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ButterflyEntity object) {
        return new ResourceLocation(MiniPekkaMod.MODID, "animations/entity/butterfly.animation.json");
    }
}
