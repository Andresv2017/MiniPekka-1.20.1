package net.darkblade.mini_pekka;
import com.mojang.logging.LogUtils;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.darkblade.mini_pekka.client.render.MiniPekkaRenderer;
import net.darkblade.mini_pekka.server.entity.MPekkaEntities;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.darkblade.mini_pekka.server.items.ModItems;
import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
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

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MiniPekkaMod.MODID)
public class MiniPekkaMod
{

    public static final String MODID = "mpekka";

    private static final Logger LOGGER = LogUtils.getLogger();

    public MiniPekkaMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        MPekkaEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);

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

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {

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
        }

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event)
        {
            event.put(MPekkaEntities.MPEKKA.get(), MiniPekka.createAttributes().build());

        }
    }
}
