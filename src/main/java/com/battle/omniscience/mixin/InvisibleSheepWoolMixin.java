package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.battle.omniscience.config.ConfigManager;
import com.battle.omniscience.util.EntityTargetGroup;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

/**
 * Ensures invisible sheep wool is rendered correctly when animal targets are revealed.
 */
@Mixin(SheepWoolLayer.class)
public class InvisibleSheepWoolMixin {
    @Shadow @Final private static Identifier SHEEP_WOOL_LOCATION;
    @Shadow @Final private EntityModel<SheepRenderState> adultModel;
    @Shadow @Final private EntityModel<SheepRenderState> babyModel;

    @Inject(
            at = @At("HEAD"),
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SheepRenderState;FF)V",
            cancellable = true)
    private void onSubmit(PoseStack matrixStack, net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int light,
            SheepRenderState sheepRenderState, float f, float g, CallbackInfo ci) {
        if (!ConfigManager.getConfig().isEnabled()) {
            return;
        }

        if (!ConfigManager.getConfig().isGroupTargeted(EntityTargetGroup.ANIMAL)) {
            return;
        }

        if (sheepRenderState.isSheared) {
            return;
        }

        if (!(sheepRenderState.isInvisible || sheepRenderState.isInvisibleToPlayer)) {
            return;
        }

        MultiBufferSource.BufferSource entityVertexConsumers = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer2 = entityVertexConsumers.getBuffer(RenderTypes.entityTranslucent(SHEEP_WOOL_LOCATION));
        EntityModel<SheepRenderState> model = sheepRenderState.isBaby ? this.babyModel : this.adultModel;
        int color = sheepRenderState.getWoolColor() & 0x26FFFFFF;
        int overlay = LivingEntityRenderer.getOverlayCoords(sheepRenderState, 0.0f);

        model.renderToBuffer(matrixStack, vertexConsumer2, light, overlay, color);
        ci.cancel();
    }
}
