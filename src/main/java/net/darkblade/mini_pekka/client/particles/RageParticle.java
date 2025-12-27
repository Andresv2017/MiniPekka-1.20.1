package net.darkblade.mini_pekka.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RageParticle extends FloorParticle {

    private final SpriteSet sprites;
    private final float maxSize;

    protected RageParticle(ClientLevel level,
                           double x, double y, double z,
                           SpriteSet sprites,
                           float maxSize) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.maxSize = maxSize;

        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;

        this.alpha = 1.0F;
        this.quadSize = 0.0F;
        this.lifetime = 90;

        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.quadSize = Mth.lerp(0.1F, this.quadSize, this.maxSize);

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else if (this.age > (this.lifetime / 2)) {
            float fadeStep = 1.0F / (this.lifetime / 2.0F);
            this.alpha -= fadeStep;
            if (this.alpha < 0.0F) {
                this.alpha = 0.0F;
            }
        }

        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float delta) {
        this.renderRotatedParticle(consumer, camera, delta, false, 0.0F);
    }

    @Override
    protected int getLightColor(float partialTicks) {
        return 240;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private static final float AURA_DIAMETER = 10.0F;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type,
                                       ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            return new RageParticle(level, x, y, z, this.sprites, AURA_DIAMETER);
        }
    }
}
