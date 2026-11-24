package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.battle.omniscience.config.ConfigManager;

import net.minecraft.client.renderer.fog.environment.BlindnessFogEnvironment;
import net.minecraft.client.renderer.fog.environment.MobEffectFogEnvironment;

/**
 * Prevents blindness fog rendering when the related config option is enabled.
 *
 * <p>Mixin can only inject into methods declared in the target class's own bytecode,
 * so the {@code instanceof} guard is required to exclude sibling subclasses
 * (e.g. {@code DarknessFogEnvironment}).
 */
@Mixin(MobEffectFogEnvironment.class)
public class BlindnessFogMixin {

    @Inject(at = @At("HEAD"), method = "isApplicable", cancellable = true)
    private void onIsApplicable(net.minecraft.world.level.material.FogType fogType,
            net.minecraft.world.entity.Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (ConfigManager.getConfig().isEnabled() && ConfigManager.getConfig().removeBlindnessEffect
                && (Object) this instanceof BlindnessFogEnvironment) {
            info.setReturnValue(false);
        }
    }

}
