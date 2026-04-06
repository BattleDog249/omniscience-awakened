package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.battle.omniscience.config.ConfigManager;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;

/**
 * Forces player name tag rendering based on the current config setting.
 */
@Mixin(AvatarRenderer.class)
public class ForceNameTagMixin {
    private static int diagnosticCalls = 0;

    @Inject(
        at = @At("HEAD"),
        method = {
            "shouldShowName(Lnet/minecraft/world/entity/Avatar;D)Z",
            "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z",
            "shouldShowName(Lnet/minecraft/world/entity/Entity;D)Z"
        },
        cancellable = true
    )
    private void onShouldShowName(Avatar avatar, double distance, CallbackInfoReturnable<Boolean> cir) {
        var config = ConfigManager.getConfig();
        if (config == null) {
            if (diagnosticCalls < 5) {
                System.out.println("[Omniscience] ForceNameTagMixin: config is null");
            }
            diagnosticCalls++;
            return;
        }

        if (!config.isEnabled()) {
            if (diagnosticCalls < 5) {
                System.out.println("[Omniscience] ForceNameTagMixin: mod disabled");
            }
            diagnosticCalls++;
            return;
        }

        boolean isPlayer = avatar instanceof Player;
        if (diagnosticCalls < 20) {
            System.out.println("[Omniscience] ForceNameTagMixin called on " + avatar.getClass().getSimpleName() +
                    " name=" + avatar.getName().getString() +
                    " isPlayer=" + isPlayer +
                    " crouching=" + (isPlayer ? ((Player) avatar).isCrouching() : "n/a") +
                    " mode=" + config.getForceRenderNameTags());
        }

        if (!isPlayer) {
            diagnosticCalls++;
            return;
        }

        Player player = (Player) avatar;
        int mode = config.getForceRenderNameTags();
        if (mode == 2 || (mode == 1 && player.isCrouching())) {
            if (diagnosticCalls < 20) {
                System.out.println("[Omniscience] ForceNameTagMixin forcing show name for player=" + player.getName().getString() +
                        " crouching=" + player.isCrouching() + " mode=" + mode);
            }
            cir.setReturnValue(true);
        } else if (diagnosticCalls < 20) {
            System.out.println("[Omniscience] ForceNameTagMixin leaving default for player=" + player.getName().getString() +
                    " crouching=" + player.isCrouching() + " mode=" + mode);
        }

        diagnosticCalls++;
    }

    @Inject(at = @At("HEAD"), method = "submitNameTag(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V")
    private void onSubmitNameDisplay(AvatarRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
        if (diagnosticCalls < 20) {
            boolean renderNames = Minecraft.getInstance().renderNames();
            String nameTag = state.nameTag == null ? "null" : state.nameTag.getString();
            String scoreText = state.scoreText == null ? "null" : state.scoreText.getString();
            System.out.println("[Omniscience] ForceNameTagMixin submitNameDisplay called renderNames=" + renderNames +
                    " isCrouching=" + state.isCrouching +
                    " isInvisibleToPlayer=" + state.isInvisibleToPlayer +
                    " nameTag=" + nameTag +
                    " scoreText=" + scoreText);
        }
        diagnosticCalls++;
    }
}
