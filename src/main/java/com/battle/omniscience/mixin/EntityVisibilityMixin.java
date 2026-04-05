package com.battle.omniscience.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.battle.omniscience.access.EntityMixinAccess;
import com.battle.omniscience.config.ConfigManager;
import com.battle.omniscience.util.EntityTargetGroup;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Entity.class)
public abstract class EntityVisibilityMixin implements EntityMixinAccess {
    private int entityTargetGroup;

    @Override
    public int getEntityTargetGroup() {
        return entityTargetGroup;
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(EntityType<?> type, Level world, CallbackInfo info) {
        entityTargetGroup = EntityTargetGroup.getEntityGroup((Entity) (Object) this);
    }

    @Inject(at = @At("HEAD"), method = "isInvisibleTo", cancellable = true)
    private void onIsInvisibleTo(Player player, CallbackInfoReturnable<Boolean> info) {
        if (ConfigManager.getConfig().isEnabled()) {
            if (ConfigManager.getConfig().isGroupTargeted(entityTargetGroup)) {
                info.setReturnValue(false);
            }
        } else if (player.isSpectator()) {
            // Please fix me: Find another way to hide invisible entities in replay of ReplayMod
            info.setReturnValue(true);
        }
    }
}
