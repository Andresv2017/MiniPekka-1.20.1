package net.darkblade.mini_pekka.server.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;


public class PkSwordItem extends SwordItem {

    private final Multimap<Attribute, AttributeModifier> customModifiers;


    public PkSwordItem(Tier pTier, float pAttackDamageBonus, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, 0, pAttackSpeedModifier, pProperties);


        float totalAttackDamage = pAttackDamageBonus + pTier.getAttackDamageBonus();


        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();


        builder.put(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", totalAttackDamage, AttributeModifier.Operation.ADDITION)
        );


        builder.put(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", pAttackSpeedModifier, AttributeModifier.Operation.ADDITION)
        );

        this.customModifiers = builder.build();
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.customModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }
}