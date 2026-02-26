package net.darkblade.mini_pekka.client.model;

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PekkaHeadModel extends SkullModel {

    private final ModelPart head;

    public PekkaHeadModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
    }

    public static MeshDefinition createPekkaHeadModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(46, 51).addBox(-4.5F, -11.5F, -4.75F, 9.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
                        .texOffs(22, 72).addBox(4.5F, -9.5F, -0.75F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(22, 72).mirror().addBox(-12.5F, -9.5F, -0.75F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, 23.4F, -0.75F));

        head.addOrReplaceChild("belly_r1",
                CubeListBuilder.create()
                        .texOffs(66, 78).mirror().addBox(-6.0F, -3.0F, -1.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-12.5F, -6.5F, 1.25F, 0.0F, 0.0F, 0.7854F));

        head.addOrReplaceChild("belly_r2",
                CubeListBuilder.create()
                        .texOffs(66, 78).addBox(0.0F, -3.0F, -1.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(12.5F, -6.5F, 1.25F, 0.0F, 0.0F, -0.7854F));

        return mesh;
    }

    public static LayerDefinition createPekkaHeadLayer() {
        MeshDefinition meshdefinition = createPekkaHeadModel();
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(float animationTick, float yRot, float xRot) {
        super.setupAnim(animationTick, yRot, xRot);
    }
}
