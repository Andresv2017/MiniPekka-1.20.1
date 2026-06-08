package net.darkblade.mini_pekka.server.items;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class PkSwordItem extends SwordItem {

    public PkSwordItem(Tier tier, float attackDamageBonus, float attackSpeedModifier, Item.Properties properties) {
        super(tier, properties.attributes(buildModifiers(tier, attackDamageBonus, attackSpeedModifier)));
    }

    private static ItemAttributeModifiers buildModifiers(Tier tier, float attackDamageBonus, float attackSpeedModifier) {
        float totalAttackDamage = attackDamageBonus + tier.getAttackDamageBonus();
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, totalAttackDamage,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeedModifier,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }
}
