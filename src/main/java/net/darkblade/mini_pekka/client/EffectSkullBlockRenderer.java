package net.darkblade.mini_pekka.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.authlib.GameProfile;
import net.darkblade.mini_pekka.server.block.EffectSkullBlock;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import net.darkblade.mini_pekka.MiniPekkaMod;

@OnlyIn(Dist.CLIENT)
public class EffectSkullBlockRenderer extends SkullBlockRenderer implements BlockEntityRenderer<SkullBlockEntity> {
    private final Map<SkullBlock.Type, SkullModelBase> modelByType;

    public static final ResourceLocation MINI_PEKKA_HEAD_TEX =
            new ResourceLocation(MiniPekkaMod.MODID, "textures/entity/mini_pk_head/mini_pk_head.png");

    public EffectSkullBlockRenderer(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
        this.modelByType = createSkullRenderers(ctx.getModelSet());

        SKIN_BY_TYPE.put(EffectSkullBlock.Types.MINI_PEKKA, MINI_PEKKA_HEAD_TEX);
    }

    @Override
    public void render(SkullBlockEntity be, float partialTicks, PoseStack pose, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        float anim = be.getAnimation(partialTicks);

        BlockState state = be.getBlockState();
        boolean wall = state.getBlock() instanceof WallSkullBlock;
        @Nullable Direction facing = wall ? state.getValue(WallSkullBlock.FACING) : null;

        int rotSegment = wall
                ? RotationSegment.convertToSegment(facing.getOpposite())
                : state.getValue(SkullBlock.ROTATION);

        float yRotDeg = RotationSegment.convertToDegrees(rotSegment);

        SkullBlock.Type type = ((AbstractSkullBlock) state.getBlock()).getType();
        SkullModelBase model = this.modelByType.get(type);

        RenderType renderType = getRenderType(type, be.getOwnerProfile());
        renderSkull(facing, yRotDeg, anim, pose, buffers, packedLight, model, renderType);
    }

    /** Igual que en el ejemplo: coloca el skull sobre bloque o pared y dibuja el modelo */
    public static void renderSkull(@Nullable Direction facing, float yRotDeg, float anim,
                                   PoseStack pose, MultiBufferSource buffers, int light,
                                   SkullModelBase model, RenderType rt) {
        pose.pushPose();
        if (facing == null) {
            pose.translate(0.5F, 0.0F, 0.5F);
        } else {
            float horizontal = 0.25F;
            float vertical = 0.25F;
            pose.translate(0.5F - (float)facing.getStepX() * horizontal, vertical,
                    0.5F - (float)facing.getStepZ() * horizontal);
        }

        pose.scale(-1.0F, -1.0F, 1.0F);

        VertexConsumer vc = buffers.getBuffer(rt);
        model.setupAnim(anim, yRotDeg, 0.0F);
        model.renderToBuffer(pose, vc, light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

        pose.popPose();
    }

    /** Crea el mapa de tipo->modelo. Sólo nuestro tipo MINI_PEKKA. */
    public static Map<SkullBlock.Type, SkullModelBase> createSkullRenderers(EntityModelSet models) {
        ImmutableMap.Builder<SkullBlock.Type, SkullModelBase> builder = ImmutableMap.builder();

        builder.put(EffectSkullBlock.Types.MINI_PEKKA, new SkullModel(models.bakeLayer(ModBlockEntityModelLayers.MINI_PK_HEAD)));

        return builder.build();
    }

    /** Reenvía al método estático de SkullBlockRenderer */
    public static RenderType getRenderType(SkullBlock.Type type, @Nullable GameProfile profile) {
        return SkullBlockRenderer.getRenderType(type, profile);
    }
}

