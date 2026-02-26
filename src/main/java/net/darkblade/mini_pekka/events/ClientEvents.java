package net.darkblade.mini_pekka.events;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.client.ModSkullBlockRenderer;
import net.darkblade.mini_pekka.client.ModSkullHeadLayer;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.darkblade.mini_pekka.client.ModBlockEntityModelLayers;
import net.darkblade.mini_pekka.client.model.MiniPekkaHeadModel;
import net.darkblade.mini_pekka.client.model.PekkaHeadModel;
import net.darkblade.mini_pekka.server.block.ModSkullBlock;
import net.darkblade.mini_pekka.server.items.ModSkullItem;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = MiniPekkaMod.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        // ¡ELIMINAMOS renderHeadPre y renderHeadPost que ocultaban la cabeza!
        // Ahora tu cabeza base siempre será visible.

        @Mod.EventBusSubscriber(modid = MiniPekkaMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
        public static class ClientModBusEvents {

            @SubscribeEvent
            public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
                event.registerBlockEntityRenderer(ModBlockEntities.EFFECT_SKULL.get(), ModSkullBlockRenderer::new);
            }

            @SubscribeEvent
            public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
                event.registerLayerDefinition(ModBlockEntityModelLayers.MINI_PK_HEAD, MiniPekkaHeadModel::createMiniPekkaHeadLayer);
                event.registerLayerDefinition(ModBlockEntityModelLayers.PEKKA_HEAD, PekkaHeadModel::createPekkaHeadLayer);
            }

            @SubscribeEvent
            public static void onCreateSkullModels(EntityRenderersEvent.CreateSkullModels event) {
                var bakedMini = event.getEntityModelSet().bakeLayer(ModBlockEntityModelLayers.MINI_PK_HEAD);
                event.registerSkullModel(
                        ModSkullBlock.Types.MINI_PEKKA,
                        new net.minecraft.client.model.SkullModel(bakedMini)
                );

                var bakedPekka = event.getEntityModelSet().bakeLayer(ModBlockEntityModelLayers.PEKKA_HEAD);
                event.registerSkullModel(
                        ModSkullBlock.Types.PEKKA,
                        new net.minecraft.client.model.SkullModel(bakedPekka)
                );
            }

            @SubscribeEvent(priority = EventPriority.LOWEST)
            public static void registerEffectSkullHeadLayers(final EntityRenderersEvent.AddLayers event) {
                // (Mantenemos esto vacío/comentado como lo dejamos antes
                // para que ModSkullHeadLayer no te estampe el bloque gigante)
            }
        }
    }
}