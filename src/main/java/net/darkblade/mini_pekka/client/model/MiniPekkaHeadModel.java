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
                .texOffs(2, 20).addBox(-4.0F, 4.0F, -3.25F, 8.0F, 8.0F, 6.4F, new CubeDeformation(0.0F))
                        .texOffs(16, 38).addBox(-4.0F, 8.8F, -4.05F, 8.0F, 3.2F, 0.8F, new CubeDeformation(0.0F))
                        .texOffs(38, 39).addBox(4.0F, 5.6F, -0.85F, 3.2F, 3.2F, 3.2F, new CubeDeformation(0.0F))
                        .texOffs(46, 8).addBox(5.6F, 3.2F, -0.45F, 1.6F, 2.4F, 2.4F, new CubeDeformation(0.0F))
                        .texOffs(46, 8).mirror().addBox(-7.2F, 3.2F, -0.45F, 1.6F, 2.4F, 2.4F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(38, 39).mirror().addBox(-7.2F, 5.6F, -0.85F, 3.2F, 3.2F, 3.2F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(2, 57).addBox(-4.0F, 4.0F, -3.25F, 5.6F, 0.0F, 5.6F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.25F)

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
