package net.darkblade.mini_pekka.server.entity;

import net.darkblade.mini_pekka.client.particles.ModParticles;
import net.darkblade.mini_pekka.server.effect.ModEffects;
import net.darkblade.mini_pekka.server.entity.ai.SimpleAabbMeleeGoal;
import net.darkblade.mini_pekka.server.items.ModItems;
import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class Pekka extends TamableAnimal implements GeoAnimatable, HeadRotatable {

    private static final EntityDataAccessor<Boolean> DATA_ATTACKING =
            SynchedEntityData.defineId(Pekka.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RAGING =
            SynchedEntityData.defineId(Pekka.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_STAR_MODE =
            SynchedEntityData.defineId(Pekka.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_EVO_MODE =
            SynchedEntityData.defineId(Pekka.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACK_INDEX =
            SynchedEntityData.defineId(Pekka.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EVO_ABILITY_FLASH_TICKS =
            SynchedEntityData.defineId(Pekka.class, EntityDataSerializers.INT);

    private static final UUID RAGE_ATTACK_SPEED_UUID =
            UUID.fromString("ae7eb812-99f4-4e96-b3e4-184c99090c37");

    private static final float EVO_HEAL_MIN_PERCENT = 0.035F;
    private static final float EVO_HEAL_MAX_PERCENT = 0.15F;
    private static final float EVO_VICTIM_HP_LOW    = 6.0F;
    private static final float EVO_VICTIM_HP_HIGH   = 100.0F;
    private static final float EVO_OVERHEAL_CAP     = 0.50F;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean wasHurt = false;
    private int spawnGraceTicks = 30;

    private static final double ATTACK_RANGE = 1.40;
    private static final double CHASE_SPEED  = 1.4;
    private static final boolean REQUIRE_LOS = true;
    private static final int  ATTACK_DURATION = 20;
    private static final int[] DAMAGE_FRAMES  = {16};
    private static final int  CD_BASE         = 0;

    private static final SimpleAabbMeleeGoal.AttackHitbox HITBOX =
            SimpleAabbMeleeGoal.AttackHitbox.of(1.15, 2.00, 1.5, 0.00, 0.0);

    public Pekka(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.FOLLOW_RANGE, 28.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18D)
                .add(Attributes.ATTACK_SPEED, 0.8D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8F)
                .add(Attributes.ATTACK_DAMAGE, 30.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new SimpleAabbMeleeGoal<>(
                this, ATTACK_RANGE, CHASE_SPEED, REQUIRE_LOS,
                ATTACK_DURATION, DAMAGE_FRAMES, CD_BASE, HITBOX,
                this::setAttacking,
                this::getEvoAttackTempo
        ));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.4D, 8.0F, 2.0F, false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    private int attackSoundDelay = -1;

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(ModItems.EVO_CRYSTAL.get()) && this.isTame() && this.isOwnedBy(player)) {
            if (!this.isEvoMode()) {
                if (!this.level().isClientSide) {
                    this.setEvoMode(true);
                    level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            ModSounds.PEKKA_EVO_SPAWN.get(), SoundSource.NEUTRAL, 1.5f, 1.0f);
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                            this.getX(), this.getY() + 0.5D, this.getZ(),
                            30, 0.3D, 0.5D, 0.3D, 0.2D);
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        if (stack.is(ModItems.STAR_ITEM.get()) && this.isTame() && this.isOwnedBy(player)) {
            if (!this.isStarMode()) {
                if (!this.level().isClientSide) {
                    this.setStarMode(true);
                    this.playSound(SoundEvents.PLAYER_LEVELUP, 1.0f, 1.0f);
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.FIREWORK,
                            this.getX(), this.getY() + 0.5D, this.getZ(),
                            5, 0.2D, 0.2D, 0.2D, 0.1D);
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        if (this.isTame() && this.isOwnedBy(player) && stack.isEmpty()) {
            if (!level().isClientSide) {
                boolean sit = !this.isOrderedToSit();
                this.setOrderedToSit(sit);
                this.setInSittingPose(sit);
                this.getNavigation().stop();
                this.setTarget(null);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    private float cachedHeadYaw = 0F;
    private float cachedHeadPitch = 0F;

    @Override public float getCachedHeadYaw() { return cachedHeadYaw; }
    @Override public void setCachedHeadYaw(float yaw) { this.cachedHeadYaw = yaw; }
    @Override public float getCachedHeadPitch() { return cachedHeadPitch; }
    @Override public void setCachedHeadPitch(float pitch) { this.cachedHeadPitch = pitch; }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACKING, false);
        this.entityData.define(RAGING, false);
        this.entityData.define(IS_STAR_MODE, false);
        this.entityData.define(IS_EVO_MODE, false);
        this.entityData.define(ATTACK_INDEX, 0);
        this.entityData.define(EVO_ABILITY_FLASH_TICKS, 0);
    }

    public boolean isRaging() { return this.entityData.get(RAGING); }
    public void setRaging(boolean value) { this.entityData.set(RAGING, value); }
    public boolean isStarMode() { return this.entityData.get(IS_STAR_MODE); }
    public void setStarMode(boolean isStar) { this.entityData.set(IS_STAR_MODE, isStar); }
    public boolean isEvoMode() { return this.entityData.get(IS_EVO_MODE); }
    public void setEvoMode(boolean evo) { this.entityData.set(IS_EVO_MODE, evo); }
    public int getAttackIndex() { return this.entityData.get(ATTACK_INDEX); }
    public void setAttackIndex(int index) { this.entityData.set(ATTACK_INDEX, index); }
    public boolean isAttacking() { return this.entityData.get(DATA_ATTACKING); }
    public int getEvoAbilityFlashTicks() { return this.entityData.get(EVO_ABILITY_FLASH_TICKS); }
    public void setEvoAbilityFlashTicks(int ticks) { this.entityData.set(EVO_ABILITY_FLASH_TICKS, ticks); }

    private void setAttacking(boolean v) {
        boolean was = this.entityData.get(DATA_ATTACKING);
        this.entityData.set(DATA_ATTACKING, v);
        if (!level().isClientSide && v && !was) {
            this.attackSoundDelay = 5;
        }
        if (!level().isClientSide && !v && was) {
            this.setAttackIndex(this.getAttackIndex() == 0 ? 1 : 0);
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (spawnGraceTicks > 0 && target != null) return;
        super.setTarget(target);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            if (spawnGraceTicks > 0) spawnGraceTicks--;

            if (this.getEvoAbilityFlashTicks() > 0) {
                this.setEvoAbilityFlashTicks(this.getEvoAbilityFlashTicks() - 1);
            }

            boolean hasFury = this.hasEffect(ModEffects.RAGE.get());
            if (hasFury != this.isRaging()) {
                this.setRaging(hasFury);
                this.updateRageAttackSpeed(hasFury);
            }

            if (this.attackSoundDelay > 0) {
                this.attackSoundDelay--;
                if (this.attackSoundDelay == 0) {
                    float pitch = hasFury ? 0.9f : 0.8f;
                    SoundEvent attackSound;
                    if (this.isEvoMode()) {
                        attackSound = (this.getAttackIndex() == 0)
                                ? ModSounds.PEKKA_EVO_ATTACK.get()
                                : ModSounds.PEKKA_EVO_ATTACK2.get();
                    } else {
                        attackSound = (this.getAttackIndex() == 0)
                                ? ModSounds.PEKKA_ATTACK.get()
                                : ModSounds.PEKKA_ATTACK2.get();
                    }
                    level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            attackSound, SoundSource.NEUTRAL, 1.5f, pitch);
                    this.attackSoundDelay = -1;
                }
            }
        }
    }

    private void updateRageAttackSpeed(boolean raging) {
        AttributeInstance attr = this.getAttribute(Attributes.ATTACK_SPEED);
        if (attr == null) return;
        AttributeModifier existing = attr.getModifier(RAGE_ATTACK_SPEED_UUID);
        if (existing != null) attr.removeModifier(existing);
        if (raging) {
            attr.addTransientModifier(new AttributeModifier(
                    RAGE_ATTACK_SPEED_UUID, "pekka_rage_attack_speed",
                    0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    private long lastStepSfxTick = -200L;

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && this.isAlive() && this.onGround() && !this.isInSittingPose()) {
            var vel = this.getDeltaMovement();
            double speed2 = vel.x * vel.x + vel.z * vel.z;
            if (speed2 > 0.001D) {
                boolean chasingOrAttacking = this.getTarget() != null || this.isAttacking();
                int interval = chasingOrAttacking ? 18 : 28;
                long now = level().getGameTime();
                if (now - lastStepSfxTick >= interval) {
                    SoundEvent stepSound = this.isEvoMode()
                            ? ModSounds.PEKKA_EVO_STEP.get()
                            : ModSounds.PEKKA_STEP.get();
                    level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            stepSound, this.getSoundSource(), 0.8F, 0.7F);
                    lastStepSfxTick = now;
                }
            }
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.PEKKA_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isEvoMode() ? ModSounds.PEKKA_EVO_AMBIENT.get() : ModSounds.PEKKA_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.level() instanceof ServerLevel sl) {
            double cx = this.getX();
            double cy = this.getY() + this.getBbHeight() * 0.5;
            double cz = this.getZ();
            for (int i = 0; i < 18; i++) {
                double ox = (this.random.nextDouble() - 0.5) * this.getBbWidth();
                double oy = this.random.nextDouble() * this.getBbHeight() * 0.6;
                double oz = (this.random.nextDouble() - 0.5) * this.getBbWidth();
                double vx = (this.random.nextDouble() - 0.5) * 0.15;
                double vy = 0.1 + this.random.nextDouble() * 0.2;
                double vz = (this.random.nextDouble() - 0.5) * 0.15;
                sl.sendParticles(ModParticles.ELIXIR_DROP.get(),
                        cx + ox, cy + oy, cz + oz,
                        1, vx, vy, vz, 0.05);
            }
        }
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity victim) {
        boolean result = super.killedEntity(level, victim);

        if (this.isEvoMode() && !this.isDeadOrDying()) {
            float victimMaxHp = victim.getMaxHealth();
            float baseMaxHp   = (float) this.getAttributeBaseValue(Attributes.MAX_HEALTH);

            float t = (victimMaxHp - EVO_VICTIM_HP_LOW) / (EVO_VICTIM_HP_HIGH - EVO_VICTIM_HP_LOW);
            t = Math.max(0.0F, Math.min(1.0F, t));
            float healPercent = EVO_HEAL_MIN_PERCENT + t * (EVO_HEAL_MAX_PERCENT - EVO_HEAL_MIN_PERCENT);
            float healAmount  = baseMaxHp * healPercent;


            applyEvoHeal(healAmount, baseMaxHp);
            this.setEvoAbilityFlashTicks(40);
        }

        return result;
    }

    private void applyEvoHeal(float healAmount, float baseMaxHp) {
        float missingHp = baseMaxHp - this.getHealth();
        float normalHeal = Math.min(healAmount, Math.max(0, missingHp));
        if (normalHeal > 0) {
            this.heal(normalHeal);
        }

        float leftover = healAmount - normalHeal;

        float overhealCap = baseMaxHp * EVO_OVERHEAL_CAP;
        float currentAbsorption = this.getAbsorptionAmount();
        float availableOverheal = overhealCap - currentAbsorption;

        if (leftover > 0 && availableOverheal > 0) {
            float overhealToApply = Math.min(leftover, availableOverheal);
            this.setAbsorptionAmount(currentAbsorption + overhealToApply);
        }

        if (this.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.HEART,
                    this.getX(), this.getY() + this.getBbHeight() * 0.7, this.getZ(),
                    4, 0.3, 0.2, 0.3, 0.02);
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target instanceof Player p && this.isOwnedBy(p)) return false;
        if (target instanceof TamableAnimal other && this.isTame() && other.isTame()) {
            UUID me = this.getOwnerUUID();
            UUID them = other.getOwnerUUID();
            if (me != null && me.equals(them)) return false;
        }
        return super.canAttack(target);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsStarMode", this.isStarMode());
        tag.putBoolean("IsEvoMode", this.isEvoMode());
        tag.putFloat("EvoAbsorption", this.getAbsorptionAmount());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setStarMode(tag.getBoolean("IsStarMode"));
        this.setEvoMode(tag.getBoolean("IsEvoMode"));
        if (tag.contains("EvoAbsorption")) {
            this.setAbsorptionAmount(tag.getFloat("EvoAbsorption"));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 7, this::predicate));
    }


    protected <E extends Pekka> PlayState predicate(final AnimationState<E> event) {
        if (this.hurtTime > 0 && !this.isDeadOrDying() && !this.isAttacking()) {
            if (!wasHurt) event.getController().forceAnimationReset();
            wasHurt = true;
            event.setAndContinue(RawAnimation.begin().thenPlay("hurt"));
            return PlayState.CONTINUE;
        }
        wasHurt = false;
        if (this.isInSittingPose()) {
            event.setAndContinue(RawAnimation.begin().thenLoop("sit"));
            return PlayState.CONTINUE;
        }
        if (this.isAttacking()) {
            event.setAndContinue(RawAnimation.begin().thenPlay("attack"));
            return PlayState.CONTINUE;
        }
        if (!event.isMoving()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
    }

    private static final int EVO_ATTACK_DURATION = 20;
    private static final int[] EVO_ATTACK_DAMAGE = {16};

    private int[] getEvoAttackTempo() {
        if (!this.isEvoMode()) return null;
        return new int[]{EVO_ATTACK_DURATION, EVO_ATTACK_DAMAGE[0]};
    }

    @Override
    public double getTick(Object o) { return tickCount; }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mate) { return null; }

    @Override
    public void onEffectRemoved(MobEffectInstance effect) {
        super.onEffectRemoved(effect);
        if (effect.getEffect() == ModEffects.RAGE.get()) updateRageAttackSpeed(false);
    }
}
