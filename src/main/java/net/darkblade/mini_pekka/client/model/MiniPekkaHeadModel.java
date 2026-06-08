package net.darkblade.mini_pekka.client.model;

import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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

        // Centered like PekkaHeadModel: offset (0,0,0), boxes centered around origin
        // Original center was X=-8+9=1 → shift boxes by +4 to center X
        // Original center Z=9.75+7.9=17.65 → shift boxes by -9.75 to center Z
        // Original Y offset 1.2 → absorbed into boxes
        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        // Main head box: 10x10x8
                        .texOffs(0, 19).addBox(-5.0F, -10.0F, -4.0F, 10.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                        // Jaw/visor: 10x4x1
                        .texOffs(14, 37).addBox(-5.0F, -4.0F, -5.0F, 10.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                        // Right horn base: 4x4x4
                        .texOffs(36, 38).addBox(5.0F, -8.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                        // Right horn tip: 2x3x3
                        .texOffs(44, 7).addBox(7.0F, -11.0F, -0.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                        // Left horn tip: 2x3x3
                        .texOffs(44, 7).mirror().addBox(-9.0F, -11.0F, -0.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                        // Left horn base: 4x4x4
                        .texOffs(36, 38).mirror().addBox(-9.0F, -8.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, 0.0F, 0.0F));

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