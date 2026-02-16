package net.darkblade.mini_pekka.client.model;

import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.model.GeoModel;

import static net.darkblade.mini_pekka.constants.CRConstans.HEAD_X_QUERY;
import static net.darkblade.mini_pekka.constants.CRConstans.HEAD_Y_QUERY;

public abstract class CRModel<T extends MiniPekka> extends GeoModel<T> {

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        float pt = animationState.getPartialTick();

        float maxYaw = 40.0F;
        float maxPitch = 30.0F;
        float initialYaw = animatable.getViewYRot(pt) - Mth.lerp(pt, animatable.yBodyRotO, animatable.yBodyRot);

        animatable.setCachedHeadYaw(Mth.clamp(initialYaw, -maxYaw, maxYaw));
        animatable.setCachedHeadPitch(Mth.clamp(animatable.getViewXRot(pt), -maxPitch, maxPitch));
    }

    @Override
    public void applyMolangQueries(T animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);

        MolangParser parser = MolangParser.INSTANCE;

        parser.setValue(HEAD_Y_QUERY, () -> animatable.getCachedHeadYaw());
        parser.setValue(HEAD_X_QUERY, () -> animatable.getCachedHeadPitch());

    }
}

