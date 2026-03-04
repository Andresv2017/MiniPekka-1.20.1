package net.darkblade.mini_pekka.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class ElixirDropParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float initialQuadSize;

    protected ElixirDropParticle(ClientLevel level, double x, double y, double z,
                                  double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;

        this.xd = xSpeed + (random.nextDouble() - 0.5) * 0.06;
        this.yd = ySpeed + random.nextDouble() * 0.04;
        this.zd = zSpeed + (random.nextDouble() - 0.5) * 0.06;

        this.gravity = 0.6F;
        this.friction = 0.92F;

        this.quadSize *= 1.8F + random.nextFloat() * 0.5F;
        this.initialQuadSize = this.quadSize;

        this.lifetime = 30;

        this.rCol = 0.85F;
        this.gCol = 0.2F;
        this.bCol = 0.95F;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);

        float remaining = (float)(this.lifetime - this.age) / 10.0F;
        if (remaining < 1.0F) {
            this.alpha = Math.max(0.0F, remaining);
            this.quadSize = this.initialQuadSize * (0.5F + remaining * 0.5F);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new ElixirDropParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}
