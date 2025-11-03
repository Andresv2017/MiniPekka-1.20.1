package net.darkblade.mini_pekka.server.entity.debug;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class DebugAABB {
    private DebugAABB() {}

    /** Número de puntos por arista (más alto = líneas más “continuas”). */
    public static int EDGE_STEPS = 12;

    /** Dibuja las 12 aristas del AABB usando partículas END_ROD. (Server-side) */
    public static void drawAabbEdges(Level level, AABB box) {
        if (level.isClientSide()) return;
        ServerLevel sl = (ServerLevel) level;

        Vec3[] c = new Vec3[]{
                new Vec3(box.minX, box.minY, box.minZ), // 0
                new Vec3(box.maxX, box.minY, box.minZ), // 1
                new Vec3(box.minX, box.minY, box.maxZ), // 2
                new Vec3(box.maxX, box.minY, box.maxZ), // 3
                new Vec3(box.minX, box.maxY, box.minZ), // 4
                new Vec3(box.maxX, box.maxY, box.minZ), // 5
                new Vec3(box.minX, box.maxY, box.maxZ), // 6
                new Vec3(box.maxX, box.maxY, box.maxZ)  // 7
        };

        int[][] edges = new int[][]{
                {0,1},{0,2},{1,3},{2,3},   // base inferior
                {4,5},{4,6},{5,7},{6,7},   // base superior
                {0,4},{1,5},{2,6},{3,7}    // columnas
        };

        for (int[] e : edges) {
            drawLine(sl, c[e[0]], c[e[1]]);
        }
    }

    /** Dibuja una línea de a->b con partículas. */
    public static void drawLine(ServerLevel sl, Vec3 a, Vec3 b) {
        for (int i = 0; i <= EDGE_STEPS; i++) {
            double t = i / (double) EDGE_STEPS;
            Vec3 p = a.lerp(b, t);
            sl.sendParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0, 0, 0, 0);
        }
    }
}
