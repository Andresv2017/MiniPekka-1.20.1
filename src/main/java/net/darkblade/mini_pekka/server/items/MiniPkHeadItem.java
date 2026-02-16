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

public class MiniPkHeadItem extends ModSkullItem {

    private static final double NETHERITE_HELMET_ARMOR = 3.0D;
    private static final double NETHERITE_HELMET_TOUGHNESS = 3.0D;
    private static final double NETHERITE_HELMET_KB = 0.1D;

    private static final UUID HELMET_ARMOR_UUID = UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150");
    private static final UUID HELMET_TOUGHNESS_UUID = UUID.fromString("3BD4F357-0FF2-4F78-A997-7A0E491CC261");
    private static final UUID HELMET_KB_UUID = UUID.fromString("4CE50468-1003-4089-BA08-8B1F5A2DD372");


    public MiniPkHeadItem(Block skull, Block wallSkull, Item.Properties properties, Direction direction) {
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
