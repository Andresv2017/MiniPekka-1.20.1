package net.darkblade.mini_pekka;

import com.mojang.logging.LogUtils;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.darkblade.mini_pekka.client.particles.ModParticles;
import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.darkblade.mini_pekka.server.effect.ModEffects;
import net.darkblade.mini_pekka.server.entity.MPekkaEntities;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.darkblade.mini_pekka.server.entity.Pekka;
import net.darkblade.mini_pekka.server.items.ModCreativeModeTabs;
import net.darkblade.mini_pekka.server.items.ModDataComponents;
import net.darkblade.mini_pekka.server.items.ModItems;
import net.darkblade.mini_pekka.server.loot.ModLootModifiers;
import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.slf4j.Logger;

@Mod(MiniPekkaMod.MODID)
public class MiniPekkaMod {

    public static final String MODID = "mpekka";

    private static final Logger LOGGER = LogUtils.getLogger();

    public MiniPekkaMod(IEventBus modEventBus) {
        ModParticles.register(modEventBus);
        ModEffects.register(modEventBus);
        MPekkaEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModDataComponents.register(modEventBus);

        modEventBus.addListener(this::onEntityAttributeCreation);
        modEventBus.addListener(this::onAddBlocksToBlockEntity);

        NeoForge.EVENT_BUS.addListener(this::onRegisterBrewingRecipes);
    }

    private void onEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(MPekkaEntities.MPEKKA.get(), MiniPekka.createAttributes().build());
        event.put(MPekkaEntities.PEKKA.get(), Pekka.createAttributes().build());
    }

    private void onAddBlocksToBlockEntity(final BlockEntityTypeAddBlocksEvent event) {
        // Make the Mini P.E.K.K.A heads valid for the vanilla SKULL block-entity type.
        event.modify(BlockEntityType.SKULL,
                ModBlocks.MINI_PK_HEAD.get(),
                ModBlocks.MINI_PK_WALL_HEAD.get());
    }

    private void onRegisterBrewingRecipes(final RegisterBrewingRecipesEvent event) {
        ItemStack awkwardPotion = PotionContents.createItemStack(Items.POTION, Potions.AWKWARD);
        event.getBuilder().addRecipe(
                Ingredient.of(awkwardPotion),
                Ingredient.of(Items.AMETHYST_SHARD),
                new ItemStack(ModItems.RAGE_POTION.get()));
    }
}
