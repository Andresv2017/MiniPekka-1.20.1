package net.darkblade.mini_pekka.events;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.client.ModBlockEntities;
import net.darkblade.mini_pekka.client.ModBlockEntityModelLayers;
import net.darkblade.mini_pekka.client.ModSkullBlockRenderer;
import net.darkblade.mini_pekka.client.model.MiniPekkaHeadModel;
import net.darkblade.mini_pekka.client.model.PekkaHeadModel;
import net.darkblade.mini_pekka.client.particles.ArrowUpParticle;
import net.darkblade.mini_pekka.client.particles.ElixirDropParticle;
import net.darkblade.mini_pekka.client.particles.FallingStarParticle;
import net.darkblade.mini_pekka.client.particles.ModParticles;
import net.darkblade.mini_pekka.client.particles.ModSweepAttackParticle;
import net.darkblade.mini_pekka.client.particles.RageParticle;
import net.darkblade.mini_pekka.client.particles.SpatulaParticle;
import net.darkblade.mini_pekka.client.render.ButterflyRenderer;
import net.darkblade.mini_pekka.client.render.MiniPekkaRenderer;
import net.darkblade.mini_pekka.client.render.PekkaRenderer;
import net.darkblade.mini_pekka.server.block.ModSkullBlock;
import net.darkblade.mini_pekka.server.entity.MPekkaEntities;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = MiniPekkaMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MPekkaEntities.MPEKKA.get(), MiniPekkaRenderer::new);
        event.registerEntityRenderer(MPekkaEntities.PEKKA.get(), PekkaRenderer::new);
        event.registerEntityRenderer(MPekkaEntities.BUTTERFLY.get(), ButterflyRenderer::new);
        event.registerEntityRenderer(MPekkaEntities.RAGE_POTION_PROJECTILE.get(),
                context -> new ThrownItemRenderer<>(context, 1.0F, true));

        event.registerBlockEntityRenderer(ModBlockEntities.EFFECT_SKULL.get(), ModSkullBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModBlockEntityModelLayers.MINI_PK_HEAD, MiniPekkaHeadModel::createMiniPekkaHeadLayer);
        event.registerLayerDefinition(ModBlockEntityModelLayers.PEKKA_HEAD, PekkaHeadModel::createPekkaHeadLayer);
    }

    @SubscribeEvent
    public static void onCreateSkullModels(EntityRenderersEvent.CreateSkullModels event) {
        var bakedMini = event.getEntityModelSet().bakeLayer(ModBlockEntityModelLayers.MINI_PK_HEAD);
        event.registerSkullModel(ModSkullBlock.Types.MINI_PEKKA, new SkullModel(bakedMini));

        var bakedPekka = event.getEntityModelSet().bakeLayer(ModBlockEntityModelLayers.PEKKA_HEAD);
        event.registerSkullModel(ModSkullBlock.Types.PEKKA, new SkullModel(bakedPekka));
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.RAGE_AURA.get(), RageParticle.Provider::new);
        event.registerSpriteSet(ModParticles.ARROW_UP.get(), ArrowUpParticle.Provider::new);
        event.registerSpriteSet(ModParticles.SPATULA.get(), SpatulaParticle.Provider::new);
        event.registerSpriteSet(ModParticles.ELIXIR_DROP.get(), ElixirDropParticle.Provider::new);
        event.registerSpriteSet(ModParticles.STAR_PARTICLE.get(), FallingStarParticle.Provider::new);
        event.registerSpriteSet(ModParticles.STAR_PARTICLE_EVO.get(), FallingStarParticle.Provider::new);
        event.registerSpriteSet(ModParticles.SWEEP_EVO.get(), ModSweepAttackParticle.Provider::new);
        event.registerSpriteSet(ModParticles.SWEEP_HERO.get(), ModSweepAttackParticle.Provider::new);
        event.registerSpriteSet(ModParticles.STAR_PEKKA_ABILITY.get(), FallingStarParticle.Provider::new);
    }
}
