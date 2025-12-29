package net.darkblade.mini_pekka.client.particles.util;

import net.darkblade.mini_pekka.client.particles.ModParticles;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ModParticleUtils {

    private static final Vector3f RAGE_COLOR = new Vector3f(0.7F, 0.3F, 1.0F);

    public static void spawnRageAura(ServerLevel serverLevel, Vec3 position, double radius) {
        double x = position.x();
        double y = position.y() + 0.05;
        double z = position.z();

        serverLevel.sendParticles(
                ModParticles.RAGE_AURA.get(),
                x, y, z,
                1,
                0, 0, 0,
                0.0F
        );

        spawnRageFloorDust(serverLevel, position, radius);
    }

    private static void spawnRageFloorDust(ServerLevel serverLevel, Vec3 position, double radius) {
        DustParticleOptions rageDust = new DustParticleOptions(RAGE_COLOR, 3.0F);

        int particleCount = (int) (radius * radius * 12);

        for (int i = 0; i < particleCount; i++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double distance = Math.sqrt(serverLevel.random.nextDouble()) * radius;

            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;

            double px = position.x() + offsetX;
            double py = position.y() + 0.05D;
            double pz = position.z() + offsetZ;

            serverLevel.sendParticles(
                    rageDust,
                    px, py, pz,
                    1,
                    0.0, 0.0, 0.0,
                    0.0
            );
        }
    }
}