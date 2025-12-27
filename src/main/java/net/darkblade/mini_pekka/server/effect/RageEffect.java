package net.darkblade.mini_pekka.server.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class RageEffect extends MobEffect {

    private static final UUID RAGE_DAMAGE_UUID =
            UUID.fromString("4C1E0C63-6A1E-4FBF-BD1F-9A8E0FAE11AA");

    public RageEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xAA00FF);

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "91AEAA56-F61E-47C0-B7DB-3932F215F1C6",
                0.35D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && !player.level().isClientSide) {
            AttributeInstance attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackAttr != null) {
                if (attackAttr.getModifier(RAGE_DAMAGE_UUID) == null) {
                    double baseBonus = 3.0D;
                    double scaledBonus = baseBonus + amplifier;

                    attackAttr.addTransientModifier(new AttributeModifier(
                            RAGE_DAMAGE_UUID,
                            "rage_damage_boost",
                            scaledBonus,
                            AttributeModifier.Operation.ADDITION
                    ));
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);

        if (entity instanceof Player player) {
            AttributeInstance attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackAttr != null && attackAttr.getModifier(RAGE_DAMAGE_UUID) != null) {
                attackAttr.removeModifier(RAGE_DAMAGE_UUID);
            }
        }
    }
}
