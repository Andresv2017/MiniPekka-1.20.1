package net.darkblade.mini_pekka.server.items;

import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class GoldenSpatulaItem extends SwordItem {

    public GoldenSpatulaItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        if (target.isDeadOrDying() && attacker instanceof Player player) {
            if (!player.level().isClientSide) {

                player.heal(2.0F);

                long currentTime = player.level().getGameTime();
                long lastSoundTime = stack.getOrDefault(ModDataComponents.LAST_PANCAKE_SOUND_TIME.get(), 0L);

                if (currentTime - lastSoundTime >= 200) {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            ModSounds.PANCAKES.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                    stack.set(ModDataComponents.LAST_PANCAKE_SOUND_TIME.get(), currentTime);
                }
            }
        }

        return result;
    }
}
