package net.darkblade.mini_pekka.events;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.client.EffectSkullBlockRenderer;
import net.darkblade.mini_pekka.client.EffectSkullHeadLayer;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.darkblade.mini_pekka.client.ModBlockEntityModelLayers;
import net.darkblade.mini_pekka.client.model.MiniPekkaHeadModel;
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

        @SuppressWarnings({"unchecked", "rawtypes"})
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void registerEffectSkullHeadLayers(final EntityRenderersEvent.AddLayers event) {
            Map<EntityType<?>, EntityRenderer<?>> renderers = Minecraft.getInstance().getEntityRenderDispatcher().renderers;
            for (Map.Entry<EntityType<?>, EntityRenderer<?>> renderer : renderers.entrySet()) {
                if (renderer.getValue() instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
                    boolean flag = false;
                    for (RenderLayer<?, ?> layer : livingEntityRenderer.layers) {
                        if (layer instanceof CustomHeadLayer customHeadLayer) {
                            flag = true;
                            customHeadLayer.skullModels = EffectSkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels());
                        }
                    }
                    if (flag) {
                        livingEntityRenderer.addLayer(new EffectSkullHeadLayer(livingEntityRenderer, Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer()));
                    }
                }
            }

            Map<String, EntityRenderer<? extends Player>> skins = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
            for (Map.Entry<String, EntityRenderer<? extends Player>> renderer : skins.entrySet()) {
                if (renderer.getValue() instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
                    boolean flag = false;
                    for (RenderLayer<?, ?> layer : livingEntityRenderer.layers) {
                        if (layer instanceof CustomHeadLayer customHeadLayer) {
                            flag = true;
                            customHeadLayer.skullModels = EffectSkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels());
                        }
                    }
                    if (flag) {
                        livingEntityRenderer.addLayer(new EffectSkullHeadLayer(livingEntityRenderer, Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer()));
                    }
                }
            }
        }
    }
}