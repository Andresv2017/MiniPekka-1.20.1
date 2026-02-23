package net.darkblade.mini_pekka.events;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.darkblade.mini_pekka.server.entity.MPekkaEntities;
import net.darkblade.mini_pekka.server.entity.Pekka;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MiniPekkaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PekkaSpawnHandler {

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getLevel() instanceof ServerLevel server)) return;

        BlockState placed = event.getPlacedBlock();
        BlockPos placedPos = event.getPos();

        if (!placed.is(ModBlocks.MINI_PK_HEAD.get())) return;

        BlockPos belowHead = placedPos.below();
        if (!server.getBlockState(belowHead).is(Blocks.IRON_BLOCK)) return;

        if (trySpawnPekka(server, placedPos, belowHead, belowHead.east(), belowHead.west(), belowHead.below(), event)) return;
        if (trySpawnPekka(server, placedPos, belowHead, belowHead.north(), belowHead.south(), belowHead.below(), event)) return;


        if (tryAsArm(server, placedPos, belowHead, 1, 0, event)) return;
        if (tryAsArm(server, placedPos, belowHead, -1, 0, event)) return;
        if (tryAsArm(server, placedPos, belowHead, 0, 1, event)) return;
        if (tryAsArm(server, placedPos, belowHead, 0, -1, event)) return;
    }


    private static boolean tryAsArm(ServerLevel server, BlockPos headPos, BlockPos armPos,
                                     int dx, int dz, BlockEvent.EntityPlaceEvent event) {
        BlockPos center = armPos.offset(-dx, 0, -dz);
        BlockPos otherArm = center.offset(-dx, 0, -dz);
        BlockPos body = center.below();
        return trySpawnPekka(server, headPos, center, armPos, otherArm, body, event);
    }

    private static boolean trySpawnPekka(ServerLevel server, BlockPos headPos,
                                          BlockPos center, BlockPos arm1, BlockPos arm2,
                                          BlockPos body, BlockEvent.EntityPlaceEvent event) {
        if (!server.getBlockState(center).is(Blocks.IRON_BLOCK)) return false;
        if (!server.getBlockState(arm1).is(Blocks.IRON_BLOCK)) return false;
        if (!server.getBlockState(arm2).is(Blocks.IRON_BLOCK)) return false;
        if (!server.getBlockState(body).is(Blocks.IRON_BLOCK)) return false;

        if (!headPos.equals(center.above())) return false;

        Pekka pekka = MPekkaEntities.PEKKA.get().create(server);
        if (pekka == null) return false;

        double sx = center.getX() + 0.5D;
        double sy = body.getY();
        double sz = center.getZ() + 0.5D;
        pekka.moveTo(sx, sy, sz, server.random.nextFloat() * 360F, 0F);

        if (event.getEntity() instanceof Player player) {
            try {
                pekka.tame(player);
            } catch (Throwable ignore) {
                pekka.setTame(true);
                pekka.setOwnerUUID(player.getUUID());
            }
        }

        pekka.setPersistenceRequired();
        var max = pekka.getAttribute(Attributes.MAX_HEALTH);
        if (max != null) pekka.setHealth((float) max.getValue());

        BlockState headState = server.getBlockState(headPos);
        BlockState centerState = server.getBlockState(center);
        BlockState arm1State = server.getBlockState(arm1);
        BlockState arm2State = server.getBlockState(arm2);
        BlockState bodyState = server.getBlockState(body);

        server.removeBlock(headPos, false);
        server.removeBlock(arm1, false);
        server.removeBlock(arm2, false);
        server.removeBlock(center, false);
        server.removeBlock(body, false);

        if (!server.addFreshEntity(pekka)) return false;

        server.playSound(null, sx, sy, sz, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.0f, 0.7f);
        server.playSound(null, sx, sy, sz, SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1.0f, 0.6f);
        server.sendParticles(ParticleTypes.EXPLOSION, sx, sy + 1.0D, sz, 5, 0.5D, 0.5D, 0.5D, 0.0D);
        server.sendParticles(ParticleTypes.ENCHANT, sx, sy + 1.0D, sz, 40, 0.8D, 1.0D, 0.8D, 0.0D);

        server.levelEvent(2001, headPos, Block.getId(headState));
        server.levelEvent(2001, center, Block.getId(centerState));
        server.levelEvent(2001, arm1, Block.getId(arm1State));
        server.levelEvent(2001, arm2, Block.getId(arm2State));
        server.levelEvent(2001, body, Block.getId(bodyState));

        return true;
    }
}
