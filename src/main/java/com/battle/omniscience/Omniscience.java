package com.battle.omniscience;

import org.lwjgl.glfw.GLFW;

import com.battle.omniscience.config.ConfigManager;
import com.battle.omniscience.gui.ScreenBuilder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;

/**
 * The main client entry point for the Omniscience mod.
 */
public class Omniscience implements ClientModInitializer {
    public static final String MOD_ID = "omniscience";
    public static final String MOD_NAME = "Omniscience";
    public static final String CONFIG_FILE_NAME = Omniscience.MOD_ID + ".json";

    // The translation key of the keybinding category.
    public static final KeyMapping.Category OMNISCIENCE = new KeyMapping.Category(
            Identifier.fromNamespaceAndPath("minecraft", Omniscience.MOD_ID));

    private KeyMapping keyBindingOpenSettings;
    private KeyMapping keyToggleEnabled;
    public static boolean isCreative = true;

    @Override
    public void onInitializeClient() {
        ConfigManager.init();

        // adding keybinding to settings
        keyBindingOpenSettings = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.omniscience.settings",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                OMNISCIENCE));

        keyToggleEnabled = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.omniscience.enable",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                OMNISCIENCE));

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    public void tick(Minecraft client) {
        if (client.player != null) {
            Omniscience.isCreative = client.player.isCreative();
        }

        if (keyBindingOpenSettings.consumeClick()) {
            ScreenBuilder.openConfigScreen(client);
            return;
        }

        if (!keyToggleEnabled.consumeClick() || client.player == null) {
            return;
        }

        boolean enabled = ConfigManager.getConfig().enabled;
        ConfigManager.getConfig().enabled = !enabled;
        Component message = Component.translatable(enabled ? "message.disabled" : "message.enabled", MOD_NAME)
                .withStyle(enabled ? ChatFormatting.RED : ChatFormatting.GREEN);
        client.player.displayClientMessage(message, true);
    }
}