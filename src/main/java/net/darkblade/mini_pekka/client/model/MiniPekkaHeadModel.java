package net.darkblade.mini_pekka.client.model;

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MiniPekkaHeadModel extends SkullModel {

    private final ModelPart head;

    public MiniPekkaHeadModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
    }

    public static MeshDefinition createMiniPekkaHeadModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Offset Z = 0 para centrar en el bloque.
        // Todas las boxes ajustadas para compensar el antiguo offset Z de +0.25
        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        // Cuerpo principal: 8x10x8, centrado en X y Z
                        .texOffs(0, 0).addBox(-4.0F, 2.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                        // Visor frontal
                        .texOffs(16, 18).addBox(-4.0F, 8.0F, -5.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                        // Oreja derecha
                        .texOffs(0, 18).addBox(4.0F, 4.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                        // Cuerno derecho
                        .texOffs(16, 23).addBox(6.0F, 1.0F, -0.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                        // Cuerno izquierdo
                        .texOffs(16, 23).mirror().addBox(-8.0F, 1.0F, -0.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                        // Oreja izquierda
                        .texOffs(0, 18).mirror().addBox(-8.0F, 4.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, -12.0F, 0.0F));

        return mesh;
    }

    public static LayerDefinition createMiniPekkaHeadLayer() {
        MeshDefinition meshdefinition = createMiniPekkaHeadModel();
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(float animationTick, float yRot, float xRot) {
        super.setupAnim(animationTick, yRot, xRot);
    }
}
