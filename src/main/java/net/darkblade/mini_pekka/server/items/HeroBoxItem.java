package net.darkblade.mini_pekka.server.items;

import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeroBoxItem extends Item {
    public HeroBoxItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            double chance = level.random.nextDouble();
            ItemStack reward;

            if (chance < 0.10) {
                reward = new ItemStack(ModItems.HERO_SHARDS_BIG.get(), 1);
            } else if (chance < 0.45) {
                int count = level.random.nextInt(2) + 1;
                reward = new ItemStack(ModItems.HERO_SHARDS_MEDIUM.get(), count);
            } else {
                int count = level.random.nextInt(2) + 2;
                reward = new ItemStack(ModItems.HERO_SHARDS_SMALL.get(), count);
            }

            if (!player.getInventory().add(reward)) {
                player.drop(reward, false);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.HERO_BOX_OPEN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}