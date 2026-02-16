package net.darkblade.mini_pekka;
import com.mojang.logging.LogUtils;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.darkblade.mini_pekka.client.particles.RageParticle;
import net.darkblade.mini_pekka.client.particles.ModParticles;
import net.darkblade.mini_pekka.constants.CRConstans;
import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.darkblade.mini_pekka.client.render.MiniPekkaRenderer;
import net.darkblade.mini_pekka.server.effect.ModEffects;
import net.darkblade.mini_pekka.server.entity.MPekkaEntities;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.darkblade.mini_pekka.server.items.ModCreativeModeTabs;
import net.darkblade.mini_pekka.server.items.ModItems;
import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.slf4j.Logger;
import software.bernie.geckolib.core.molang.LazyVariable;
import software.bernie.geckolib.core.molang.MolangParser;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

@Mod(MiniPekkaMod.MODID)
public class MiniPekkaMod
{

    public static final String MODID = "mpekka";

    private static final Logger LOGGER = LogUtils.getLogger();

    public MiniPekkaMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        ModParticles.register(modEventBus);
        ModEffects.register(modEventBus);

        modEventBus.addListener(this::onEntityAttributeCreation);

        MPekkaEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        MolangParser.INSTANCE.register(new LazyVariable(CRConstans.HEAD_X_QUERY, 0));
        MolangParser.INSTANCE.register(new LazyVariable(CRConstans.HEAD_Y_QUERY, 0));

        modEventBus.addListener(this::addCreative);

    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                Field f = ObfuscationReflectionHelper.findField(BlockEntityType.class, "validBlocks");
                @SuppressWarnings("unchecked")
                Set<net.minecraft.world.level.block.Block> oldSet =
                        (Set<net.minecraft.world.level.block.Block>) f.get(BlockEntityType.SKULL);

                Set<net.minecraft.world.level.block.Block> newSet = new LinkedHashSet<>(oldSet);
                newSet.add(net.darkblade.mini_pekka.server.block.ModBlocks.MINI_PK_HEAD.get());
                newSet.add(net.darkblade.mini_pekka.server.block.ModBlocks.MINI_PK_WALL_HEAD.get());

                f.set(BlockEntityType.SKULL, newSet);
            } catch (Throwable t) {
                LOGGER.error("[mpekka] Could not replace SKULL validBlocks", t);
            }

            ItemStack awkwardPotion = new ItemStack(Items.POTION);
            PotionUtils.setPotion(awkwardPotion, Potions.AWKWARD);

            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(awkwardPotion),
                    Ingredient.of(Items.AMETHYST_SHARD),
                    new ItemStack(ModItems.RAGE_POTION.get())
            ));
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    private void onEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(MPekkaEntities.MPEKKA.get(), MiniPekka.createAttributes().build());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.RAGE_AURA.get(), RageParticle.Provider::new);
        }

        @SubscribeEvent
        public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {

        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(MPekkaEntities.MPEKKA.get(), MiniPekkaRenderer::new);
            event.enqueueWork(() -> {
                EntityRenderers.register(
                        MPekkaEntities.RAGE_POTION_PROJECTILE.get(),
                        (context) -> new ThrownItemRenderer<>(context, 1.0F, true)
                );
            });
        }

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event)
        {

        }
    }
}
