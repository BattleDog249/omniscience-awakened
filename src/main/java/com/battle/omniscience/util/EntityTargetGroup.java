package com.battle.omniscience.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;

/**
 * Utility helper for mapping entities to configured target groups.
 */
public class EntityTargetGroup {
    // Bitmask values for entity target categories.
    public static final int ALL = 1;
    public static final int PLAYER = 2;
    public static final int MONSTER = 4;
    public static final int VILLAGER = 8;
    public static final int ANIMAL = 16;
    public static final int OBJECT = 32;

    public static int getEntityGroup(Entity entity) {
        int entityGroup = 0;

        if (entity instanceof Player) {
            entityGroup = EntityTargetGroup.PLAYER;
        } else if (entity instanceof Monster
                || entity instanceof Slime
                || entity instanceof Ghast
                || entity instanceof Phantom
                || entity instanceof EnderDragon) {
            entityGroup = EntityTargetGroup.MONSTER;
        } else if (entity instanceof WanderingTrader
                || entity instanceof Villager) {
            entityGroup = EntityTargetGroup.VILLAGER;
        } else if (entity instanceof Animal
                || entity instanceof WaterAnimal
                || entity instanceof AbstractGolem
                || entity instanceof AmbientCreature) {
            entityGroup = EntityTargetGroup.ANIMAL;
        } else if (entity instanceof ArmorStand
                || entity instanceof Painting) {
            entityGroup = EntityTargetGroup.OBJECT;
        }

        return entityGroup | EntityTargetGroup.ALL;
    }
}
