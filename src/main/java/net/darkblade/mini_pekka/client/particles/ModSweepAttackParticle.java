package net.darkblade.mini_pekka.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModSweepAttackParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected ModSweepAttackParticle(ClientLevel level, double x, double y, double z, double scale, SpriteSet sprites) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.sprites = sprites;
        // 8 ticks de vida para que coincida con tus 8 frames (0 al 7)
        this.lifetime = 8;
        // Tamaño de la partícula (ajústalo si el swipe se ve muy pequeño o muy grande)
        this.quadSize = 1.5F * (float) scale;

        // Colores al máximo para que la textura original se respete
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        // Avanza la animación o elimina la partícula si terminó
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        // PARTICLE_SHEET_LIT ignora las sombras del mundo, ideal para ataques mágicos/brillantes
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            // Usamos el xSpeed como un modificador de escala opcional (si es 0, la escala es 1.0)
            double scale = (xSpeed == 0.0D) ? 1.0D : xSpeed;
            return new ModSweepAttackParticle(level, x, y, z, scale, this.sprites);
        }
    }
}