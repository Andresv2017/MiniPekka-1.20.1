package net.darkblade.mini_pekka.client;

import net.darkblade.mini_pekka.MiniPekkaMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class ModBlockEntityModelLayers {
    public static final ModelLayerLocation MINI_PK_HEAD = register("mini_pk_head");

    private static ModelLayerLocation register(String layer) {
        return new ModelLayerLocation(new ResourceLocation(MiniPekkaMod.MODID, layer), "main");
    }

    private ModBlockEntityModelLayers() {}
}
