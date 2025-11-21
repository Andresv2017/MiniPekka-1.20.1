package net.darkblade.mini_pekka.server.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkblade.mini_pekka.client.ModSkullBlockRenderer;
import net.darkblade.mini_pekka.server.block.ModSkullBlock; // Tu SkullBlock con Types.MINI_PEKKA
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SkullBlock;

import java.util.Map;

public class ModSkullItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;

    public static final ModSkullItemRenderer INSTANCE = new ModSkullItemRenderer(
            Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels()
    );

    public static ModSkullItemRenderer getInstance() {
        return INSTANCE;
    }

    public ModSkullItemRenderer(BlockEntityRenderDispatcher bed, EntityModelSet modelSet) {
        super(bed, modelSet);
        this.skullModels = ModSkullBlockRenderer.createSkullRenderers(modelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose,
                             MultiBufferSource buffers, int light, int overlay) {
        if (!(stack.getItem() instanceof BlockItem bi)) return;
        if (!(bi.getBlock() instanceof SkullBlock skull)) return;

        SkullBlock.Type type = skull.getType();
        if (type != ModSkullBlock.Types.MINI_PEKKA) return;

        SkullModelBase model = this.skullModels.get(type);
        if (model == null) return;

        RenderType rt = SkullBlockRenderer.getRenderType(type, null);
        ModSkullBlockRenderer.renderSkull(null, 180.0F, 0.0F, pose, buffers, light, model, rt);
    }
}
