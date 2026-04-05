package com.battle.omniscience.config;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.annotations.Expose;
import com.battle.omniscience.Omniscience;
import com.battle.omniscience.access.EntityMixinAccess;
import com.battle.omniscience.util.EntityTargetGroup;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class Config {

    @Expose public boolean enabled = true;
    @Expose public float alpha = 0.40f;
    @Expose public int invisibleEntityGlow = 0;
    @Expose public int entityTargetGroup = EntityTargetGroup.PLAYER | EntityTargetGroup.MONSTER | EntityTargetGroup.VILLAGER | EntityTargetGroup.ANIMAL;
    @Expose public int forceRenderNameTags = 0;
    @Expose public boolean exposeBarrierBlocks = false;
    @Expose public boolean removeBlindnessEffect = false;
    @Expose public boolean onlyEnableInCreative = false;

    @Expose public static final String[] ForceRenderNameTagsStrings = {
            Component.translatable("config.generic.misc.name_tags.option.never").getString(),
            Component.translatable("config.generic.misc.name_tags.option.when_sneaking").getString(),
            Component.translatable("config.generic.misc.name_tags.option.always").getString(),
    };

    @Expose public static final String[] InvisibleEntityGlowStrings = {
            Component.translatable("config.generic.glow.option.none").getString(),
            Component.translatable("config.generic.glow.option.player").getString(),
            Component.translatable("config.generic.glow.option.match_targets").getString(),
    };

    /**
     * Returns true if the mod is enabled and allowed in the current creative mode setting.
     */
    public boolean isEnabled() {
        return enabled && (!onlyEnableInCreative || Omniscience.isCreative);
    }

    public void setAlphaPercent(int i) {
        alpha = ((float) i) / 100;
    }

    public int getAlphaPercent() {
        return (int) (alpha * 100);
    }

    public void setForceRenderNameTags(int i) {
        if (i >= 0 && i < ForceRenderNameTagsStrings.length) {
            forceRenderNameTags = i;
        }
    }

    public int getForceRenderNameTags() {
        return forceRenderNameTags;
    }

    public void setForceRenderNameTags(String t) {
        setForceRenderNameTags(ArrayUtils.indexOf(ForceRenderNameTagsStrings, t));
    }

    public String getForceRenderNameTagsString() {
        int index = Math.max(0, Math.min(forceRenderNameTags, ForceRenderNameTagsStrings.length - 1));
        return ForceRenderNameTagsStrings[index];
    }

    public void setInvisibleEntityGlow(int i) {
        if (i >= 0 && i < InvisibleEntityGlowStrings.length) {
            invisibleEntityGlow = i;
        }
    }

    public int getInvisibleEntityGlow() {
        return invisibleEntityGlow;
    }

    public void setInvisibleEntityGlow(String t) {
        setInvisibleEntityGlow(ArrayUtils.indexOf(InvisibleEntityGlowStrings, t));
    }

    public String getInvisibleEntityGlowString() {
        int index = Math.max(0, Math.min(getInvisibleEntityGlow(), InvisibleEntityGlowStrings.length - 1));
        return InvisibleEntityGlowStrings[index];
    }

    public void setEntityTargetGroup(int group, boolean active) {
        this.entityTargetGroup = active ? this.entityTargetGroup | group : this.entityTargetGroup & ~group;
    }

    public boolean isGroupTargeted(int group) {
        return (this.entityTargetGroup & group) > 0;
    }

    public boolean isEntityTargeted(Entity entity) {
        return isGroupTargeted(((EntityMixinAccess) entity).getEntityTargetGroup());
    }

    public boolean shouldGroupGlow(int group) {
        return ((invisibleEntityGlow == 1 && (group & EntityTargetGroup.PLAYER) > 0) ||
                (invisibleEntityGlow == 2 && isGroupTargeted(group)));
    }

    public boolean shouldEntityGlow(Entity entity) {
        return shouldGroupGlow(((EntityMixinAccess) entity).getEntityTargetGroup());
    }

    /**
     * Validates configuration values and clamps them to safe ranges.
     *
     * @return true if no values were changed during validation
     */
    public boolean validate() {
        boolean valid = true;

        if (alpha < 0) {
            alpha = 0;
            valid = false;
        } else if (alpha > 1) {
            alpha = 1;
            valid = false;
        }

        if (invisibleEntityGlow < 0) {
            invisibleEntityGlow = 0;
            valid = false;
        } else if (invisibleEntityGlow >= InvisibleEntityGlowStrings.length) {
            invisibleEntityGlow = InvisibleEntityGlowStrings.length - 1;
            valid = false;
        }

        if (entityTargetGroup < 0 || entityTargetGroup >= 64) {
            entityTargetGroup = 0;
            valid = false;
        }

        return valid;
    }
}
