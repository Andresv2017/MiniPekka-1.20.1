package net.darkblade.mini_pekka.client;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.darkblade.mini_pekka.server.block.ModSkullBlock;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;

/**
 * Capa mínima para renderizar la cabeza de Mini Pekka como casco.
 */
public class ModSkullHeadLayer<T extends LivingEntity, M extends EntityModel<T> & HeadedModel>
        extends CustomHeadLayer<T, M> {

    private final ItemInHandRenderer itemInHandRenderer;
    private final SkullModelBase miniPekkaSkull;

    public ModSkullHeadLayer(RenderLayerParent<T, M> parent, EntityModelSet models,
                             ItemInHandRenderer inHandRenderer) {
        super(parent, models, 1.0F, 1.0F, 1.0F, inHandRenderer);
        this.itemInHandRenderer = inHandRenderer;
        this.miniPekkaSkull = new SkullModel(models.bakeLayer(ModBlockEntityModelLayers.MINI_PK_HEAD));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack head = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (head.isEmpty()) return;

        Item item = head.getItem();
        pose.pushPose();

        boolean isVillager = (entity instanceof Villager) || (entity instanceof ZombieVillager);
        if (entity.isBaby() && !(entity instanceof Villager)) {
            pose.translate(0.0F, 0.03125F, 0.0F);
            pose.scale(0.7F, 0.7F, 0.7F);
            pose.translate(0.0F, 1.0F, 0.0F);
        }

        this.getParentModel().getHead().translateAndRotate(pose);

        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock skullBlock) {
            SkullBlock.Type type = skullBlock.getType();
            if (type == ModSkullBlock.Types.MINI_PEKKA) {
                pose.scale(0.5f, -0.5F, -0.5F);
                if (isVillager) pose.translate(0.0F, 0.0625F, 0.0F);
                pose.translate(-0.5D, 0.0D, -0.5D);

                GameProfile profile = null;
                if (head.hasTag()) {
                    CompoundTag tag = head.getTag();
                    if (tag != null && tag.contains("SkullOwner", 10)) {
                        profile = NbtUtils.readGameProfile(tag.getCompound("SkullOwner"));
                    }
                }

                RenderType rt = SkullBlockRenderer.getRenderType(type, profile);
                float anim = entity.walkAnimation.position(partialTicks);

                ModSkullBlockRenderer.renderSkull(
                        null,
                        180.0F,
                        anim,
                        pose, buffers, light,
                        miniPekkaSkull, rt
                );
                pose.popPose();
                return;
            }
        }

        if (!(item instanceof ArmorItem armor) || armor.getEquipmentSlot() != EquipmentSlot.HEAD) {
            translateToHead(pose, isVillager);
            this.itemInHandRenderer.renderItem(entity, head, ItemDisplayContext.HEAD, false, pose, buffers, light);
        }

        pose.popPose();
    }
}
