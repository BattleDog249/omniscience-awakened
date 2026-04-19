package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.battle.omniscience.config.ConfigManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Makes barrier blocks visible by forcing the client marker particle target to BARRIER
 * when the expose barrier blocks option is enabled.
 */
@Mixin(ClientLevel.class)
public class BarrierMarkerMixin {
    @Inject(at = @At("HEAD"), method = "getMarkerParticleTarget", cancellable = true)
    private void onGetMarkerParticleTarget(CallbackInfoReturnable<Block> cir) {
        if (ConfigManager.getConfig().isEnabled() && ConfigManager.getConfig().exposeBarrierBlocks) {
            if (Minecraft.getInstance().player != null) {
                cir.setReturnValue(Blocks.BARRIER);
            }
        }
    }
}
