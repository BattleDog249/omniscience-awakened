package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.battle.omniscience.config.ConfigManager;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;

/**
 * Forces rendering of name tags based on the current config state.
 */
@Mixin(LivingEntityRenderer.class)
public class ForceNameTagMixin {
    @Inject(at = @At("HEAD"), method = "shouldShowName(Lnet/minecraft/world/entity/Entity;D)Z", cancellable = true)
    private void onShouldShowName(Entity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        if (!ConfigManager.getConfig().isEnabled()) {
            return;
        }

        int mode = ConfigManager.getConfig().getForceRenderNameTags();

        if (mode == 2) {
            cir.setReturnValue(true);
        } else if (mode == 1 && entity.isCrouching()) {
            cir.setReturnValue(true);
        }
    }
}
