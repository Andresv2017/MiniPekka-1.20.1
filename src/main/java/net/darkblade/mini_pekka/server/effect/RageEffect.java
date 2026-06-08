package net.darkblade.mini_pekka.server.effect;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RageEffect extends MobEffect {

    public RageEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xAA00FF);

        // Movement speed boost (applies to every affected entity, managed by the effect engine).
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "rage_movement_speed"),
                0.35D,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        // Attack damage boost (applies to every affected entity, managed by the effect engine).
        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "rage_attack_damage"),
                3.0D,
                AttributeModifier.Operation.ADD_VALUE
        );
    }
}
