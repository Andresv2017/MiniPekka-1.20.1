package net.darkblade.mini_pekka.server.items;

import net.darkblade.mini_pekka.server.entity.projectile.RageThrownPotion;
import net.darkblade.mini_pekka.sounds.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;

public class RagePotionItem extends PotionItem {

    public RagePotionItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return this.getDescriptionId();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.RAGE_THROW.get(), SoundSource.NEUTRAL, 0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            RageThrownPotion potion = new RageThrownPotion(level, player);
            potion.setItem(itemstack);
            potion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.7F, 1.0F);
            level.addFreshEntity(potion);
        }

        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
            player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}