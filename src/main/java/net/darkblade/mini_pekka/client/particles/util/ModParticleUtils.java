package net.darkblade.mini_pekka.client.particles.util;

import net.darkblade.mini_pekka.client.particles.ModParticles;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ModParticleUtils {

    // Color Rage exacto: R=0.7, G=0.3, B=1.0
    private static final Vector3f RAGE_COLOR = new Vector3f(0.7F, 0.3F, 1.0F);

    public static void spawnRageAura(ServerLevel serverLevel, Vec3 position) {
        double x = position.x();
        double y = position.y() + 0.05;
        double z = position.z();

        // 1. TU LÓGICA ORIGINAL (MANTENIDA)
        // Lanza tu partícula custom RAGE_AURA
        serverLevel.sendParticles(
                ModParticles.RAGE_AURA.get(),
                x, y, z,
                1,
                0, 0, 0,
                0.0F
        );

        // 2. NUEVA LÓGICA (AGREGADA)
        // Colorea el piso con polvo de Redstone modificado al color de la rabia
        spawnRageFloorDust(serverLevel, position);
    }

    private static void spawnRageFloorDust(ServerLevel serverLevel, Vec3 position) {
        // CAMBIO 1: Aumentamos el tamaño de 1.0F a 3.0F para que sean más visibles
        DustParticleOptions rageDust = new DustParticleOptions(RAGE_COLOR, 3.0F);

        // CAMBIO 2: Aumentamos la cantidad de 50 a 400 para rellenar los huecos
        int particleCount = 400;
        double radius = 3.0D;

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