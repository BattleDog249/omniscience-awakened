package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.battle.omniscience.config.ConfigManager;

import net.minecraft.client.Minecraft;

/**
 * Forces glowing for invisible entities according to the configured glow mode.
 */
@Mixin(Minecraft.class)
public class EntityGlowMixin {
    @Inject(at = @At("HEAD"), method = "shouldEntityAppearGlowing", cancellable = true)
    private void onShouldEntityAppearGlowing(net.minecraft.world.entity.Entity entity,
            CallbackInfoReturnable<Boolean> info) {
        if (ConfigManager.getConfig().isEnabled()
                && entity.isInvisible()
                && ConfigManager.getConfig().shouldEntityGlow(entity)) {
            info.setReturnValue(true);
        }
    }
}
