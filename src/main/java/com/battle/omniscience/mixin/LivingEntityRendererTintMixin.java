package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.battle.omniscience.config.ConfigManager;
import com.battle.omniscience.util.ColorUtil;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/**
 * Adjusts the model tint alpha for invisible entities when the mod is enabled.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererTintMixin {

    @Inject(at = @At("RETURN"), method = "getModelTint(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)I", cancellable = true)
    private void onGetModelTint(LivingEntityRenderState renderState, CallbackInfoReturnable<Integer> cir) {
        if (!ConfigManager.getConfig().isEnabled()) {
            return;
        }

        if (!renderState.isInvisible || renderState.isInvisibleToPlayer) {
            return;
        }

        int baseTint = cir.getReturnValue();
        if (baseTint == -1) {
            baseTint = 0xFFFFFFFF;
        }

        int alpha = Math.round(ConfigManager.getConfig().alpha * 255f);
        cir.setReturnValue(ColorUtil.withAlpha(baseTint, alpha));
    }
}
