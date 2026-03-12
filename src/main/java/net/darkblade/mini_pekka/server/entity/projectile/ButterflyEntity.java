package net.darkblade.mini_pekka.server.entity.projectile;

import net.darkblade.mini_pekka.server.entity.Pekka;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ButterflyEntity extends Entity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(ButterflyEntity.class, EntityDataSerializers.INT);

    private int lifeTicks = 0;

    public ButterflyEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TARGET_ID, -1);
    }

    public void setTargetPekka(Entity pekka) {
        this.entityData.set(TARGET_ID, pekka.getId());
    }

    private float healAmount = 0.0f;

    public void setHealAmount(float amount) {
        this.healAmount = amount;
    }

    @Override
    public void tick() {
        super.tick();

        int targetId = this.entityData.get(TARGET_ID);

        if (targetId == -1) {
            if (!this.level().isClientSide()) this.discard();
            return;
        }

        Entity target = this.level().getEntity(targetId);

        if (target == null || !target.isAlive() || !(target instanceof Pekka pekka)) {
            if (!this.level().isClientSide()) this.discard();
            return;
        }

        lifeTicks++;

        if (lifeTicks <= 15) {
            // Fase 1: Elevación
            if (lifeTicks == 1) {
                this.setDeltaMovement(0, 0.35, 0);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.80));
            }
        } else if (lifeTicks <= 40) {
            double hoverMotionY = Math.sin((lifeTicks - 15) * 0.25) * 0.015;
            this.setDeltaMovement(0, hoverMotionY, 0);
        } else {
            Vec3 targetPos = new Vec3(target.getX(), target.getY() + (target.getBbHeight() * 0.55), target.getZ());
            Vec3 moveVec = targetPos.subtract(this.position());

            if (this.getBoundingBox().intersects(target.getBoundingBox().inflate(-0.3))) {
                if (!this.level().isClientSide()) {
                    pekka.receiveButterflyHeal(this.healAmount);
                    this.discard();
                }
                return;
            } else {
                Vec3 currentMotion = this.getDeltaMovement();
                Vec3 desiredMotion = moveVec.normalize().scale(0.50);
                this.setDeltaMovement(currentMotion.lerp(desiredMotion, 0.15));
            }
        }

        this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("HealAmount", this.healAmount);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.healAmount = tag.getFloat("HealAmount");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 0, event -> {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("fly"));
            return software.bernie.geckolib.core.object.PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}