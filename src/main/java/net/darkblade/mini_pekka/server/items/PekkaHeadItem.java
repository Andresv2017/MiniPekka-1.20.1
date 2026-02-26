package net.darkblade.mini_pekka.server.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.UUID;

public class PekkaHeadItem extends ModSkullItem {

    private static final double NETHERITE_HELMET_ARMOR = 3.0D;
    private static final double NETHERITE_HELMET_TOUGHNESS = 3.0D;
    private static final double NETHERITE_HELMET_KB = 0.1D;

    private static final UUID HELMET_ARMOR_UUID = UUID.fromString("a4d5e6f7-1234-5678-9abc-def012345678");
    private static final UUID HELMET_TOUGHNESS_UUID = UUID.fromString("b5e6f708-2345-6789-abcd-ef0123456789");
    private static final UUID HELMET_KB_UUID = UUID.fromString("c6f70819-3456-789a-bcde-f01234567890");

    public PekkaHeadItem(Block skull, Block wallSkull, Item.Properties properties, Direction direction) {
        super(skull, wallSkull, properties.stacksTo(1), direction);
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> base = super.getDefaultAttributeModifiers(slot);
        if (slot != EquipmentSlot.HEAD) return base;

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(base);

        builder.put(Attributes.ARMOR,
                new AttributeModifier(HELMET_ARMOR_UUID, "Armor modifier",
                        NETHERITE_HELMET_ARMOR, AttributeModifier.Operation.ADDITION));

        builder.put(Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(HELMET_TOUGHNESS_UUID, "Armor toughness",
                        NETHERITE_HELMET_TOUGHNESS, AttributeModifier.Operation.ADDITION));

        builder.put(Attributes.KNOCKBACK_RESISTANCE,
                new AttributeModifier(HELMET_KB_UUID, "Armor knockback resistance",
                        NETHERITE_HELMET_KB, AttributeModifier.Operation.ADDITION));

        return builder.build();
    }
}
