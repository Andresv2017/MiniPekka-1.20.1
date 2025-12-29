package net.darkblade.mini_pekka.server.items;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.darkblade.mini_pekka.server.entity.MPekkaEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MiniPekkaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, MiniPekkaMod.MODID);

    public static final RegistryObject<Item> MINI_PK_HEAD = ITEMS.register("mini_pk_head", () ->
            new MiniPkHeadItem(
                    ModBlocks.MINI_PK_HEAD.get(),
                    ModBlocks.MINI_PK_WALL_HEAD.get(),
                    new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant(),
                    Direction.DOWN
            )
    );

    public static final FoodProperties PANCAKE_FOOD = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.6f)
            .build();

    public static final RegistryObject<Item> MP_SPAWN_EGG = ITEMS.register("mk_spawn_egg",
            () -> new ForgeSpawnEggItem(MPekkaEntities.MPEKKA, 0x566784, 0x70d4f0, new Item.Properties()));

    public static final RegistryObject<Item> PANCAKE = ITEMS.register("pancake",
            () -> new PancakeItem(new Item.Properties().food(PANCAKE_FOOD)));

    public static final RegistryObject<Item> PK_SWORD = ITEMS.register("pk_sword",
            () -> new PkSwordItem(Tiers.NETHERITE, 3.5F, -2.4F,
                    new Item.Properties().fireResistant().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> RAGE_POTION = ITEMS.register("rage_potion",
            () -> new RagePotionItem(new Item.Properties().stacksTo(1)));
}
