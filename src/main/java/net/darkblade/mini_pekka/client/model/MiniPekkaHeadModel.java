package net.darkblade.mini_pekka.client.model;

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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

        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox( -5.0F, -10.0F, -4.0F, 10.0F, 10.0F, 8.0F)             // y'= -10..0
                        .texOffs(0, 26).addBox(-5.0F,  -4.0F, -5.0F, 10.0F,  4.0F, 1.0F)             // y'= -4..0
                        .texOffs(22, 26).addBox( 5.0F,  -8.0F, -1.0F,  4.0F,  4.0F, 4.0F)            // y'= -8..-4 (lado der)
                        .texOffs(0, 31).addBox(  7.0F, -11.0F, -0.5F, 2.0F,  3.0F, 3.0F)             // punta der
                        .texOffs(0, 31).mirror().addBox(-9.0F, -11.0F, -0.5F, 2.0F, 3.0F, 3.0F).mirror(false) // punta izq
                        .texOffs(22, 26).mirror().addBox(-9.0F,  -8.0F, -1.0F, 4.0F, 4.0F, 4.0F).mirror(false) // lado izq
                        .texOffs(0, 18).addBox( -5.0F, -11.0F, -4.0F, 7.0F, 1.0F, 7.0F)              // placa superior
                        .texOffs(28, 18).addBox(-5.0F, -11.0F, -5.0F, 7.0F, 4.0F, 1.0F),             // borde frontal
                PartPose.ZERO
        );

        return mesh;
    }

    /** LayerDefinition 64x64, igual que tu modelo original. */
    public static LayerDefinition createMiniPekkaHeadLayer() {
        MeshDefinition meshdefinition = createMiniPekkaHeadModel();
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(float animationTick, float yRot, float xRot) {
        super.setupAnim(animationTick, yRot, xRot);
    }
}
