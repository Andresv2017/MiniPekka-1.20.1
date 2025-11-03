package net.darkblade.mini_pekka.server.items;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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
                    new Item.Properties().rarity(Rarity.UNCOMMON),
                    Direction.DOWN
            )
    );

    public static final FoodProperties PANCAKE_FOOD = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.6f)
            .build();

    public static final RegistryObject<Item> PANCAKE = ITEMS.register("pancake",
            () -> new PancakeItem(new Item.Properties().food(PANCAKE_FOOD)));

    @SubscribeEvent
    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            e.accept(MINI_PK_HEAD);
        }
        if (e.getTabKey().equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            e.accept(PANCAKE);
        }
    }
}
