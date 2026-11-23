package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.battle.omniscience.config.ConfigManager;
import com.battle.omniscience.util.ColorUtil;

import net.minecraft.client.model.geom.ModelPart;

/**
 * Adjusts transparency of model part rendering for revealed invisible entities.
 */
@Mixin(ModelPart.class)
public class ModelPartAlphaMixin {

    @ModifyVariable(at = @At("HEAD"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V", ordinal = 2, argsOnly = true)
    private int onRender(int color) {
        if (!ConfigManager.getConfig().isEnabled()) {
            return color;
        }

        int alpha = (color >> 24) & 0xFF;
        if (alpha != 0xFF) {
            int newAlpha = Math.round(ConfigManager.getConfig().alpha * 255f);
            return ColorUtil.withAlpha(color, newAlpha);
        }

        return color;
    }
}
