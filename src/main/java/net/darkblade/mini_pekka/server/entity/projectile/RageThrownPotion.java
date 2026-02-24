package net.darkblade.mini_pekka.server.entity.projectile;

import net.darkblade.mini_pekka.client.particles.util.ModParticleUtils;
import net.darkblade.mini_pekka.server.effect.ModEffects;
import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.darkblade.mini_pekka.server.items.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.UUID;

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

        Entity ownerEntity = this.getOwner();
        UUID ownerUUID = ownerEntity != null ? ownerEntity.getUUID() : null;

        AABB area = this.getBoundingBox().inflate(EFFECT_RADIUS);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, area);

        for (LivingEntity target : targets) {
            if (!target.isAlive() || !target.isAffectedByPotions()) continue;

            if (isAlly(target, ownerUUID)) {
                // Aliado: aplicar efecto de rage (boost)
                MobEffectInstance furyEffect = new MobEffectInstance(
                        ModEffects.RAGE.get(), duration, amplifier, false, true
                );
                target.addEffect(furyEffect, ownerEntity instanceof LivingEntity l ? l : null);
            } else {
                DamageSource src = target.damageSources().magic();
                if (ownerEntity instanceof LivingEntity livingOwner) {
                    src = target.damageSources().indirectMagic(this, livingOwner);
                }
                target.hurt(src, 2.0F);
            }
        }
    }


    private boolean isAlly(LivingEntity target, UUID ownerUUID) {
        if (ownerUUID == null) return false;

        if (target.getUUID().equals(ownerUUID)) return true;

        if (target instanceof TamableAnimal tamable) {
            UUID tamableOwner = tamable.getOwnerUUID();
            if (tamableOwner != null && tamableOwner.equals(ownerUUID)) return true;
        }

        return false;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModItems.RAGE_POTION.get());
    }
}