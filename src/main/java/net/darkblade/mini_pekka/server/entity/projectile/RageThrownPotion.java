package net.darkblade.mini_pekka.server.entity.projectile;

import net.darkblade.mini_pekka.client.particles.util.ModParticleUtils;
import net.darkblade.mini_pekka.server.effect.ModEffects;
import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.darkblade.mini_pekka.server.items.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class RageThrownPotion extends ThrownPotion {

    private static final double EFFECT_RADIUS = 5.0D;

    public RageThrownPotion(EntityType<? extends ThrownPotion> type, Level level) {
        super(type, level);
    }

    public RageThrownPotion(Level level, LivingEntity thrower) {
        super(level, thrower);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    ModSounds.RAGE_BREAK.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);

            if (this.level() instanceof ServerLevel serverLevel) {
                ModParticleUtils.spawnRageAura(serverLevel, hitResult.getLocation(), EFFECT_RADIUS);
            }

            this.applyRageEffects();
            this.discard();
        }
    }

    protected void applyRageEffects() {
        int duration = 600;
        int amplifier = 0;

        AABB area = this.getBoundingBox().inflate(EFFECT_RADIUS);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, area);

        for (LivingEntity target : targets) {
            if (target.isAlive() && target.isAffectedByPotions()) {
                MobEffectInstance furyEffect = new MobEffectInstance(
                        ModEffects.RAGE.get(), duration, amplifier, false, true
                );
                target.addEffect(furyEffect, this.getOwner() instanceof LivingEntity l ? l : null);
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.RAGE_POTION.get());
    }
}