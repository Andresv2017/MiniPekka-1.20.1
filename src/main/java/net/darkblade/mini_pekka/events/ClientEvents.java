package net.darkblade.mini_pekka.events;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.client.EffectSkullBlockRenderer;
import net.darkblade.mini_pekka.client.EffectSkullHeadLayer;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.darkblade.mini_pekka.client.ModBlockEntityModelLayers;
import net.darkblade.mini_pekka.client.model.MiniPekkaHeadModel;
import net.darkblade.mini_pekka.server.block.EffectSkullBlock;
import net.darkblade.mini_pekka.server.items.EffectSkullItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.StrayClothingLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

public class ClientEvents {
    public static int time = 0;

    @Mod.EventBusSubscriber(modid = MiniPekkaMod.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void renderHeadPre(RenderLivingEvent.Pre<?, ?> event) {
            EntityModel<?> model = event.getRenderer().getModel();
            if (model instanceof HumanoidModel<?> humanoidModel) {
                if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof EffectSkullItem) {
                    humanoidModel.head.visible = false;
                    humanoidModel.hat.visible  = false;
                }
            }
        }

        @SubscribeEvent
        public static void renderHeadPost(RenderLivingEvent.Post<?, ?> event) {
            EntityModel<?> model = event.getRenderer().getModel();
            if (model instanceof HumanoidModel<?> humanoidModel) {
                if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof EffectSkullItem) {
                    humanoidModel.head.visible = true;
                    humanoidModel.hat.visible  = true;
                }
            }
        }


        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            Minecraft minecraft = Minecraft.getInstance();
            if (!minecraft.isPaused()) {
                time++;
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MiniPekkaMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.EFFECT_SKULL.get(), EffectSkullBlockRenderer::new);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ModBlockEntityModelLayers.MINI_PK_HEAD, MiniPekkaHeadModel::createMiniPekkaHeadLayer);
        }

        @SubscribeEvent
        public static void onCreateSkullModels(EntityRenderersEvent.CreateSkullModels event) {
            var baked = event.getEntityModelSet().bakeLayer(ModBlockEntityModelLayers.MINI_PK_HEAD);
            event.registerSkullModel(
                    EffectSkullBlock.Types.MINI_PEKKA,
                    new net.minecraft.client.model.SkullModel(baked)
            );
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void registerEffectSkullHeadLayers(final EntityRenderersEvent.AddLayers event) {
            var renderers = Minecraft.getInstance().getEntityRenderDispatcher().renderers;
            for (var e : renderers.entrySet()) {
                if (e.getValue() instanceof LivingEntityRenderer<?, ?> ler) {
                    boolean hasCustomHead = ler.layers.stream().anyMatch(l -> l instanceof CustomHeadLayer);
                    if (hasCustomHead) {
                        ler.addLayer(new EffectSkullHeadLayer(
                                ler,
                                Minecraft.getInstance().getEntityModels(),
                                Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer()
                        ));
                    }
                }
            }

            var skins = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
            for (var e : skins.entrySet()) {
                if (e.getValue() instanceof LivingEntityRenderer<?, ?> ler) {
                    boolean hasCustomHead = ler.layers.stream().anyMatch(l -> l instanceof CustomHeadLayer);
                    if (hasCustomHead) {
                        ler.addLayer(new EffectSkullHeadLayer(
                                ler,
                                Minecraft.getInstance().getEntityModels(),
                                Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer()
                        ));
                    }
                }
            }
        }
    }

}