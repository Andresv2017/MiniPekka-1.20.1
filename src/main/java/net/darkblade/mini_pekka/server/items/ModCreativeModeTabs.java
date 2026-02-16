package net.darkblade.mini_pekka.server.items;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MiniPekkaMod.MODID);

    public static final RegistryObject<CreativeModeTab> MPEKKA_TAB = CREATIVE_MODE_TABS.register("mpekka_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.MINI_PK_HEAD.get()))
                    .title(Component.translatable("creativetab.mpekka_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.MINI_PK_HEAD.get());
                        output.accept(ModItems.PK_SWORD.get());
                        output.accept(ModItems.PANCAKE.get());
                        output.accept(ModItems.RAGE_POTION.get());
                        output.accept(ModItems.MP_SPAWN_EGG.get());
                        output.accept(ModItems.STAR_ITEM.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}