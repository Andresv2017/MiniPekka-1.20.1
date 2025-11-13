package net.darkblade.mini_pekka.server.entity;

import net.darkblade.mini_pekka.server.entity.ai.SimpleAabbMeleeGoal;
import net.darkblade.mini_pekka.server.items.ModItems;
import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
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

public class MiniPekka extends TamableAnimal implements GeoAnimatable {

    private static final EntityDataAccessor<Boolean> PANCAKES =
            SynchedEntityData.defineId(MiniPekka.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_ATTACKING =
            SynchedEntityData.defineId(MiniPekka.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MiniPekka(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.FOLLOW_RANGE, 28.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.ATTACK_SPEED, 0.8D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5F)
                .add(Attributes.ATTACK_DAMAGE, 10.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new SimpleAabbMeleeGoal<>(
                this,
                ATTACK_RANGE,
                CHASE_SPEED,
                REQUIRE_LOS,
                ATTACK_DURATION,
                DAMAGE_FRAMES,
                CD_BASE,
                BEAR_HITBOX,
                this::setAttacking
        ));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.4D, 8.0F, 2.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    private long lastPancakeSfxTick = -200L;

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(ModItems.PANCAKE.get()) && this.isTame()
                && player.getUUID().equals(this.getOwnerUUID())) {

            if (this.getHealth() < this.getMaxHealth()) {
                if (!level().isClientSide) {
                    float healAmount = 6.0F;
                    this.heal(healAmount);

                    ((ServerLevel) level()).sendParticles(
                            ParticleTypes.HEART,
                            getX(), getY() + getBbHeight() * 0.6D, getZ(),
                            6, 0.3D, 0.3D, 0.3D, 0.02D
                    );

                    long now = level().getGameTime();
                    if (now - lastPancakeSfxTick >= 120L) {
                        level().playSound(
                                null, this.getX(), this.getY(), this.getZ(),
                                ModSounds.PANCAKES.get(),
                                SoundSource.NEUTRAL, 1.0f, 1.0f
                        );
                        lastPancakeSfxTick = now;
                    }

                    level().playSound(null, this, SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 1.0f, 1.0f);

                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
            return InteractionResult.CONSUME_PARTIAL;
        }

        if (this.isTame()
                && player.getUUID().equals(this.getOwnerUUID())
                && stack.isEmpty()) {

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


    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        this.spawnAtLocation(new ItemStack(ModItems.MINI_PK_HEAD.get()));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PANCAKES, false);
        this.entityData.define(DATA_ATTACKING, false);
    }

    public boolean hasPancakesSkin() {
        return this.entityData.get(PANCAKES);
    }

    public void setPancakesSkin(boolean value) {
        this.entityData.set(PANCAKES, value);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        if (!level().isClientSide) {
            boolean magic = name != null && "pancakes".equalsIgnoreCase(name.getString().trim());
            if (magic) {
                this.setPancakesSkin(true);
                this.setCustomNameVisible(false);
                super.setCustomName(null);

                level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        ModSounds.PANCAKES.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
            }
        }
    }


    private boolean hasScoreboardPancakes() {
        return this.getTags().contains("pancakes");
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            boolean viaNameTag = this.hasCustomName()
                    && this.getCustomName() != null
                    && "pancakes".equalsIgnoreCase(this.getCustomName().getString().trim());

            boolean viaScoreboard = hasScoreboardPancakes();

            boolean desired = this.hasPancakesSkin() || viaNameTag || viaScoreboard;

            if (desired != this.hasPancakesSkin()) {
                this.setPancakesSkin(desired);
            }
            if (viaNameTag) this.setCustomNameVisible(false);
        }
    }

    private long lastStepSfxTick = 0L;

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide && this.isAlive() && this.onGround() && !this.isInSittingPose()) {
            var vel = this.getDeltaMovement();
            double speed2 = vel.x * vel.x + vel.z * vel.z;

            if (speed2 > 0.001D) {
                boolean chasingOrAttacking = this.getTarget() != null || this.isAttacking();
                int interval = chasingOrAttacking ? 20 : 30;

                long now = level().getGameTime();
                if (now - lastStepSfxTick >= interval) {
                    level().playSound(
                            null,
                            this.getX(), this.getY(), this.getZ(),
                            ModSounds.STEPS.get(),
                            this.getSoundSource(),
                            0.6F,
                            1.0F
                    );
                    lastStepSfxTick = now;
                }
            }
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.DEATH.get();
    }


    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasPancakesSkin", this.hasPancakesSkin());
        tag.putLong("LastPancakeSfx", this.lastPancakeSfxTick);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("HasPancakesSkin")) this.setPancakesSkin(tag.getBoolean("HasPancakesSkin"));
        if (tag.contains("LastPancakeSfx")) this.lastPancakeSfxTick = tag.getLong("LastPancakeSfx");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<MiniPekka> controller =
                new AnimationController<>(this, "controller", 10, this::predicate);
        controllers.add(controller);
    }


    @Override
    protected void tickDeath() {
        ++this.deathTime;
        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        if (this.deathTime >= 59) {
            if (!this.level().isClientSide) {
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    protected <E extends MiniPekka> PlayState predicate(final AnimationState<E> event) {
        if (this.deathTime > 0) {
            event.setAndContinue(RawAnimation.begin().thenPlay("death"));
            return PlayState.CONTINUE;
        }
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

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target instanceof Player p && this.isOwnedBy(p)) {
            return false;
        }

        if (target instanceof TamableAnimal other) {
            if (this.isTame() && other.isTame()) {
                UUID me = this.getOwnerUUID();
                UUID them = other.getOwnerUUID();
                if (me != null && me.equals(them)) {
                    return false;
                }
            }
        }

        return super.canAttack(target);
    }

    private void setAttacking(boolean v) {
        boolean was = this.entityData.get(DATA_ATTACKING);
        this.entityData.set(DATA_ATTACKING, v);
        if (!level().isClientSide && v && !was) {
            level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    ModSounds.ANA.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
        }
    }

    private boolean isAttacking() { return this.entityData.get(DATA_ATTACKING); }

    private static final double ATTACK_RANGE = 0.50;
    private static final double CHASE_SPEED  = 1.6;
    private static final boolean REQUIRE_LOS = true;

    private static final int  ATTACK_DURATION = 25;
    private static final int[] DAMAGE_FRAMES  = {12};
    private static final int  CD_BASE         = 8;

    private static final SimpleAabbMeleeGoal.AttackHitbox BEAR_HITBOX =
            SimpleAabbMeleeGoal.AttackHitbox.of(
                    0.50,
                    1.00,
                    1.0,
                    0.00,
                    0.30
            );

    @Override
    public double getTick(Object o) {
        return tickCount;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mate) {
        return null;
    }
}
