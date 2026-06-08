package net.darkblade.mini_pekka.client.model;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.minecraft.resources.ResourceLocation;

public class MiniPekkaModel extends CRModel<MiniPekka> {

    @Override
    public ResourceLocation getModelResource(MiniPekka miniPekka) {
        if (miniPekka.isHeroMode()) {
            return ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "geo/entity/mini_pk_hero.geo.json");
        }
        return ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "geo/entity/mini_pk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MiniPekka animatable) {
        boolean isPancake = animatable.hasPancakesSkin();
        boolean isStar = animatable.isStarMode();
        boolean isHero = animatable.isHeroMode();

        if (isHero) {
            return isPancake
                    ? ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "textures/entity/mini_pk_hero_pancake.png")
                    : ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "textures/entity/mini_pk_hero.png");
        }

        if (isStar && isPancake) {
            return ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "textures/entity/mini_pk_star_pancake.png");
        } else if (isStar) {
            return ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "textures/entity/mini_pk_star.png");
        } else if (isPancake) {
            return ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "textures/entity/mini_pk_pancake.png");
        }

        return ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "textures/entity/mini_pk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MiniPekka miniPekka) {
        if (miniPekka.isHeroMode()) {
            return ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "animations/entity/mini_pk_hero.animation.json");
        }
        return ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "animations/entity/mini_pk.animation.json");
    }
}
