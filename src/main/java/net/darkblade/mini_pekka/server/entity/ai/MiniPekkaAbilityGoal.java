package net.darkblade.mini_pekka.server.entity.ai;

import net.darkblade.mini_pekka.client.particles.ModParticles;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class MiniPekkaAbilityGoal extends Goal {
    private final MiniPekka mob;
    private int timer;
    private final int ANIMATION_DURATION = 12;

    public MiniPekkaAbilityGoal(MiniPekka mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return mob.isHeroAbilityActive() && mob.getHeroAbilityTicksRemaining() > (600 - ANIMATION_DURATION);
    }

    @Override
    public void start() {
        this.timer = ANIMATION_DURATION;
        this.mob.getNavigation().stop();
        this.mob.setCastingAbility(true);
    }

    @Override
    public void stop() {
        this.mob.setCastingAbility(false);
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer > 0;
    }

    @Override
    public void tick() {
        this.timer--;
        this.mob.getNavigation().stop();

        if (this.timer == ANIMATION_DURATION - 6) {
            if (this.mob.level() instanceof ServerLevel serverLevel) {
                double x = this.mob.getX();
                double y = this.mob.getY() + this.mob.getBbHeight();
                double z = this.mob.getZ();

                serverLevel.sendParticles(ModParticles.ARROW_UP.get(),
                        x, y + 0.3D, z,
                        7,
                        0.4D, 0.2D, 0.4D,
                        0.02D);


                serverLevel.sendParticles(ModParticles.SPATULA.get(),
                        x, y + 1.2D, z,
                        1,
                        0.0D, 0.0D, 0.0D,
                        0.0D);
            }
        }
    }
}