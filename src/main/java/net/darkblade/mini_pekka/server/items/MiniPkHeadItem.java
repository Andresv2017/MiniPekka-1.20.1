package net.darkblade.mini_pekka.server.items;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;

public class MiniPkHeadItem extends ModSkullItem {

    private static final double NETHERITE_HELMET_ARMOR = 3.0D;
    private static final double NETHERITE_HELMET_TOUGHNESS = 3.0D;
    private static final double NETHERITE_HELMET_KB = 0.1D;

    public MiniPkHeadItem(Block skull, Block wallSkull, Item.Properties properties, Direction direction) {
        super(skull, wallSkull, properties.stacksTo(1), direction);
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "mini_pk_head_armor"),
                                NETHERITE_HELMET_ARMOR, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.ARMOR_TOUGHNESS,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "mini_pk_head_toughness"),
                                NETHERITE_HELMET_TOUGHNESS, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(MiniPekkaMod.MODID, "mini_pk_head_kb"),
                                NETHERITE_HELMET_KB, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .build();
    }
}
