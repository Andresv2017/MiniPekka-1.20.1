package net.darkblade.mini_pekka.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class ModSweepAttackParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float rotYaw;
    private final int attackType;

    protected ModSweepAttackParticle(ClientLevel level, double x, double y, double z, double yaw, double scale, double type, SpriteSet sprites) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.sprites = sprites;
        this.rotYaw = (float) yaw;
        this.attackType = (int) Math.round(type);
        this.lifetime = 7;

        this.quadSize = (float) scale;
        if (this.quadSize <= 0.0f) this.quadSize = 1.5f;

        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float renderX = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float renderY = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float renderZ = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());

        Quaternionf quaternionf = new Quaternionf();

        quaternionf.rotationY(-this.rotYaw * ((float) Math.PI / 180F));

        if (this.attackType == 0 || this.attackType == 1) {
            quaternionf.rotateX((float) Math.PI / 2F);

        } else if (this.attackType == 2) {
            quaternionf.rotateY((float) -Math.PI / 2F);
            quaternionf.rotateZ((float) Math.PI / 2F);
        }

        Vector3f[] avector3f = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };

        float currentSize = this.getQuadSize(partialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternionf);
            vector3f.mul(currentSize);
            vector3f.add(renderX, renderY, renderZ);
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        if (this.attackType == 1) {
            float temp = u0;
            u0 = u1;
            u1 = temp;

        } else if (this.attackType == 2) {
            float tempV = v0;
            v0 = v1;
            v1 = tempV;

            float tempU = u0;
            u0 = u1;
            u1 = tempU;
        }

        int light = this.getLightColor(partialTicks);

        vertexConsumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        vertexConsumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        vertexConsumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        vertexConsumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();

        vertexConsumer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        vertexConsumer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        vertexConsumer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        vertexConsumer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
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
            return new ModSweepAttackParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}