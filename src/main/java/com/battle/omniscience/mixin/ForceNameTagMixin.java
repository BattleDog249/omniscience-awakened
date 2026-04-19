package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.battle.omniscience.config.ConfigManager;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

/**
 * Forces player name tags to render when the corresponding config mode is enabled.
 */
@Mixin(AvatarRenderer.class)
public class ForceNameTagMixin {

    @Inject(
        at = @At("HEAD"),
        method = "shouldShowName(Lnet/minecraft/world/entity/Avatar;D)Z",
        cancellable = true
    )
    private void onShouldShowName(Entity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        var config = ConfigManager.getConfig();
        if (config == null || !config.isEnabled()) {
            return;
        }

        if (!(entity instanceof Player player)) {
            return;
        }

        int mode = config.getForceRenderNameTags();
        if (mode == 2 || (mode == 1 && player.isCrouching())) {
            cir.setReturnValue(true);
        }
    }
}
