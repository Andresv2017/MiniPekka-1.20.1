package net.darkblade.mini_pekka.server.items;

import net.darkblade.mini_pekka.server.block.ModBlocks;
import net.darkblade.mini_pekka.server.entity.MPekkaEntities;
import net.darkblade.mini_pekka.server.entity.MiniPekka;
import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class PancakeItem extends Item {
    public PancakeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos clickedPos = ctx.getClickedPos();

        BlockPos ironPos;
        BlockPos headPos;

        if (level.getBlockState(clickedPos).is(Blocks.IRON_BLOCK)
                && level.getBlockState(clickedPos.above()).is(ModBlocks.MINI_PK_HEAD.get())) {
            ironPos = clickedPos;
            headPos = clickedPos.above();
        } else if (level.getBlockState(clickedPos).is(ModBlocks.MINI_PK_HEAD.get())
                && level.getBlockState(clickedPos.below()).is(Blocks.IRON_BLOCK)) {
            headPos = clickedPos;
            ironPos = clickedPos.below();
        } else {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) return InteractionResult.SUCCESS;

        ServerLevel server = (ServerLevel) level;
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();

        MiniPekka mp = MPekkaEntities.MPEKKA.get().create(server);
        if (mp == null) return InteractionResult.FAIL;

        double sx = ironPos.getX() + 0.5D;
        double sy = ironPos.getY() + 1.0D;
        double sz = ironPos.getZ() + 0.5D;
        mp.moveTo(sx, sy, sz, server.random.nextFloat() * 360F, 0F);

        if (player != null) {
            try {
                mp.tame(player);
            } catch (Throwable ignore) {
                mp.setTame(true);
                mp.setOwnerUUID(player.getUUID());
            }
        }
        mp.setPersistenceRequired();
        var max = mp.getAttribute(Attributes.MAX_HEALTH);
        if (max != null) mp.setHealth((float) max.getValue());

        server.removeBlock(headPos, false);
        server.removeBlock(ironPos, false);

        if (!server.addFreshEntity(mp)) {
            return InteractionResult.FAIL;
        }

        if (player == null || !player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        server.playSound(null, sx, sy, sz, ModSounds.PANCAKES.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
        return InteractionResult.CONSUME;
    }
}
