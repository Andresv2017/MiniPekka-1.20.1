package net.darkblade.mini_pekka.server.entity.ai;

import net.darkblade.mini_pekka.server.entity.debug.DebugAABB;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.TamableAnimal;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class SimpleAabbMeleeGoal<E extends PathfinderMob> extends Goal {

    public interface AttackAnimBridge { void setAttacking(boolean value); }

    public static final class AttackHitbox {
        public final double halfSize, height, forward, lateral, yOffset;
        public AttackHitbox(double halfSize, double height, double forward, double lateral, double yOffset) {
            this.halfSize = halfSize; this.height = height;
            this.forward = forward; this.lateral = lateral; this.yOffset = yOffset;
        }
        public static AttackHitbox of(double half, double h, double f, double lat, double y) {
            return new AttackHitbox(half, h, f, lat, y);
        }
    }

    private final E mob;
    private final double CHASE_SPEED;

    private final double ATTACK_RANGE;
    private final boolean REQUIRE_LOS;

    private final int   DURATION_TICKS;
    private final int[] DAMAGE_FRAMES;
    private final int   COOLDOWN_BASE_TICKS;
    private final AttackHitbox HITBOX;
    private final AttackAnimBridge animBridge;

    private int dynamicAttackDuration;
    private int[] dynamicDamageFrames;
    private int dynamicAttackDelayTicks;

    private boolean active = false;
    private int tick = 0;
    private int cooldown = 0;

    // NEW: Debug para medir el tiempo real
    private int attackStartTimeTick = 0;

    private Path path;
    private double pathedTargetX, pathedTargetY, pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = true;

    private static final int RECALC_BASE_MIN = 2;
    private static final int RECALC_BASE_MAX = 4;
    private static final int FAILED_PENALTY_MAX = 20;

    private static final boolean DEBUG = false; // TEXT (original System.out.printf)
    private static final boolean DEBUG_AABB = false; // ATTACK HITBOX
    private static final boolean DEBUG_HUD = false; // NEW: Activar/desactivar el debug en la action bar

    // Debug HUD Variables
    private int dbgNextSendTick = 0;
    private String dbgCachedLine = "";
    private boolean dbgPrevActive = false;
    private String dbgStickyMsg = "";
    private int dbgStickyUntilTick = 0;
    private int dbgStickyPrio = 0;
    private int dbgStickySetTick = -9999;

    public SimpleAabbMeleeGoal(
            E mob,
            double attackRangeEdge,
            double chaseSpeed,
            boolean requireLOS,
            int durationTicks,
            int[] damageFrames,
            int cooldownBase,
            AttackHitbox hitbox,
            AttackAnimBridge animBridge
    ) {
        this.mob = mob;
        this.ATTACK_RANGE = Math.max(0.0, attackRangeEdge);
        this.CHASE_SPEED = chaseSpeed;
        this.REQUIRE_LOS = requireLOS;
        this.DURATION_TICKS = durationTicks;
        this.DAMAGE_FRAMES  = damageFrames;
        this.COOLDOWN_BASE_TICKS = cooldownBase;
        this.HITBOX = hitbox;
        this.animBridge = animBridge;

        this.dynamicAttackDuration = durationTicks;
        this.dynamicDamageFrames = damageFrames;
        this.dynamicAttackDelayTicks = 20;

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        if (t == null || !t.isAlive()) return false;

        this.path = mob.getNavigation().createPath(t, 0);
        if (this.path != null) return true;

        return isInAttackRangeEdgeXZ(t);
    }

    @Override
    public boolean canContinueToUse() {
        // [FIX] Si el ataque está activo (animación en curso), continuar aunque el objetivo muera
        if (active) return true;

        LivingEntity t = mob.getTarget();
        return t != null && t.isAlive();
    }

    @Override
    public void start() {
        LivingEntity t = mob.getTarget();
        if (t != null && this.path != null) mob.getNavigation().moveTo(this.path, CHASE_SPEED);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        setAttackActive(false);
        mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() { return true; }

    @Override
    public void tick() {
        final LivingEntity target = mob.getTarget();

        // [FIX] Si estamos atacando pero el objetivo es nulo o murió, solo actualizamos la animación y retornamos
        if (active && (target == null || !target.isAlive())) {
            updateAttackFrames();
            // NEW: Actualizar la Action Bar aunque no haya objetivo, si estamos activos
            if (!mob.level().isClientSide) renderDebugActionbar(null);
            return;
        }

        if (target == null) { setAttackActive(false); return; }

        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        final double d3 = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());

        final boolean inEdgeRange = isInAttackRangeEdgeXZ(target);

        this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
        final boolean hasLOS = !REQUIRE_LOS || mob.getSensing().hasLineOfSight(target);

        if (this.ticksUntilNextPathRecalculation <= 0 &&
                (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0
                        || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0
                        || mob.getRandom().nextFloat() < 0.05F)) {

            this.pathedTargetX = target.getX();
            this.pathedTargetY = target.getY();
            this.pathedTargetZ = target.getZ();

            this.ticksUntilNextPathRecalculation = RECALC_BASE_MIN + mob.getRandom().nextInt(RECALC_BASE_MAX - RECALC_BASE_MIN + 1);

            if (this.canPenalize) {
                this.ticksUntilNextPathRecalculation += this.failedPathFindingPenalty;
                if (mob.getNavigation().getPath() != null) {
                    Node end = mob.getNavigation().getPath().getEndNode();
                    if (end != null && target.distanceToSqr(end.x, end.y, end.z) < 1.0) {
                        this.failedPathFindingPenalty = 0;
                    } else {
                        this.failedPathFindingPenalty = Math.min(this.failedPathFindingPenalty + 10, FAILED_PENALTY_MAX);
                    }
                } else {
                    this.failedPathFindingPenalty = Math.min(this.failedPathFindingPenalty + 10, FAILED_PENALTY_MAX);
                }
            }

            if (d3 > 1024.0) this.ticksUntilNextPathRecalculation += 10;
            else if (d3 > 256.0) this.ticksUntilNextPathRecalculation += 5;

            if (!mob.getNavigation().moveTo(target, CHASE_SPEED)) {
                mob.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), CHASE_SPEED);
                this.ticksUntilNextPathRecalculation += 8;
            }

            this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
        }

        if ((mob.getNavigation().isDone() || mob.getNavigation().getPath() == null) && !inEdgeRange) {
            mob.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), CHASE_SPEED);
        }

        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);

        if (!active) {
            final boolean canStartAttack = !REQUIRE_LOS || hasLOS;
            if (inEdgeRange && this.ticksUntilNextAttack <= 0 && canStartAttack) {
                startAttack();
                resetAttackCooldown();
            }
        } else {
            updateAttackFrames();
        }

        if (DEBUG && (mob.tickCount % 10 == 0 || active)) {
            double[] r = edgeDebug(target);
            System.out.printf(
                    "[MELEE][%s] distXZ=%.2f sumR=%.2f edge=%.2f thr=%.2f LOS=%s atkCD=%d active=%s navDone=%s%n",
                    mob.getType().getDescriptionId(), r[0], r[1], r[2], r[3],
                    hasLOS ? "true" : "false", this.ticksUntilNextAttack,
                    active ? "true" : "false", mob.getNavigation().isDone() ? "true" : "false"
            );
        }

        if (active && !inEdgeRange) {
            mob.getNavigation().moveTo(target, CHASE_SPEED);
        }

        // NEW: Actualizar la Action Bar para el dueño
        if (!mob.level().isClientSide) {
            renderDebugActionbar(target);
        }
    }

    private boolean isInAttackRangeEdgeXZ(LivingEntity target) {
        double dx = target.getX() - mob.getX();
        double dz = target.getZ() - mob.getZ();
        double distXZ = Math.sqrt(dx*dx + dz*dz);
        double sumR = 0.5 * (mob.getBbWidth() + target.getBbWidth());
        double edge = Math.max(0.0, distXZ - sumR);
        return edge <= ATTACK_RANGE;
    }

    private double[] edgeDebug(LivingEntity target) {
        double dx = target.getX() - mob.getX();
        double dz = target.getZ() - mob.getZ();
        double distXZ = Math.sqrt(dx*dx + dz*dz);
        double sumR = 0.5 * (mob.getBbWidth() + target.getBbWidth());
        double edge = Math.max(0.0, distXZ - sumR);
        double thr = ATTACK_RANGE;
        return new double[]{distXZ, sumR, edge, thr};
    }

    protected void resetAttackCooldown() {
        refreshAttackTempos();
        this.ticksUntilNextAttack = this.adjustedTickDelay(dynamicAttackDelayTicks);
    }

    private void startAttack() {
        refreshAttackTempos();
        tick = 0;
        setAttackActive(true);
        // NEW: Registrar inicio y enviar debug
        attackStartTimeTick = mob.tickCount;
        double speed = computeAttackSpeedScale();
        debugHud(String.format("ATTACK START (Scale: %.2f)", speed));
    }

    private void endAttack() {
        setAttackActive(false);
        cooldown = COOLDOWN_BASE_TICKS;
        // NEW: Calcular duración y enviar debug
        int duration = mob.tickCount - attackStartTimeTick;
        double speed = computeAttackSpeedScale(); // Recalculamos la escala por si terminó el efecto de Rage
        double expectedDur = DURATION_TICKS * speed;
        debugHud(String.format("ATTACK END (Duration: %d ticks, Expected: %.2f)", duration, expectedDur));
    }

    private void setAttackActive(boolean v) {
        if (active == v) return;
        active = v;
        animBridge.setAttacking(v);
    }

    private void updateAttackFrames() {
        if (cooldown > 0) cooldown--;
        tick++;
        int[] frames = dynamicDamageFrames != null ? dynamicDamageFrames : DAMAGE_FRAMES;
        for (int f : frames) {
            if (tick == f) {
                AABB box = buildAABBFromHeadYaw(HITBOX);
                applyDamageInBox(box);
                if (DEBUG_AABB && mob.level() instanceof ServerLevel sl) {
                    DebugAABB.drawAabbEdges(sl, box);
                }
                break;
            }
        }
        int duration = dynamicAttackDuration > 0 ? dynamicAttackDuration : DURATION_TICKS;
        if (tick >= duration) endAttack();
    }

    private AABB buildAABBFromHeadYaw(AttackHitbox hb) {
        final float yawRad  = mob.getYHeadRot() * Mth.DEG_TO_RAD;
        final double dirX   = -Mth.sin(yawRad), dirZ = Mth.cos(yawRad);
        final double rightX = -dirZ,            rightZ = dirX;

        final double cx = mob.getX() + dirX * hb.forward + rightX * hb.lateral;
        final double cz = mob.getZ() + dirZ * hb.forward + rightZ * hb.lateral;
        final double minY = mob.getY() + hb.yOffset;

        return new AABB(
                cx - hb.halfSize, minY, cz - hb.halfSize,
                cx + hb.halfSize, minY + hb.height, cz + hb.halfSize
        );
    }

    private void applyDamageInBox(AABB box) {
        Predicate<LivingEntity> filter = (LivingEntity e) ->
                e.isAlive() && e != mob && mob.canAttack(e);
        List<LivingEntity> victims = mob.level().getEntitiesOfClass(LivingEntity.class, box, filter);
        for (LivingEntity v : victims) mob.doHurtTarget(v);
    }

    private void refreshAttackTempos() {
        double scale = computeAttackSpeedScale();
        this.dynamicAttackDuration = Math.max(5, (int) Math.round(this.DURATION_TICKS * scale));
        if (this.DAMAGE_FRAMES.length > 0) {
            int[] scaled = new int[this.DAMAGE_FRAMES.length];
            int prev = 0;
            for (int i = 0; i < this.DAMAGE_FRAMES.length; i++) {
                int frame = Math.max(1, (int) Math.round(this.DAMAGE_FRAMES[i] * scale));
                int maxFrame = Math.max(1, this.dynamicAttackDuration - 1);
                if (frame >= maxFrame) {
                    frame = maxFrame;
                }
                if (i > 0 && frame <= prev) {
                    frame = prev + 1;
                }
                prev = frame;
                scaled[i] = frame;
            }
            this.dynamicDamageFrames = scaled;
        } else {
            this.dynamicDamageFrames = this.DAMAGE_FRAMES;
        }
        // Se escala el delay base (20 ticks por defecto si no se especifica)
        this.dynamicAttackDelayTicks = Math.max(1, (int) Math.round(20 * scale));
    }

    private double computeAttackSpeedScale() {
        AttributeInstance attr = mob.getAttribute(Attributes.ATTACK_SPEED);
        if (attr == null) {
            return 1.0D;
        }
        double current = attr.getValue();
        double base = mob.getAttributeBaseValue(Attributes.ATTACK_SPEED);
        if (current <= 0.0D || base <= 0.0D) {
            return 1.0D;
        }
        // Calcula la escala, usando la relación BASE / ACTUAL
        double ratio = base / current;
        return Mth.clamp(ratio, 0.3D, 3.0D);
    }

    // ===================== DEBUG HUD FUNCTIONS =====================

    // NEW: Helper para obtener al dueño (si existe y está cerca) para enviar el mensaje.
    private Player getOwnerPlayer() {
        if (mob.level().isClientSide || !(mob instanceof TamableAnimal tamable)) return null;
        if (!tamable.isTame()) return null;

        UUID ownerUUID = tamable.getOwnerUUID();
        if (ownerUUID == null) return null;

        // Devuelve al dueño si está en el mundo
        return mob.level().getPlayerByUUID(ownerUUID);
    }

    private static String fmt(double d) { return String.format("%.2f", d); }

    private void renderDebugActionbar(LivingEntity target) {
        if (!DEBUG_HUD) return;
        Player owner = getOwnerPlayer();
        if (owner == null) return;

        int now = mob.tickCount;

        // === Parte fija/base ===
        double speed = computeAttackSpeedScale();
        AttributeInstance attr = mob.getAttribute(Attributes.ATTACK_SPEED);
        double currentAS = attr != null ? attr.getValue() : 1.0;

        String base = (active ? ("ATK_T:" + tick) : ("CD:" + ticksUntilNextAttack))
                + " | DUR:" + dynamicAttackDuration
                + " | DELAY:" + dynamicAttackDelayTicks
                + " | AS:" + fmt(currentAS)
                + " | Scale:" + fmt(speed)
                + " | RAGE:" + (speed < 1.0 ? "Y" : "N");

        // === Mensaje Sticky (Inicio/Fin de Ataque) ===
        String sticky = (now < dbgStickyUntilTick && !dbgStickyMsg.isEmpty())
                ? " | " + dbgStickyMsg
                : "";

        String line = base + sticky;

        boolean mustUpdate = (active != dbgPrevActive)
                || (now >= dbgNextSendTick)
                || (!line.equals(dbgCachedLine));

        if (mustUpdate) {
            owner.displayClientMessage(Component.literal(line), true);
            dbgCachedLine = line;
            dbgPrevActive = active;
            dbgNextSendTick = now + 5;
        }
    }

    private void debugHud(String msg) {
        if (!DEBUG_HUD) return;
        Player owner = getOwnerPlayer();
        if (owner == null) return;

        int now = mob.tickCount;

        int prio = 1;
        if (msg.startsWith("ATTACK START")) prio = 3;
        else if (msg.startsWith("ATTACK END")) prio = 2;

        boolean stickyActive = now < dbgStickyUntilTick;
        if (stickyActive && prio < dbgStickyPrio) return;
        if (dbgStickySetTick == now && prio < dbgStickyPrio) return;

        dbgStickyMsg = msg;
        dbgStickyPrio = prio;
        dbgStickyUntilTick = now + 40; // 2s
        dbgStickySetTick = now;
    }
}