package net.darkblade.mini_pekka.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;


public class RenderHelper extends RenderStateShard {

    private RenderHelper() {
        super("dummy", () -> {}, () -> {});
    }

    public static RenderType entityOverlayEmissive(ResourceLocation texture) {
        return RenderType.create(
                "pekka_evo_ability_overlay",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false, true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setWriteMaskState(COLOR_WRITE)
                        .setOverlayState(OVERLAY)
                        .setLayeringState(POLYGON_OFFSET_LAYERING)
                        .createCompositeState(false)
        );
    }
}
