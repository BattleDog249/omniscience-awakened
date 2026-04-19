package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.battle.omniscience.config.ConfigManager;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererModelTintMixin {

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
        alpha = Math.max(0, Math.min(255, alpha));
        int red = (baseTint >> 16) & 0xFF;
        int green = (baseTint >> 8) & 0xFF;
        int blue = baseTint & 0xFF;

        cir.setReturnValue((alpha << 24) | (red << 16) | (green << 8) | blue);
    }
}
