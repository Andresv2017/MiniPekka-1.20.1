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

        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 19).addBox(-13.0F, -10.0F, 5.75F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(14, 37).addBox(-13.0F, -4.0F, 4.75F, 10.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(36, 38).addBox(-3.0F, -8.0F, 8.75F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(44, 7).addBox(-1.0F, -11.0F, 9.25F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(44, 7).mirror().addBox(-17.0F, -11.0F, 9.25F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(36, 38).mirror().addBox(-17.0F, -8.0F, 8.75F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(9F, 1.2F, 7.9F));

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