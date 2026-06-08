package net.darkblade.mini_pekka.server.items;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.darkblade.mini_pekka.server.entity.MPekkaEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, MiniPekkaMod.MODID);

    public static final DeferredHolder<Item, Item> MINI_PK_HEAD = ITEMS.register("mini_pk_head", () ->
            new MiniPkHeadItem(
                    ModBlocks.MINI_PK_HEAD.get(),
                    ModBlocks.MINI_PK_WALL_HEAD.get(),
                    new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()
                            .attributes(MiniPkHeadItem.createAttributes()),
                    Direction.DOWN
            )
    );

    public static final FoodProperties PANCAKE_FOOD = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.6f)
            .build();

    public static final DeferredHolder<Item, Item> MP_SPAWN_EGG = ITEMS.register("mk_spawn_egg",
            () -> new DeferredSpawnEggItem(MPekkaEntities.MPEKKA, 0x566784, 0x70d4f0, new Item.Properties()));

    public static final DeferredHolder<Item, Item> PANCAKE = ITEMS.register("pancake",
            () -> new PancakeItem(new Item.Properties().food(PANCAKE_FOOD)));

    public static final DeferredHolder<Item, Item> GOLDEN_SPATULA = ITEMS.register("golden_spatula",
            () -> new GoldenSpatulaItem(Tiers.GOLD, 3, -2.0F, new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> PEKKA_SWORD = ITEMS.register("pekka_sword",
            () -> new SwordItem(Tiers.NETHERITE,
                    new Item.Properties().stacksTo(1)
                            .attributes(SwordItem.createAttributes(Tiers.NETHERITE, 8, -3.3F))));

    public static final DeferredHolder<Item, Item> PK_SWORD = ITEMS.register("pk_sword",
            () -> new PkSwordItem(Tiers.NETHERITE, 3.5F, -2.4F,
                    new Item.Properties().fireResistant().rarity(Rarity.RARE)));

    public static final DeferredHolder<Item, Item> RAGE_POTION = ITEMS.register("rage_potion",
            () -> new RagePotionItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> STAR_ITEM = ITEMS.register("star_item",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> HERO_CRYSTAL = ITEMS.register("hero_crystal",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final DeferredHolder<Item, Item> EVO_CRYSTAL = ITEMS.register("evo_crystal",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final DeferredHolder<Item, Item> PEKKA_HEAD = ITEMS.register("pekka_head", () ->
            new PekkaHeadItem(
                    ModBlocks.PEKKA_HEAD.get(),
                    ModBlocks.PEKKA_WALL_HEAD.get(),
                    new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()
                            .attributes(PekkaHeadItem.createAttributes()),
                    Direction.DOWN
            )
    );

    public static final DeferredHolder<Item, Item> PEKKA_SPAWN_EGG = ITEMS.register("pekka_spawn_egg",
            () -> new DeferredSpawnEggItem(MPekkaEntities.PEKKA, 0x2d446a, 0xc363c5, new Item.Properties()));

    public static final DeferredHolder<Item, Item> EVO_FRAGMENT = ITEMS.register("evo_fragment",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> HERO_SHARDS_SMALL = ITEMS.register("hero_shards_small",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> HERO_SHARDS_MEDIUM = ITEMS.register("hero_shards_medium",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> HERO_SHARDS_BIG = ITEMS.register("hero_shards_big",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> HERO_BOX = ITEMS.register("hero_box",
            () -> new HeroBoxItem(new Item.Properties()));

    public static final DeferredHolder<Item, Item> CREATIVE_TAB_LOGO = ITEMS.register("creative_tab_logo",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
