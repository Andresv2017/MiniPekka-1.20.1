package net.darkblade.mini_pekka.client.model;

import net.darkblade.mini_pekka.server.entity.HeadRotatable;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.TamableAnimal;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.model.GeoModel;

import static net.darkblade.mini_pekka.constants.CRConstans.HEAD_X_QUERY;
import static net.darkblade.mini_pekka.constants.CRConstans.HEAD_Y_QUERY;

public abstract class CRModel<T extends TamableAnimal & GeoAnimatable & HeadRotatable> extends GeoModel<T> {

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
    public void applyMolangQueries(AnimationState<T> animationState, double animTime) {
        super.applyMolangQueries(animationState, animTime);

        T animatable = animationState.getAnimatable();
        float headYaw = animatable.getCachedHeadYaw();
        float headPitch = animatable.getCachedHeadPitch();

        // Expose the cached head rotation to the molang queries used by the animation.
        MathParser.setVariable(HEAD_Y_QUERY, () -> headYaw);
        MathParser.setVariable(HEAD_X_QUERY, () -> headPitch);
    }
}
